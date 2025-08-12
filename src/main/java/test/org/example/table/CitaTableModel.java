package test.org.example.table;

import test.org.example.model.Cita;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class CitaTableModel extends AbstractTableModel {
    private final String[] columnNames = {"ID", "Fecha", "Hora", "Cliente", "Mascota", "Motivo"};
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
        Cita cita = citas.get(rowIndex);
        switch (columnIndex) {
            case 0: return cita.getId();
            case 1: return cita.getFecha();
            case 2: return cita.getHora();
            case 3: return cita.getClienteNombre();
            case 4: return cita.getMascotaNombre();
            case 5: return cita.getMotivo();
            default: return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public void setCitas(List<Cita> citas) {
        this.citas = citas;
        fireTableDataChanged();
    }

    public Cita getCitaAt(int row) {
        return citas.get(row);
    }
}
