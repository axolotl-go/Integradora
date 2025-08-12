package test.org.example.view;

import test.org.example.db.DBConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ViewForm extends JPanel {

    private JTextField txtUser;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnExit;

    // 1. Modifica la interfaz para que el rol sea un parámetro
    public interface LoginListener {
        void onLoginSuccess(String username, String rol);
    }

    private LoginListener loginListener;

    public void setLoginListener(LoginListener listener) {
        this.loginListener = listener;
    }

    public ViewForm() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel panelCampos = new JPanel(new GridBagLayout());
        panelCampos.setBorder(BorderFactory.createTitledBorder("Iniciar Sesión"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtUser = new JTextField(15);
        txtPassword = new JPasswordField(15);

        int fila = 0;
        addCampo(panelCampos, gbc, fila++, "Usuario:", txtUser);
        addCampo(panelCampos, gbc, fila++, "Contraseña:", txtPassword);

        add(panelCampos, BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        btnLogin = new JButton("Ingresar");
        btnExit = new JButton("Salir");

        btnLogin.addActionListener(this::loginAction);
        btnExit.addActionListener(e -> System.exit(0));

        panelBotones.add(btnLogin);
        panelBotones.add(btnExit);

        add(panelBotones, BorderLayout.SOUTH);
    }

    private void addCampo(JPanel panel, GridBagConstraints gbc, int fila, String etiqueta, JComponent campo) {
        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(new JLabel(etiqueta), gbc);

        gbc.gridx = 1;
        panel.add(campo, gbc);
    }

    private void loginAction(ActionEvent e) {
        String username = txtUser.getText();
        String password = new String(txtPassword.getPassword());

        try (Connection conn = DBConnection.getConnection()) {
            // 2. Modifica la consulta SQL para obtener el rol del usuario
            String sql = "SELECT rol FROM superUsers WHERE username=? AND password=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String rol = rs.getString("rol"); // 3. Obtén el rol del resultado
                JOptionPane.showMessageDialog(this, "Login exitoso. ¡Bienvenido " + username + "!");

                if (loginListener != null) {
                    loginListener.onLoginSuccess(username, rol); // 4. Pasa el rol al listener
                }
            } else {
                JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al conectar: " + ex.getMessage());
        }
    }
}