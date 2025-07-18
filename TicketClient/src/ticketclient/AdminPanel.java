package ticketclient;

import cinema.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AdminPanel extends JFrame {
    private final ReservationService service;
    private final User admin;

    public AdminPanel(ReservationService service, User admin) {
        this.service = service;
        this.admin = admin;

        setTitle("Πίνακας Διαχειριστή");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(5, 1));
        JButton addEventBtn = new JButton("➕ Καταχώρηση νέου θεάματος");
        JButton addShowBtn = new JButton("🎭 Καταχώρηση παράστασης");
        JButton deactivateEventBtn = new JButton("🚫 Απενεργοποίηση θεάματος");
        JButton viewEventsBtn = new JButton("📋 Προβολή θεαμάτων");
        JButton logoutBtn = new JButton("🚪 Αποσύνδεση");

        panel.add(addEventBtn);
        panel.add(addShowBtn);
        panel.add(deactivateEventBtn);
        panel.add(viewEventsBtn);
        panel.add(logoutBtn);
        add(panel);

        addEventBtn.addActionListener(this::handleAddEvent);
        addShowBtn.addActionListener(this::handleAddShow);
        deactivateEventBtn.addActionListener(this::handleDeactivateEvent);
        viewEventsBtn.addActionListener(this::handleViewEvents);
        logoutBtn.addActionListener(e -> {
            try {
                service.logout(admin.getUsername());
            } catch (Exception ignored) {}
            dispose();
            new LoginFrame().setVisible(true);
        });
    }

    private void handleAddEvent(ActionEvent e) {
        String title = JOptionPane.showInputDialog("Τίτλος Θεάματος:");
        String type = JOptionPane.showInputDialog("Είδος (Θέατρο, Συναυλία κλπ):");

        if (title != null && type != null) {
            cinema.Event event = new cinema.Event(0, title, type);
            try {
                if (service.addEvent(event, admin.getUsername())) {
                    JOptionPane.showMessageDialog(this, "Το θέαμα προστέθηκε.");
                } else {
                    JOptionPane.showMessageDialog(this, "Αποτυχία.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Σφάλμα: " + ex.getMessage());
            }
        }
    }

    private void handleAddShow(ActionEvent e) {
        try {
            List<cinema.Event> events = service.getAllEvents();
            if (events.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Δεν υπάρχουν διαθέσιμα θεάματα.");
                return;
            }

            String[] choices = events.stream().map(ev -> ev.getId() + ": " + ev.getTitle()).toArray(String[]::new);
            String selected = (String) JOptionPane.showInputDialog(this, "Επιλέξτε θέαμα", "Θέαμα",
                    JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);

            if (selected != null) {
                int eventId = Integer.parseInt(selected.split(":")[0]);

                String dateStr = JOptionPane.showInputDialog("Ημερομηνία (π.χ. 2025-06-01):");
                String timeStr = JOptionPane.showInputDialog("Ώρα (π.χ. 20:30):");
                String priceStr = JOptionPane.showInputDialog("Τιμή:");
                String seatsStr = JOptionPane.showInputDialog("Διαθέσιμες θέσεις:");

                Show show = new Show(
                        0,
                        eventId,
                        LocalDate.parse(dateStr),
                        LocalTime.parse(timeStr),
                        Double.parseDouble(priceStr),
                        Integer.parseInt(seatsStr)
                );

                if (service.addShow(show, admin.getUsername())) {
                    JOptionPane.showMessageDialog(this, "Η παράσταση προστέθηκε.");
                } else {
                    JOptionPane.showMessageDialog(this, "Αποτυχία.");
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Σφάλμα: " + ex.getMessage());
        }
    }

    private void handleDeactivateEvent(ActionEvent e) {
        try {
            List<cinema.Event> events = service.getAllEvents();
            String[] options = events.stream().map(ev -> ev.getId() + ": " + ev.getTitle()).toArray(String[]::new);
            String selected = (String) JOptionPane.showInputDialog(this, "Επιλέξτε θέαμα για απενεργοποίηση",
                    "Απενεργοποίηση", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            if (selected != null) {
                int id = Integer.parseInt(selected.split(":")[0]);
                if (service.deactivateEvent(id, admin.getUsername())) {
                    JOptionPane.showMessageDialog(this, "Το θέαμα απενεργοποιήθηκε.");
                } else {
                    JOptionPane.showMessageDialog(this, "Αποτυχία.");
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Σφάλμα: " + ex.getMessage());
        }
    }

    private void handleViewEvents(ActionEvent e) {
        try {
            List<cinema.Event> events = service.getAllEvents();
            StringBuilder sb = new StringBuilder("Θεάματα:\n");
            for (cinema.Event ev : events) {
                sb.append("ID: ").append(ev.getId())
                        .append(" | Τίτλος: ").append(ev.getTitle())
                        .append(" | Είδος: ").append(ev.getType())
                        .append(" | Ενεργό: ").append(ev.isActive())
                        .append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Σφάλμα: " + ex.getMessage());
        }
    }
}
