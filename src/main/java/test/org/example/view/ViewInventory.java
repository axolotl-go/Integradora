package test.org.example.view;

import test.org.example.controller.InventoryController;
import test.org.example.model.Inventory;
import test.org.example.table.InventoryTableModel;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ViewInventory extends JPanel {
    private JTextField txtNombreProducto, txtCantidad, txtPrecioVenta;
    private JButton btnAgregar, btnCargar;
    private JTable tblInventario;
    private InventoryTableModel tableModel;

    private InventoryController controller;

    public ViewInventory() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Formulario
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Datos del Producto"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtNombreProducto = new JTextField(15);
        txtCantidad = new JTextField(15);
        txtPrecioVenta = new JTextField(15);

        int row = 0;
        addField(panelForm, gbc, row++, "Nombre del Producto:", txtNombreProducto);
        addField(panelForm, gbc, row++, "Cantidad:", txtCantidad);
        addField(panelForm, gbc, row++, "Precio de Venta:", txtPrecioVenta);

        add(panelForm, BorderLayout.WEST);

        // Botones
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        btnAgregar = new JButton("Agregar Producto");
        btnCargar = new JButton("Cargar Inventario");

        panelButtons.add(btnAgregar);
        panelButtons.add(btnCargar);

        add(panelButtons, BorderLayout.SOUTH);

        // Tabla
        tableModel = new InventoryTableModel(new ArrayList<>());
        tblInventario = new JTable(tableModel);
        tblInventario.setRowHeight(25);
        tblInventario.setFillsViewportHeight(true);
        tblInventario.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scroll = new JScrollPane(tblInventario);
        scroll.setBorder(BorderFactory.createTitledBorder("Inventario Registrado"));
        add(scroll, BorderLayout.CENTER);

        // Instantiate controller and link with this view
        controller = new InventoryController(this);
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int row, String label, JTextField field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    // Getters for controller to access fields and buttons
    public String getNombreProducto() { return txtNombreProducto.getText().trim(); }
    public String getCantidad() { return txtCantidad.getText().trim(); }
    public String getPrecioVenta() { return txtPrecioVenta.getText().trim(); }
    public JButton getBtnAdd() { return btnAgregar; }
    public JButton getBtnLoad() { return btnCargar; }

    public void setInventario(List<Inventory> inventario) {
        tableModel.setInventario(inventario);
    }

    public void clearFields() {
        txtNombreProducto.setText("");
        txtCantidad.setText("");
        txtPrecioVenta.setText("");
    }
}