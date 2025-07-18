package ticketclient;

import cinema.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class RegisterFrame extends JFrame {
    public RegisterFrame(ReservationService service) {
        setTitle("Εγγραφή Χρήστη");
        setSize(400, 400);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(8, 2));

        JTextField fullName = new JTextField();
        JTextField phone = new JTextField();
        JTextField email = new JTextField();
        JTextField username = new JTextField();
        JPasswordField password = new JPasswordField();
        JComboBox<String> roleBox = new JComboBox<>(new String[]{"USER", "ADMIN"});

        panel.add(new JLabel("Ονοματεπώνυμο:"));
        panel.add(fullName);
        panel.add(new JLabel("Τηλέφωνο:"));
        panel.add(phone);
        panel.add(new JLabel("Email:"));
        panel.add(email);
        panel.add(new JLabel("Όνομα χρήστη:"));
        panel.add(username);
        panel.add(new JLabel("Κωδικός:"));
        panel.add(password);
        panel.add(new JLabel("Ρόλος:"));
        panel.add(roleBox);

        JButton submit = new JButton("Εγγραφή");
        panel.add(submit);

        add(panel);

        submit.addActionListener((ActionEvent e) -> {
            try {
                User user = new User(
                        username.getText(),
                        new String(password.getPassword()),
                        fullName.getText(),
                        phone.getText(),
                        email.getText(),
                        User.UserRole.valueOf(roleBox.getSelectedItem().toString())
                );
                boolean success = service.registerUser(user);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Εγγραφή επιτυχής");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "Ο χρήστης υπάρχει ήδη");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Σφάλμα εγγραφής: " + ex.getMessage());
            }
        });
    }
}
