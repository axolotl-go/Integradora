package org.axolotl.view;

import org.axolotl.model.Cita;
import org.axolotl.table.CitaTableModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ViewAppointments extends JPanel {
    private JTextField txtIdMascota, txtFechaHora, txtMotivo, txtEstado;
    private JButton btnAgregar, btnCargar;
    private JTable tblCitas;
    private CitaTableModel tableModel;

    public ViewAppointments() {
        setLayout(new BorderLayout(10,10));
        setBorder(new EmptyBorder(10,10,10,10));

        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos de la Cita"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtIdMascota = new JTextField(15);
        txtFechaHora = new JTextField(15);
        txtMotivo = new JTextField(15);
        txtEstado = new JTextField(15);

        int row = 0;
        addField(panelForm, gbc, row++, "ID Mascota:", txtIdMascota);
        addField(panelForm, gbc, row++, "Fecha y Hora (yyyy-MM-dd HH:mm):", txtFechaHora);
        addField(panelForm, gbc, row++, "Motivo:", txtMotivo);
        addField(panelForm, gbc, row++, "Estado:", txtEstado);

        add(panelForm, BorderLayout.WEST);

        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        btnAgregar = new JButton("Agregar Cita");
        btnCargar = new JButton("Cargar Citas");

        panelButtons.add(btnAgregar);
        panelButtons.add(btnCargar);

        add(panelButtons, BorderLayout.SOUTH);

        tableModel = new CitaTableModel(new ArrayList<>());
        tblCitas = new JTable(tableModel);
        tblCitas.setRowHeight(25);
        tblCitas.setFillsViewportHeight(true);
        tblCitas.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scroll = new JScrollPane(tblCitas);
        scroll.setBorder(BorderFactory.createTitledBorder("Citas Registradas"));
        add(scroll, BorderLayout.CENTER);
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int row, String label, JTextField field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    // Getters
    public int getIdMascota() {
        try {
            return Integer.parseInt(txtIdMascota.getText().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    public LocalDateTime getFechaHora() {
        try {
            return LocalDateTime.parse(txtFechaHora.getText().trim(),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        } catch (Exception e) {
            return null;
        }
    }
    public String getMotivo() { return txtMotivo.getText().trim(); }
    public String getEstado() { return txtEstado.getText().trim(); }

    public JTable getTblCitas() { return tblCitas; }
    public JButton getBtnAgregar() { return btnAgregar; }
    public JButton getBtnCargar() { return btnCargar; }

    public void setCitas(java.util.List<Cita> citas) {
        tableModel.setCitas(citas);
    }
}
