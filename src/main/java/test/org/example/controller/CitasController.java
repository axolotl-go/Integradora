package test.org.example.controller;

import org.axolotl.db.DBConnection;
import test.org.example.model.Cita;
import test.org.example.view.ViewCitas;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CitasController {
    private ViewCitas viewCitas;
    private Map<String, Integer> clienteNameToIdMap;
    private Map<String, Integer> mascotaNameToIdMap;

    public CitasController(ViewCitas viewCitas) {
        this.viewCitas = viewCitas;
        this.clienteNameToIdMap = new HashMap<>();
        this.mascotaNameToIdMap = new HashMap<>();

        // ActionListeners
        this.viewCitas.getBtnAdd().addActionListener(this::addCita);
        this.viewCitas.getBtnLoad().addActionListener(e -> {
            loadCitasFromDB();
        });

        // Listener para el ComboBox de clientes para cargar las mascotas en cascada
        this.viewCitas.getCmbClientes().addActionListener(this::onClienteSelected);

        // Carga inicial
        loadClientesIntoComboBox();
        loadCitasFromDB();
    }

    private void onClienteSelected(ActionEvent e) {
        String selectedClient = (String) viewCitas.getCmbClientes().getSelectedItem();
        if (selectedClient != null && !selectedClient.isEmpty()) {
            int clienteId = clienteNameToIdMap.get(selectedClient);
            loadMascotasByClientIntoComboBox(clienteId);
        } else {
            viewCitas.setMascotas(new ArrayList<>());
        }
    }

    private void addCita(ActionEvent e) {
        String fechaStr = viewCitas.getFecha();
        String horaStr = viewCitas.getHora();
        String motivo = viewCitas.getMotivo();
        String nombreCliente = (String) viewCitas.getCmbClientes().getSelectedItem();
        String nombreMascota = (String) viewCitas.getCmbMascotas().getSelectedItem();

        if (nombreCliente == null || nombreMascota == null || fechaStr.isEmpty() || horaStr.isEmpty() || motivo.isEmpty()) {
            JOptionPane.showMessageDialog(viewCitas, "Por favor, llene todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            LocalDate fecha = LocalDate.parse(fechaStr);
            LocalTime hora = LocalTime.parse(horaStr);
            LocalDateTime fechaHora = LocalDateTime.of(fecha, hora);

            int idMascota = mascotaNameToIdMap.get(nombreMascota);

            try (Connection conn = DBConnection.getConnection()) {
                String sql = "INSERT INTO citas (id_mascota, fecha_hora, motivo) VALUES (?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, idMascota);
                ps.setTimestamp(2, Timestamp.valueOf(fechaHora));
                ps.setString(3, motivo);

                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(viewCitas, "Cita agregada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    viewCitas.clearFields();
                    loadCitasFromDB();
                } else {
                    JOptionPane.showMessageDialog(viewCitas, "Error al agregar la cita.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(viewCitas, "Formato de fecha u hora inválido. Use 'yyyy-MM-dd' y 'HH:mm'.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(viewCitas, "Error en base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadClientesIntoComboBox() {
        List<String> clientes = new ArrayList<>();
        clienteNameToIdMap.clear();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT id, nombre FROM usuarios ORDER BY nombre";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String nombre = rs.getString("nombre");
                int id = rs.getInt("id");
                clientes.add(nombre);
                clienteNameToIdMap.put(nombre, id);
            }
            viewCitas.setClientes(clientes);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(viewCitas, "Error al cargar clientes: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadMascotasByClientIntoComboBox(int clienteId) {
        List<String> mascotas = new ArrayList<>();
        mascotaNameToIdMap.clear();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT id, nombre FROM mascotas WHERE id_usuario = ? ORDER BY nombre";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, clienteId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String nombre = rs.getString("nombre");
                int id = rs.getInt("id");
                mascotas.add(nombre);
                mascotaNameToIdMap.put(nombre, id);
            }
            viewCitas.setMascotas(mascotas);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(viewCitas, "Error al cargar mascotas: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadCitasFromDB() {
        List<Cita> citas = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT c.id, c.fecha_hora, c.motivo, c.id_mascota, m.nombre AS nombre_mascota, m.id_usuario, u.nombre AS nombre_cliente " +
                    "FROM citas c JOIN mascotas m ON c.id_mascota = m.id JOIN usuarios u ON m.id_usuario = u.id";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Cita cita = new Cita();
                cita.setId(rs.getInt("id"));
                cita.setClienteId(rs.getInt("id_usuario"));
                cita.setClienteNombre(rs.getString("nombre_cliente"));
                cita.setMascotaId(rs.getInt("id_mascota"));
                cita.setMascotaNombre(rs.getString("nombre_mascota"));
                cita.setMotivo(rs.getString("motivo"));

                LocalDateTime fechaHora = rs.getTimestamp("fecha_hora").toLocalDateTime();
                cita.setFecha(fechaHora.toLocalDate());
                cita.setHora(fechaHora.toLocalTime());

                citas.add(cita);
            }

            viewCitas.setCitas(citas);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(viewCitas, "Error al cargar las citas: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}