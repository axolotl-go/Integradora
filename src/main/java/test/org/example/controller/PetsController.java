package test.org.example.controller;

import org.axolotl.db.DBConnection;
import test.org.example.model.Pet;
import test.org.example.view.ViewPets;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PetsController {
    private ViewPets viewPets;

    public PetsController(ViewPets viewPets) {
        this.viewPets = viewPets;

        loadPetsFromDB();

        loadOwnersIntoComboBox();

        viewPets.getBtnAdd().addActionListener(e -> addPet());
        viewPets.getBtnLoad().addActionListener(e -> loadPetsFromDB());
    }

    private void loadOwnersIntoComboBox() {
        List<String> owners = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT nombre FROM usuarios ORDER BY nombre";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        owners.add(rs.getString("nombre"));
                    }
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(viewPets, "Error al cargar los dueños: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        viewPets.setOwners(owners);
    }


    private void addPet() {
        String nombre = viewPets.getNombre();
        String especie = viewPets.getEspecie();
        String raza = viewPets.getRaza();
        String edadStr = viewPets.getEdad();
        String dueño = (String) viewPets.getCmbDueños().getSelectedItem();

        System.out.println("Dueño seleccionado: " + dueño); // <-- Línea para depurar

        if (dueño == null) {
            JOptionPane.showMessageDialog(viewPets, "Por favor selecciona un dueño.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idUsuario = getIdUsuarioByName(dueño);
        System.out.println("ID de usuario obtenido: " + idUsuario); // <-- Línea para depurar


        if (nombre.isEmpty() || especie.isEmpty() || raza.isEmpty() || edadStr.isEmpty() || dueño == null) {
            JOptionPane.showMessageDialog(viewPets, "Por favor llena todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int edad;
        try {
            edad = Integer.parseInt(edadStr);
            if (edad < 0) throw new NumberFormatException("Edad negativa");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(viewPets, "La edad debe ser un número entero positivo.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (idUsuario == -1) {
            JOptionPane.showMessageDialog(viewPets, "Dueño no válido.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO mascotas (nombre, especie, raza, edad, id_usuario) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setString(2, especie);
            ps.setString(3, raza);
            ps.setInt(4, edad);
            ps.setInt(5, idUsuario);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(viewPets, "Mascota agregada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                clearPetFields();
                loadPetsFromDB();
            } else {
                JOptionPane.showMessageDialog(viewPets, "Error al agregar la mascota.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(viewPets, "Error en base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private int getIdUsuarioByName(String nombre) {
        int idUsuario = -1;
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT id FROM usuarios WHERE nombre = ?"; // <-- Cláusula WHERE
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, nombre);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        idUsuario = rs.getInt("id");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(viewPets, "Error al obtener ID de usuario: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return idUsuario;
    }


    private void loadPetsFromDB() {
        List<Pet> mascotas = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT m.id, m.nombre, m.especie, m.raza, m.edad, m.id_usuario, u.nombre AS nombre_usuario " + // <-- AGREGAR m.id_usuario aquí
                    "FROM mascotas m " +
                    "JOIN usuarios u ON m.id_usuario = u.id";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Pet pet = new Pet();
                pet.setId(rs.getInt("id"));
                pet.setNombre(rs.getString("nombre"));
                pet.setEspecie(rs.getString("especie"));
                pet.setRaza(rs.getString("raza"));
                pet.setEdad(rs.getInt("edad"));
                pet.setId_usuario(rs.getInt("id_usuario"));
                pet.setNombreUsuario(rs.getString("nombre_usuario"));

                mascotas.add(pet);
            }

            viewPets.setMascotas(mascotas);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(viewPets, "Error loading Pets: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearPetFields() {
        viewPets.getTxtNombre().setText("");
        viewPets.getTxtEspecie().setText("");
        viewPets.getTxtRaza().setText("");
        viewPets.getTxtEdad().setText("");
        viewPets.getCmbDueños().setSelectedIndex(-1); // Deselecciona el combo, opcionalmente puedes poner 0 si quieres seleccionar el primero
    }


}