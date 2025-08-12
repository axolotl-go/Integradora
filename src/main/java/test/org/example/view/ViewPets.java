package test.org.example.view;

import test.org.example.controller.PetsController;
import test.org.example.model.Pet;
import test.org.example.table.PetTableModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List; // Importar java.util.List para el método setOwners

public class ViewPets extends JPanel {
    private JTextField txtNombre, txtEspecie, txtRaza, txtEdad;
    private JComboBox<String> cmbDueños;
    private JButton btnAgregar, btnCargar;
    private JTable tblMascotas;
    private PetTableModel tableModel;

    private PetsController controller;

    public ViewPets() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Formulario
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos de la Mascota"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNombre = new JTextField(15);
        txtEspecie = new JTextField(15);
        txtRaza = new JTextField(15);
        txtEdad = new JTextField(15);
        cmbDueños = new JComboBox<>();

        int row = 0;
        addField(panelForm, gbc, row++, "Nombre:", txtNombre);
        addField(panelForm, gbc, row++, "Especie:", txtEspecie);
        addField(panelForm, gbc, row++, "Raza:", txtRaza);
        addField(panelForm, gbc, row++, "Edad:", txtEdad);

        gbc.gridx = 0;
        gbc.gridy = row++;
        panelForm.add(new JLabel("Dueño:"), gbc);
        gbc.gridx = 1;
        panelForm.add(cmbDueños, gbc);

        add(panelForm, BorderLayout.WEST);

        // Botones
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        btnAgregar = new JButton("Agregar Mascota");
        btnCargar = new JButton("Cargar Mascotas");

        panelButtons.add(btnAgregar);
        panelButtons.add(btnCargar);

        add(panelButtons, BorderLayout.SOUTH);

        // Tabla
        tableModel = new PetTableModel(new ArrayList<>());
        tblMascotas = new JTable(tableModel);
        tblMascotas.setRowHeight(25);
        tblMascotas.setFillsViewportHeight(true);
        tblMascotas.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scroll = new JScrollPane(tblMascotas);
        scroll.setBorder(BorderFactory.createTitledBorder("Mascotas Registradas"));
        add(scroll, BorderLayout.CENTER);

        // Instantiate controller and link with this view
        controller = new PetsController(this);
    }

    /**
     * Este método recibe una lista de nombres de dueños y los carga en el JComboBox.
     * Es llamado por el PetsController.
     * @param owners Una lista de cadenas con los nombres de los dueños.
     */
    public void setOwners(List<String> owners) {
        cmbDueños.removeAllItems();
        for (String owner : owners) {
            cmbDueños.addItem(owner);
        }
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int row, String label, JTextField field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    // Getters for controller to access fields and buttons
    public String getNombre() { return txtNombre.getText().trim(); }
    public String getEspecie() { return txtEspecie.getText().trim(); }
    public String getRaza() { return txtRaza.getText().trim(); }
    public String getEdad() { return txtEdad.getText().trim(); }

    public JTextField getTxtNombre() { return txtNombre; }
    public JTextField getTxtEspecie() { return txtEspecie; }
    public JTextField getTxtRaza() { return txtRaza; }
    public JTextField getTxtEdad() { return txtEdad; }
    public JComboBox<String> getCmbDueños() { return cmbDueños; }

    public JTable getTblMascotas() { return tblMascotas; }

    public JButton getBtnAdd() { return btnAgregar; }
    public JButton getBtnLoad() { return btnCargar; }

    // Method to update the table model with new pet list
    public void setMascotas(java.util.List<Pet> mascotas) {
        tableModel.setMascotas(mascotas);
    }
}