package test.org.example.controller;

import org.axolotl.db.DBConnection;
import test.org.example.model.Inventory;
import test.org.example.view.ViewInventory;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InventoryController {
    private ViewInventory viewInventory;

    public InventoryController(ViewInventory viewInventory) {
        this.viewInventory = viewInventory;

        // ActionListeners para los botones
        this.viewInventory.getBtnAdd().addActionListener(e -> addProducto());
        this.viewInventory.getBtnLoad().addActionListener(e -> loadInventarioFromDB());

        // Carga inicial de datos
        loadInventarioFromDB();
    }

    private void addProducto() {
        String nombreProducto = viewInventory.getNombreProducto();
        String cantidadStr = viewInventory.getCantidad();
        String precioVentaStr = viewInventory.getPrecioVenta();

        if (nombreProducto.isEmpty() || cantidadStr.isEmpty() || precioVentaStr.isEmpty()) {
            JOptionPane.showMessageDialog(viewInventory, "Por favor, llene todos los campos.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int cantidad = Integer.parseInt(cantidadStr);
            if (cantidad < 0) {
                JOptionPane.showMessageDialog(viewInventory, "La cantidad no puede ser negativa.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            double precioVenta = Double.parseDouble(precioVentaStr);
            if (precioVenta < 0) {
                JOptionPane.showMessageDialog(viewInventory, "El precio de venta no puede ser negativo.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection conn = DBConnection.getConnection()) {
                String sql = "INSERT INTO inventario (nombre_producto, cantidad, precio_venta) VALUES (?, ?, ?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, nombreProducto);
                ps.setInt(2, cantidad);
                ps.setDouble(3, precioVenta);

                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(viewInventory, "Producto agregado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    viewInventory.clearFields();
                    loadInventarioFromDB();
                } else {
                    JOptionPane.showMessageDialog(viewInventory, "Error al agregar el producto.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(viewInventory, "Cantidad y Precio de Venta deben ser números válidos.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(viewInventory, "Error en base de datos: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadInventarioFromDB() {
        List<Inventory> inventario = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT id, nombre_producto, cantidad, precio_venta FROM inventario";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Inventory producto = new Inventory();
                producto.setId(rs.getInt("id"));
                producto.setNombre_producto(rs.getString("nombre_producto"));
                producto.setCantidad(rs.getInt("cantidad"));
                producto.setPrecio_venta(rs.getDouble("precio_venta"));
                inventario.add(producto);
            }

            viewInventory.setInventario(inventario);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(viewInventory, "Error al cargar el inventario: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}