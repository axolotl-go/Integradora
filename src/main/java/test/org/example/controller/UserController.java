package test.org.example.controller;

import test.org.example.model.User;
import test.org.example.view.ViewUsers;
import test.org.example.db.DBConnection;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserController {

    private ViewUsers viewUsers;

    public UserController(ViewUsers viewUsers) {
        this.viewUsers = viewUsers;

        // Load users from DB when controller is created
        loadUsersFromDB();

        // Add action listeners
        viewUsers.getBtnAdd().addActionListener(e -> addUser());
        viewUsers.getBtnLoad().addActionListener(e -> loadUsersFromDB());
    }

    private void addUser() {
        String nombre = viewUsers.getName();
        String telefono = viewUsers.getPhone();
        String correo = viewUsers.getEmail();

        if (nombre.isEmpty() || telefono.isEmpty() || correo.isEmpty()) {
            JOptionPane.showMessageDialog(viewUsers, "Please fill all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO usuarios (nombre, telefono, correo) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setString(2, telefono);
            ps.setString(3, correo);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(viewUsers, "User added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                loadUsersFromDB();
            } else {
                JOptionPane.showMessageDialog(viewUsers, "Error adding user.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(viewUsers, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadUsersFromDB() {
        List<User> users = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT id, nombre, telefono, correo FROM usuarios";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setNombre(rs.getString("nombre"));
                user.setTelefono(rs.getString("telefono"));
                user.setCorreo(rs.getString("correo"));
                users.add(user);
            }

            viewUsers.setUsers(users);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(viewUsers, "Error loading users: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        viewUsers.getTxtName().setText("");
        viewUsers.getTxtPhone().setText("");
        viewUsers.getTxtEmail().setText("");
    }
}
