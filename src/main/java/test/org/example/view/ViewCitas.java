package test.org.example.view;

import test.org.example.controller.*;
import test.org.example.model.Cita;
import test.org.example.table.CitaTableModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ViewCitas extends JPanel {
    private JComboBox<String> cmbClientes;
    private JComboBox<String> cmbMascotas;
    private JTextField txtFecha, txtHora, txtMotivo;
    private JButton btnAgregar, btnCargar;
    private JTable tblCitas;
    private CitaTableModel tableModel;

    private CitasController controller;

    public ViewCitas() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Formulario
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos de la Cita"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cmbClientes = new JComboBox<>();
        cmbMascotas = new JComboBox<>();
        txtFecha = new JTextField("yyyy-MM-dd", 10);
        txtHora = new JTextField("HH:mm", 8);
        txtMotivo = new JTextField(15);

        int row = 0;
        gbc.gridx = 0;
        gbc.gridy = row++;
        panelForm.add(new JLabel("Cliente:"), gbc);
        gbc.gridx = 1;
        panelForm.add(cmbClientes, gbc);

        gbc.gridx = 0;
        gbc.gridy = row++;
        panelForm.add(new JLabel("Mascota:"), gbc);
        gbc.gridx = 1;
        panelForm.add(cmbMascotas, gbc);

        gbc.gridx = 0;
        gbc.gridy = row++;
        panelForm.add(new JLabel("Fecha (yyyy-MM-dd):"), gbc);
        gbc.gridx = 1;
        panelForm.add(txtFecha, gbc);

        gbc.gridx = 0;
        gbc.gridy = row++;
        panelForm.add(new JLabel("Hora (HH:mm):"), gbc);
        gbc.gridx = 1;
        panelForm.add(txtHora, gbc);

        addField(panelForm, gbc, row++, "Motivo:", txtMotivo);

        add(panelForm, BorderLayout.WEST);

        // Botones
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        btnAgregar = new JButton("Agregar Cita");
        btnCargar = new JButton("Cargar Citas");

        panelButtons.add(btnAgregar);
        panelButtons.add(btnCargar);

        add(panelButtons, BorderLayout.SOUTH);

        // Tabla
        tableModel = new CitaTableModel(new ArrayList<>());
        tblCitas = new JTable(tableModel);
        tblCitas.setRowHeight(25);
        tblCitas.setFillsViewportHeight(true);
        tblCitas.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scroll = new JScrollPane(tblCitas);
        scroll.setBorder(BorderFactory.createTitledBorder("Citas Registradas"));
        add(scroll, BorderLayout.CENTER);

        // Instantiate controller and link with this view
        controller = new CitasController(this);
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int row, String label, JTextField field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    // Getters for controller to access fields and buttons
    public String getFecha() { return txtFecha.getText().trim(); }
    public String getHora() { return txtHora.getText().trim(); }
    public String getMotivo() { return txtMotivo.getText().trim(); }

    public JComboBox<String> getCmbClientes() { return cmbClientes; }
    public JComboBox<String> getCmbMascotas() { return cmbMascotas; }
    public JButton getBtnAdd() { return btnAgregar; }
    public JButton getBtnLoad() { return btnCargar; }

    public void setCitas(List<Cita> citas) {
        tableModel.setCitas(citas);
    }

    public void setClientes(List<String> clientes) {
        cmbClientes.removeAllItems();
        for (String cliente : clientes) {
            cmbClientes.addItem(cliente);
        }
    }

    public void setMascotas(List<String> mascotas) {
        cmbMascotas.removeAllItems();
        for (String mascota : mascotas) {
            cmbMascotas.addItem(mascota);
        }
    }

    public void clearFields() {
        txtFecha.setText("yyyy-MM-dd");
        txtHora.setText("HH:mm");
        txtMotivo.setText("");
        if (cmbClientes.getItemCount() > 0) {
            cmbClientes.setSelectedIndex(0);
        }
        cmbMascotas.removeAllItems();
    }
}