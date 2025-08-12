package test.org.example.table;

import test.org.example.model.VentaItem;
import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.ArrayList;

public class VentaItemTableModel extends AbstractTableModel {
    private final String[] columnNames = {"Producto", "Cantidad", "Precio Unitario", "Subtotal"};
    private List<VentaItem> ventaItems;

    public VentaItemTableModel(List<VentaItem> ventaItems) {
        this.ventaItems = ventaItems;
    }

    @Override
    public int getRowCount() {
        return ventaItems.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        VentaItem item = ventaItems.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> item.getNombreProducto();
            case 1 -> item.getCantidad();
            case 2 -> String.format("$%.2f", item.getPrecioUnitario());
            case 3 -> String.format("$%.2f", item.getSubtotal());
            default -> null;
        };
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public void setVentaItems(List<VentaItem> ventaItems) {
        this.ventaItems = ventaItems;
        fireTableDataChanged();
    }
}