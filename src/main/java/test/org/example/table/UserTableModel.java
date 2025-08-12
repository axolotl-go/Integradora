package test.org.example.table;

import test.org.example.model.User;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class UserTableModel extends AbstractTableModel {

    private final String[] columnNames = {"ID", "Nombre", "Teléfono", "Correo"};
    private List<User> users;

    public UserTableModel(List<User> users) {
        this.users = users;
    }

    @Override
    public int getRowCount() {
        return users == null ? 0 : users.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        User user = users.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> user.getId();
            case 1 -> user.getNombre();
            case 2 -> user.getTelefono();
            case 3 -> user.getCorreo();
            default -> null;
        };
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public void setUsers(List<User> users) {
        this.users = users;
        fireTableDataChanged();
    }

    public User getUserAt(int row) {
        return users.get(row);
    }
}
