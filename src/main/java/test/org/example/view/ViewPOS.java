package test.org.example.view;

import test.org.example.controller.POSController;
import test.org.example.model.VentaItem;
import test.org.example.table.VentaItemTableModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ViewPOS extends JPanel {
    private JComboBox<String> cmbProductos;
    private JTextField txtCantidad;
    private JButton btnAgregar, btnFinalizar, btnCancelar;
    private JTable tblCarrito;
    private VentaItemTableModel tableModel;
    private JLabel lblTotal;

    private POSController controller;

    public ViewPOS() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel de Control (Selección de productos)
        JPanel panelControl = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelControl.setBorder(BorderFactory.createTitledBorder("Agregar Producto"));

        cmbProductos = new JComboBox<>();
        txtCantidad = new JTextField(5);
        btnAgregar = new JButton("Agregar al Carrito");

        panelControl.add(new JLabel("Producto:"));
        panelControl.add(cmbProductos);
        panelControl.add(new JLabel("Cantidad:"));
        panelControl.add(txtCantidad);
        panelControl.add(btnAgregar);

        add(panelControl, BorderLayout.NORTH);

        // Tabla del Carrito
        tableModel = new VentaItemTableModel(new ArrayList<>());
        tblCarrito = new JTable(tableModel);
        tblCarrito.setRowHeight(25);
        tblCarrito.setFillsViewportHeight(true);
        tblCarrito.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scroll = new JScrollPane(tblCarrito);
        scroll.setBorder(BorderFactory.createTitledBorder("Carrito de Compras"));
        add(scroll, BorderLayout.CENTER);

        // Panel de Resumen y Botones
        JPanel panelBottom = new JPanel(new BorderLayout(10, 10));

        // Total
        JPanel panelTotal = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        lblTotal = new JLabel("Total: $0.00");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 24));
        panelTotal.add(lblTotal);
        panelBottom.add(panelTotal, BorderLayout.NORTH);

        // Botones de acción
        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        btnFinalizar = new JButton("Finalizar Venta");
        btnFinalizar.setBackground(new Color(0, 150, 0));
        btnFinalizar.setForeground(Color.WHITE);
        btnCancelar = new JButton("Cancelar Venta");
        btnCancelar.setBackground(new Color(200, 0, 0));
        btnCancelar.setForeground(Color.WHITE);

        panelButtons.add(btnFinalizar);
        panelButtons.add(btnCancelar);
        panelBottom.add(panelButtons, BorderLayout.CENTER);

        add(panelBottom, BorderLayout.SOUTH);

        // Instantiate controller and link with this view
        controller = new POSController(this);
    }

    // Getters for controller to access components
    public JComboBox<String> getCmbProductos() { return cmbProductos; }
    public JTextField getTxtCantidad() { return txtCantidad; }
    public JButton getBtnAgregar() { return btnAgregar; }
    public JButton getBtnFinalizar() { return btnFinalizar; }
    public JButton getBtnCancelar() { return btnCancelar; }

    public void setProductos(List<String> productos) {
        cmbProductos.removeAllItems();
        for (String producto : productos) {
            cmbProductos.addItem(producto);
        }
    }

    public void setCartItems(List<VentaItem> items) {
        tableModel.setVentaItems(items);
    }

    public void updateTotal(double total) {
        lblTotal.setText(String.format("Total: $%.2f", total));
    }

    public void clearInputFields() {
        if (cmbProductos.getItemCount() > 0) {
            cmbProductos.setSelectedIndex(0);
        }
        txtCantidad.setText("");
    }
}