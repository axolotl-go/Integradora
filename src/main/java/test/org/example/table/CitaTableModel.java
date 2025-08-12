package test.org.example.table;

import test.org.example.model.Cita;
import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.ArrayList;

public class CitaTableModel extends AbstractTableModel {
    private final String[] columnNames = {"ID", "Cliente", "Mascota", "Fecha", "Hora", "Motivo"};
    private List<Cita> citas;

    public CitaTableModel(List<Cita> citas) {
        this.citas = citas;
    }

    @Override
    public int getRowCount() {
        return citas.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Cita c = citas.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> c.getId();
            case 1 -> c.getClienteNombre();
            case 2 -> c.getMascotaNombre();
            case 3 -> c.getFecha();
            case 4 -> c.getHora();
            case 5 -> c.getMotivo();
            default -> null;
        };
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public void setCitas(List<Cita> citas) {
        this.citas = citas;
        fireTableDataChanged();
    }
}