package test.org.example;

import test.org.example.view.*;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Veterinaria App");
            frame.setSize(1000, 700); // Se recomienda un tamaño un poco más grande
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);

            JPanel contentPanel = new JPanel(new CardLayout());

            // Crear paneles
            ViewForm loginPanel = new ViewForm();
            ViewUsers clientesPanel = new ViewUsers();
            ViewPets mascotasPanel = new ViewPets();
            ViewCitas citasPanel = new ViewCitas();
            ViewInventory inventoryPanel = new ViewInventory();
            ViewPOS POS = new ViewPOS();

            // Registrar pantallas en el CardLayout
            contentPanel.add(loginPanel, "Login");
            contentPanel.add(clientesPanel, "Clientes");
            contentPanel.add(mascotasPanel, "Mascotas");
            contentPanel.add(citasPanel, "Citas");
            contentPanel.add(inventoryPanel, "Inventario");
            contentPanel.add(POS, "POS");

            frame.getContentPane().add(contentPanel);

            // Listener para login exitoso
            // 1. Modifica el lambda para aceptar el parámetro 'rol'
            loginPanel.setLoginListener((username, rol) -> {
                JMenuBar menuBar = new JMenuBar();
                JMenu menu = new JMenu("Navegación");

                CardLayout cl = (CardLayout) contentPanel.getLayout();

                // 2. Crea las opciones del menú según el rol
                if ("admin".equalsIgnoreCase(rol)) {
                    JMenuItem menuClientes = new JMenuItem("Clientes");
                    JMenuItem menuMascotas = new JMenuItem("Mascotas");
                    JMenuItem menuCitas = new JMenuItem("Citas");
                    JMenuItem menuInvetary = new JMenuItem("Inventario");
                    JMenuItem menuPOS = new JMenuItem("POS");

                    menu.add(menuClientes);
                    menu.add(menuMascotas);
                    menu.add(menuCitas);
                    menu.add(menuInvetary);
                    menu.add(menuPOS);
                    menuBar.add(menu);
                    frame.setJMenuBar(menuBar);

                    menuClientes.addActionListener(e -> cl.show(contentPanel, "Clientes"));
                    menuMascotas.addActionListener(e -> cl.show(contentPanel, "Mascotas"));
                    menuCitas.addActionListener(e -> cl.show(contentPanel, "Citas"));
                    menuInvetary.addActionListener(e -> cl.show(contentPanel, "Inventario"));
                    menuPOS.addActionListener(e -> cl.show(contentPanel, "POS"));

                    // 3. Muestra la pantalla de inicio para el administrador
                    cl.show(contentPanel, "Clientes");

                } else if ("pos".equalsIgnoreCase(rol)) {
                    JMenuItem menuPOS = new JMenuItem("Punto de Venta");
                    JMenuItem menuInvetary = new JMenuItem("Inventario");

                    menu.add(menuPOS);
                    menu.add(menuInvetary);
                    menuBar.add(menu);
                    frame.setJMenuBar(menuBar);

                    menuPOS.addActionListener(e -> cl.show(contentPanel, "POS"));
                    menuInvetary.addActionListener(e -> cl.show(contentPanel, "Inventario"));

                    // 4. Muestra la pantalla de inicio para el POS
                    cl.show(contentPanel, "POS");

                } else {
                    JOptionPane.showMessageDialog(frame, "Rol de usuario desconocido: " + rol, "Error de Login", JOptionPane.ERROR_MESSAGE);
                }

                frame.revalidate();
                frame.repaint();
            });



            frame.setVisible(true);
        });
    }
}