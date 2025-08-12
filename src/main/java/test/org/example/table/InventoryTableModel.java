package test.org.example.table;

import test.org.example.model.Inventory;
import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.ArrayList;

public class InventoryTableModel extends AbstractTableModel {
    private final String[] columnNames = {"ID", "Producto", "Cantidad", "Precio de Venta"};
    private List<Inventory> inventario;

    public InventoryTableModel(List<Inventory> inventario) {
        this.inventario = inventario;
    }

    @Override
    public int getRowCount() {
        return inventario.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Inventory i = inventario.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> i.getId();
            case 1 -> i.getNombre_producto();
            case 2 -> i.getCantidad();
            case 3 -> String.format("$%.2f", i.getPrecio_venta()); // Formato de moneda
            default -> null;
        };
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public void setInventario(List<Inventory> inventario) {
        this.inventario = inventario;
        fireTableDataChanged();
    }
}