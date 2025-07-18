package ticketclient;

import cinema.*;
import cinema.User.UserRole;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class LoginFrame extends JFrame {
    private ReservationService service;

    public LoginFrame() {
        setTitle("Είσοδος Χρήστη");
        setSize(400, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        connectToServer();

        JPanel panel = new JPanel(new GridLayout(6, 1));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginBtn = new JButton("Σύνδεση");
        JButton registerBtn = new JButton("Εγγραφή");

        panel.add(new JLabel("Όνομα χρήστη:"));
        panel.add(usernameField);
        panel.add(new JLabel("Κωδικός:"));
        panel.add(passwordField);
        panel.add(loginBtn);
        panel.add(registerBtn);

        add(panel);

        loginBtn.addActionListener((ActionEvent e) -> {
            try {
                User user = service.login(usernameField.getText(), new String(passwordField.getPassword()));
                if (user != null) {
                    JOptionPane.showMessageDialog(this, "Καλωσήρθατε " + user.getFullName());
                    if (user.getRole() == UserRole.ADMIN) {
                        new AdminPanel(service, user).setVisible(true);
                    } else {
                        new UserPanel(service, user).setVisible(true);
                    }
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Λανθασμένα στοιχεία.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Σφάλμα σύνδεσης: " + ex.getMessage());
            }
        });

        registerBtn.addActionListener((ActionEvent e) -> {
            new RegisterFrame(service).setVisible(true);
        });
    }

    private void connectToServer() {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            service = (ReservationService) registry.lookup("ReservationService");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Αποτυχία σύνδεσης στον server: " + e.getMessage());
            System.exit(1);
        }
    }
}
