package test.org.example;

import test.org.example.view.ViewAppointments;
import test.org.example.view.ViewForm;
import test.org.example.view.ViewPets;
import test.org.example.view.ViewUsers;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Veterinaria App");
            frame.setSize(800, 600);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);

            JPanel contentPanel = new JPanel(new CardLayout());

            // Crear paneles
            ViewForm loginPanel = new ViewForm();
            ViewUsers clientesPanel = new ViewUsers();
            ViewPets mascotasPanel = new ViewPets();
            ViewAppointments citasPanel = new ViewAppointments();

            // Registrar pantallas en el CardLayout
            contentPanel.add(loginPanel, "Login");
            contentPanel.add(clientesPanel, "Clientes");
            contentPanel.add(mascotasPanel, "Mascotas");
            contentPanel.add(citasPanel, "Citas");

            frame.getContentPane().add(contentPanel);

            // Listener para login exitoso
            loginPanel.setLoginListener(username -> {
                JMenuBar menuBar = new JMenuBar();
                JMenu menu = new JMenu("Navegación");

                JMenuItem menuClientes = new JMenuItem("Clientes");
                JMenuItem menuMascotas = new JMenuItem("Mascotas");
                JMenuItem menuCitas = new JMenuItem("Citas");

                menu.add(menuClientes);
                menu.add(menuMascotas);
                menu.add(menuCitas);
                menuBar.add(menu);

                frame.setJMenuBar(menuBar);

                CardLayout cl = (CardLayout) contentPanel.getLayout();

                menuClientes.addActionListener(e -> cl.show(contentPanel, "Clientes"));
                menuMascotas.addActionListener(e -> cl.show(contentPanel, "Mascotas"));
                menuCitas.addActionListener(e -> cl.show(contentPanel, "Citas"));

                cl.show(contentPanel, "Clientes");
                frame.revalidate();
                frame.repaint();
            });

            frame.setVisible(true);
        });
    }
}
