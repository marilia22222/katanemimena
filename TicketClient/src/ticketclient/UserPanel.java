package ticketclient;

import cinema.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class UserPanel extends JFrame implements DiscountListener {
    private final ReservationService service;
    private final User user;

    public UserPanel(ReservationService service, User user) {
        this.service = service;
        this.user = user;

        setTitle("Πίνακας Χρήστη");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        try {
            service.registerForDiscountNotifications(this);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Σφάλμα στην εγγραφή για εκπτώσεις.");
        }

        JPanel panel = new JPanel(new GridLayout(6, 1));
        JButton searchEventsBtn = new JButton("🔍 Αναζήτηση Εκδηλώσεων");
        JButton orderTicketBtn = new JButton("🎫 Παραγγελία Εισιτηρίων");
        JButton cancelOrderBtn = new JButton("❌ Ακύρωση Παραγγελίας");
        JButton viewOrdersBtn = new JButton("📜 Οι Παραγγελίες μου");
        JButton logoutBtn = new JButton("🚪 Αποσύνδεση");

        panel.add(searchEventsBtn);
        panel.add(orderTicketBtn);
        panel.add(cancelOrderBtn);
        panel.add(viewOrdersBtn);
        panel.add(logoutBtn);

        add(panel);

        searchEventsBtn.addActionListener(this::handleSearchEvents);
        orderTicketBtn.addActionListener(this::handleOrderTickets);
        cancelOrderBtn.addActionListener(this::handleCancelOrder);
        viewOrdersBtn.addActionListener(this::handleViewOrders);
        logoutBtn.addActionListener(e -> {
            try {
                service.unregisterForDiscountNotifications(this);
                service.logout(user.getUsername());
            } catch (Exception ignored) {}
            dispose();
            new LoginFrame().setVisible(true);
        });
    }

    private void handleSearchEvents(ActionEvent e) {
        String keyword = JOptionPane.showInputDialog("Λέξη κλειδί (ή κενό για όλα):");
        try {
            List<cinema.Event> events = service.searchEvents(keyword);
            StringBuilder sb = new StringBuilder("Αποτελέσματα:\n");
            for (cinema.Event ev : events) {
                sb.append("ID: ").append(ev.getId())
                        .append(" | ").append(ev.getTitle())
                        .append(" [").append(ev.getType()).append("]\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Σφάλμα: " + ex.getMessage());
        }
    }

    private void handleOrderTickets(ActionEvent e) {
        try {
            List<cinema.Event> events = service.getAllEvents();
            String[] choices = events.stream().map(ev -> ev.getId() + ": " + ev.getTitle()).toArray(String[]::new);
            String selectedEvent = (String) JOptionPane.showInputDialog(this, "Επιλέξτε Εκδήλωση", "Επιλογή",
                    JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
            if (selectedEvent == null) return;

            int eventId = Integer.parseInt(selectedEvent.split(":")[0]);
            List<Show> shows = service.getShowsByEventId(eventId);
            if (shows.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Δεν υπάρχουν παραστάσεις για το θέαμα.");
                return;
            }

            String[] showOptions = shows.stream().map(
                    sh -> sh.getId() + ": " + sh.getDate() + " " + sh.getTime() + " | Διαθέσιμες: " + sh.getAvailableSeats()
            ).toArray(String[]::new);

            String selectedShow = (String) JOptionPane.showInputDialog(this, "Επιλογή Παράστασης", "Επιλογή",
                    JOptionPane.QUESTION_MESSAGE, null, showOptions, showOptions[0]);
            if (selectedShow == null) return;

            int showId = Integer.parseInt(selectedShow.split(":")[0]);
            String seatStr = JOptionPane.showInputDialog("Πόσες θέσεις θέλετε;");

            Order order = service.createOrder(user.getUsername(), showId, Integer.parseInt(seatStr));
            if (order == null) {
                JOptionPane.showMessageDialog(this, "Αποτυχία δημιουργίας παραγγελίας.");
                return;
            }

            JOptionPane.showMessageDialog(this, "Δημιουργήθηκε παραγγελία. Προχωρήστε σε πληρωμή.");
            handlePayment(order.getId());

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Σφάλμα: " + ex.getMessage());
        }
    }

    private void handlePayment(int orderId) {
        JTextField nameField = new JTextField();
        JTextField cardField = new JTextField();
        Object[] fields = {
                "Ονοματεπώνυμο:", nameField,
                "Αριθμός κάρτας:", cardField
        };

        int result = JOptionPane.showConfirmDialog(this, fields, "Πληρωμή", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                boolean paid = service.processPayment(orderId, nameField.getText(), cardField.getText());
                if (paid) {
                    JOptionPane.showMessageDialog(this, "Η πληρωμή ολοκληρώθηκε.");
                } else {
                    JOptionPane.showMessageDialog(this, "Αποτυχία πληρωμής.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Σφάλμα: " + ex.getMessage());
            }
        }
    }

    private void handleCancelOrder(ActionEvent e) {
        try {
            List<Order> orders = service.getUserOrders(user.getUsername());
            if (orders.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Δεν έχετε παραγγελίες.");
                return;
            }

            String[] options = orders.stream().map(o ->
                    o.getId() + ": Παράσταση " + o.getShowId() + " | Θέσεις: " + o.getSeats()
            ).toArray(String[]::new);

            String selected = (String) JOptionPane.showInputDialog(this, "Επιλέξτε παραγγελία για ακύρωση:",
                    "Ακύρωση", JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            if (selected != null) {
                int orderId = Integer.parseInt(selected.split(":")[0]);
                boolean cancelled = service.cancelOrder(orderId, user.getUsername());
                if (cancelled) {
                    JOptionPane.showMessageDialog(this, "Η παραγγελία ακυρώθηκε.");
                } else {
                    JOptionPane.showMessageDialog(this, "Δεν ήταν δυνατή η ακύρωση (ίσως είναι για σήμερα).");
                }
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Σφάλμα: " + ex.getMessage());
        }
    }

    private void handleViewOrders(ActionEvent e) {
        try {
            List<Order> orders = service.getUserOrders(user.getUsername());
            StringBuilder sb = new StringBuilder("Οι Παραγγελίες σας:\n");
            for (Order o : orders) {
                sb.append("ID: ").append(o.getId())
                        .append(" | Παράσταση: ").append(o.getShowId())
                        .append(" | Θέσεις: ").append(o.getSeats())
                        .append(" | Πληρωμένο: ").append(o.isPaid())
                        .append("\n");
            }
            JOptionPane.showMessageDialog(this, sb.toString());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Σφάλμα: " + ex.getMessage());
        }
    }

    // 📣 Callback για εκπτώσεις
    @Override
    public void notifyDiscount(int showId, String eventTitle, String showDate, String showTime) {
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(this,
                "🎉 Έκπτωση 50% για την παράσταση " + eventTitle + " (" + showDate + " " + showTime + ")!\n" +
                "Απομένουν λιγότερες από 15 θέσεις!"));
    }
}
