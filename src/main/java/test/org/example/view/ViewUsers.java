package test.org.example.view;

import test.org.example.controller.UserController;
import test.org.example.model.User;
import test.org.example.table.UserTableModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class ViewUsers extends JPanel {
    private JTextField txtNombre, txtTelefono, txtCorreo;
    private JButton btnAgregar, btnCargar;
    private JTable tblUsuarios;
    private UserTableModel tableModel;

    private UserController controller; // Add this field

    public ViewUsers() {
        setLayout(new BorderLayout(10,10));
        setBorder(new EmptyBorder(10,10,10,10));

        // Formulario
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del Usuario"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNombre = new JTextField(15);
        txtTelefono = new JTextField(15);
        txtCorreo = new JTextField(15);

        int row = 0;
        addField(panelForm, gbc, row++, "Nombre:", txtNombre);
        addField(panelForm, gbc, row++, "Teléfono:", txtTelefono);
        addField(panelForm, gbc, row++, "Correo:", txtCorreo);

        add(panelForm, BorderLayout.WEST);

        // Botones
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        btnAgregar = new JButton("Agregar Usuario");
        btnCargar = new JButton("Cargar Usuarios");

        panelButtons.add(btnAgregar);
        panelButtons.add(btnCargar);

        add(panelButtons, BorderLayout.SOUTH);

        // Tabla
        tableModel = new UserTableModel(new ArrayList<>());
        tblUsuarios = new JTable(tableModel);
        tblUsuarios.setRowHeight(25);
        tblUsuarios.setFillsViewportHeight(true);
        tblUsuarios.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scroll = new JScrollPane(tblUsuarios);
        scroll.setBorder(BorderFactory.createTitledBorder("Usuarios Registrados"));
        add(scroll, BorderLayout.CENTER);

        // Instantiate controller and link with this view
        controller = new UserController(this);
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int row, String label, JTextField field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    // Getters for controller to access fields and buttons
    public String getName() { return txtNombre.getText().trim(); }
    public String getPhone() { return txtTelefono.getText().trim(); }
    public String getEmail() { return txtCorreo.getText().trim(); }

    public JTextField getTxtName() { return txtNombre; }
    public JTextField getTxtPhone() { return txtTelefono; }
    public JTextField getTxtEmail() { return txtCorreo; }

    public JTable getTblUsuarios() { return tblUsuarios; }

    public JButton getBtnAdd() { return btnAgregar; }
    public JButton getBtnLoad() { return btnCargar; }

    // Method to update the table model with new user list
    public void setUsers(java.util.List<User> usuarios) {
        tableModel.setUsers(usuarios);
    }
}
