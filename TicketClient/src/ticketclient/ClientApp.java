package ticketclient;

import cinema.User;
import cinema.User.UserRole;

import javax.swing.*;

public class ClientApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        });
    }
}
