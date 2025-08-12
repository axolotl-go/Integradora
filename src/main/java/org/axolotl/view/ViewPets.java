package org.axolotl.view;

import org.axolotl.model.Mascota;
import org.axolotl.model.User;
import org.axolotl.table.MascotaTableModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ViewPets extends JPanel {
    private JTextField txtNombre, txtEspecie, txtRaza, txtEdad;
    private JComboBox<User> comboUsuarios;
    private JButton btnAgregar, btnCargar;
    private JTable tblMascotas;
    private MascotaTableModel tableModel;

    public ViewPets() {
        setLayout(new BorderLayout(10,10));
        setBorder(new EmptyBorder(10,10,10,10));

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos de la Mascota"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNombre = new JTextField(15);
        txtEspecie = new JTextField(15);
        txtRaza = new JTextField(15);
        txtEdad = new JTextField(15);
        comboUsuarios = new JComboBox<>();
        comboUsuarios.setPreferredSize(new Dimension(150, 25));

        int row = 0;
        addField(panelForm, gbc, row++, "Nombre:", txtNombre);
        addField(panelForm, gbc, row++, "Especie:", txtEspecie);
        addField(panelForm, gbc, row++, "Raza:", txtRaza);
        addField(panelForm, gbc, row++, "Edad:", txtEdad);
        addField(panelForm, gbc, row++, "Dueño (Usuario):", comboUsuarios);

        add(panelForm, BorderLayout.WEST);

        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        btnAgregar = new JButton("Agregar Mascota");
        btnCargar = new JButton("Cargar Mascotas");

        panelButtons.add(btnAgregar);
        panelButtons.add(btnCargar);

        add(panelButtons, BorderLayout.SOUTH);

        tableModel = new MascotaTableModel(new ArrayList<>());
        tblMascotas = new JTable(tableModel);
        tblMascotas.setRowHeight(25);
        tblMascotas.setFillsViewportHeight(true);
        tblMascotas.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scroll = new JScrollPane(tblMascotas);
        scroll.setBorder(BorderFactory.createTitledBorder("Mascotas Registradas"));
        add(scroll, BorderLayout.CENTER);
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int row, String label, JTextField field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    public void limpiarCampos() {
        txtNombre.setText("");
        txtEspecie.setText("");
        txtRaza.setText("");
        txtEdad.setText("");
        comboUsuarios.setSelectedIndex(-1); // Deseleccionar
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int row, String label, JComboBox<?> comboBox) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(comboBox, gbc);
    }

    // Getters
    public String getNombre() {
        return txtNombre.getText().trim();
    }

    public String getEspecie() {
        return txtEspecie.getText().trim();
    }

    public String getRaza() {
        return txtRaza.getText().trim();
    }

    public int getEdad() {
        try {
            return Integer.parseInt(txtEdad.getText().trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public User getUsuarioSeleccionado() {
        return (User) comboUsuarios.getSelectedItem();
    }

    public JTable getTblMascotas() {
        return tblMascotas;
    }

    public JButton getBtnAgregar() {
        return btnAgregar;
    }

    public JButton getBtnCargar() {
        return btnCargar;
    }

    public void setMascotas(List<Mascota> mascotas) {
        tableModel.setMascotas(mascotas);
    }

    public void setUsuarios(List<User> usuarios) {
        comboUsuarios.removeAllItems();
        for (User u : usuarios) {
            comboUsuarios.addItem(u);
        }
    }
}
