package test.org.example.controller;

import org.axolotl.db.DBConnection;
import test.org.example.model.Inventory;
import test.org.example.model.VentaItem;
import test.org.example.view.ViewPOS;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class POSController {
    private ViewPOS viewPOS;
    private List<VentaItem> cartItems;
    private Map<String, Inventory> inventarioMap; // Mapa para buscar productos por nombre

    public POSController(ViewPOS viewPOS) {
        this.viewPOS = viewPOS;
        this.cartItems = new ArrayList<>();
        this.inventarioMap = new HashMap<>();

        // ActionListeners para los botones
        this.viewPOS.getBtnAgregar().addActionListener(e -> addProductToCart());
        this.viewPOS.getBtnFinalizar().addActionListener(e -> finalizeSale());
        this.viewPOS.getBtnCancelar().addActionListener(e -> cancelSale());

        // Carga inicial
        loadProductosForPOS();
    }

    private void loadProductosForPOS() {
        List<String> nombresProductos = new ArrayList<>();
        inventarioMap.clear();

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT id, nombre_producto, cantidad, precio_venta FROM inventario WHERE cantidad > 0 ORDER BY nombre_producto";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Inventory producto = new Inventory();
                producto.setId(rs.getInt("id"));
                producto.setNombre_producto(rs.getString("nombre_producto"));
                producto.setCantidad(rs.getInt("cantidad"));
                producto.setPrecio_venta(rs.getDouble("precio_venta"));

                nombresProductos.add(producto.getNombre_producto());
                inventarioMap.put(producto.getNombre_producto(), producto);
            }
            viewPOS.setProductos(nombresProductos);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(viewPOS, "Error al cargar productos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addProductToCart() {
        String nombreProducto = (String) viewPOS.getCmbProductos().getSelectedItem();
        String cantidadStr = viewPOS.getTxtCantidad().getText().trim();

        if (nombreProducto == null || nombreProducto.isEmpty() || cantidadStr.isEmpty()) {
            JOptionPane.showMessageDialog(viewPOS, "Por favor, seleccione un producto y especifique la cantidad.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int cantidad = Integer.parseInt(cantidadStr);
            if (cantidad <= 0) {
                JOptionPane.showMessageDialog(viewPOS, "La cantidad debe ser un número positivo.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Inventory productoEnInventario = inventarioMap.get(nombreProducto);
            if (productoEnInventario != null) {
                if (cantidad > productoEnInventario.getCantidad()) {
                    JOptionPane.showMessageDialog(viewPOS, "Cantidad insuficiente en el inventario. Stock disponible: " + productoEnInventario.getCantidad(), "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                VentaItem item = new VentaItem(productoEnInventario.getId(), nombreProducto, cantidad, productoEnInventario.getPrecio_venta());
                cartItems.add(item);
                viewPOS.setCartItems(cartItems);
                updateTotal();
                viewPOS.clearInputFields();
            } else {
                JOptionPane.showMessageDialog(viewPOS, "Producto no encontrado en el inventario.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(viewPOS, "La cantidad debe ser un número entero válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void finalizeSale() {
        if (cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(viewPOS, "El carrito está vacío.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Connection conn = null; // Declarar la conexión fuera del try
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Iniciar una transacción

            String sqlUpdateInventario = "UPDATE inventario SET cantidad = cantidad - ? WHERE id = ?";

            try (PreparedStatement psUpdate = conn.prepareStatement(sqlUpdateInventario)) {
                for (VentaItem item : cartItems) {
                    psUpdate.setInt(1, item.getCantidad());
                    psUpdate.setInt(2, item.getIdProducto());
                    psUpdate.addBatch();
                }
                psUpdate.executeBatch();
            }

            conn.commit(); // Confirmar la transacción

            JOptionPane.showMessageDialog(viewPOS, "Venta finalizada correctamente. Inventario actualizado.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            // Limpiar el carrito y refrescar la vista
            cartItems.clear();
            viewPOS.setCartItems(cartItems);
            updateTotal();
            loadProductosForPOS(); // Recargar el JComboBox

        } catch (SQLException ex) {
            try {
                if (conn != null) { // Asegurarse de que la conexión no sea nula antes de intentar el rollback
                    conn.rollback(); // Deshacer la transacción en caso de error
                }
            } catch (SQLException rollbackEx) {
                // Manejar el error del rollback si es necesario
                rollbackEx.printStackTrace();
            }
            JOptionPane.showMessageDialog(viewPOS, "Error al finalizar la venta: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (conn != null) {
                    conn.close(); // Asegurarse de cerrar la conexión
                }
            } catch (SQLException closeEx) {
                closeEx.printStackTrace();
            }
        }
    }

    private void cancelSale() {
        if (JOptionPane.showConfirmDialog(viewPOS, "¿Está seguro que desea cancelar la venta?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            cartItems.clear();
            viewPOS.setCartItems(cartItems);
            updateTotal();
            viewPOS.clearInputFields();
        }
    }

    private void updateTotal() {
        double total = cartItems.stream().mapToDouble(VentaItem::getSubtotal).sum();
        viewPOS.updateTotal(total);
    }
}