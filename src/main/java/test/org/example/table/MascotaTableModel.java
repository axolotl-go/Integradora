package test.org.example.table;

import test.org.example.model.Pet;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class MascotaTableModel extends AbstractTableModel {
    private final String[] columnNames = {"ID", "Nombre", "Especie", "Raza", "Edad", "Dueño"};
    private List<Pet> mascotas;

    public MascotaTableModel(List<Pet> mascotas) {
        this.mascotas = mascotas;
    }

    @Override
    public int getRowCount() {
        return mascotas.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Pet m = mascotas.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> m.getId();
            case 1 -> m.getNombre();
            case 2 -> m.getEspecie();
            case 3 -> m.getRaza();
            case 4 -> m.getEdad();
            case 5 -> m.getNombreUsuario(); // <-- Usar el nuevo campo
            default -> null;
        };
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public void setMascotas(List<Pet> mascotas) {
        this.mascotas = mascotas;
        fireTableDataChanged();
    }
}