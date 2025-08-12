package org.axolotl.controller;

import org.axolotl.db.DBConnection;
import org.axolotl.model.Mascota;
import org.axolotl.model.User;
import org.axolotl.view.ViewPets;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ControllerPets {

    private final ViewPets view;

    public ControllerPets(ViewPets view) {
        this.view = view;
        initController();
    }

    private void initController() {
        cargarUsuarios();
        cargarMascotas();

        view.getBtnAgregar().addActionListener(e -> agregarMascota());
        view.getBtnCargar().addActionListener(e -> cargarMascotas());
    }

    private void cargarUsuarios() {
        List<User> usuarios = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, nombre, telefono, correo FROM usuarios")) {

            while (rs.next()) {
                usuarios.add(new User(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("telefono"),
                        rs.getString("correo")
                ));
            }

            view.setUsuarios(usuarios);

            // Debug: verificar que se cargaron usuarios
            System.out.println("Usuarios cargados: " + usuarios.size());

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading users: " + ex.getMessage());
        }
    }

    private void cargarMascotas() {
        List<Mascota> mascotas = new ArrayList<>();
        String query = """
            SELECT m.id, m.nombre, m.especie, m.raza, m.edad,
                   u.id AS clienteId, u.nombre AS clienteNombre
            FROM mascotas m
            JOIN usuarios u ON m.id_usuario = u.id
        """;

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                mascotas.add(new Mascota(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("especie"),
                        rs.getString("raza"),
                        rs.getInt("edad"),
                        rs.getInt("clienteId"),
                        rs.getString("clienteNombre")
                ));
            }

            view.setMascotas(mascotas);

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading mascotas: " + ex.getMessage());
        }
    }

    private void agregarMascota() {
        String nombre = view.getNombre();
        String especie = view.getEspecie();
        String raza = view.getRaza();
        int edad = view.getEdad();
        User user = view.getUsuarioSeleccionado();

        if (nombre.isEmpty() || especie.isEmpty() || raza.isEmpty() || edad <= 0 || user == null) {
            JOptionPane.showMessageDialog(null, "Please fill all fields and select a user.");
            return;
        }

        String insertQuery = "INSERT INTO mascotas (nombre, especie, raza, edad, id_usuario) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(insertQuery)) {

            ps.setString(1, nombre);
            ps.setString(2, especie);
            ps.setString(3, raza);
            ps.setInt(4, edad);
            ps.setInt(5, user.getId());

            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(null, "Mascota added successfully!");
                // Limpiar campos después del éxito
                view.limpiarCampos();
                // Recargar la tabla
                cargarMascotas();
            } else {
                JOptionPane.showMessageDialog(null, "Failed to add mascota.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage());
        }
    }
}