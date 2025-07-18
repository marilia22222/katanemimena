package server1;

import cinema.*;
import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server1 {
    private static final int RMI_PORT = 1099;
    private static final String RMI_NAME = "ReservationService";
    private static final String SERVER2_HOST = "localhost";
    private static final int SERVER2_PORT = 5001;
    
    private ReservationServiceImpl reservationService;
    
    public Server1() {
        try {
            reservationService = new ReservationServiceImpl();
            ReservationService stub = (ReservationService) UnicastRemoteObject.exportObject(reservationService, 0);
            
            // Create and start RMI registry
            Registry registry = LocateRegistry.createRegistry(RMI_PORT);
            registry.rebind(RMI_NAME, stub);
            
            System.out.println("Server1 (Reservation Server) started on port " + RMI_PORT);
            System.out.println("RMI service bound as '" + RMI_NAME + "'");
            
        } catch (RemoteException e) {
            System.err.println("Server1 initialization error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private class ReservationServiceImpl implements ReservationService {
        private final List<DiscountListener> discountListeners;
        
        public ReservationServiceImpl() {
            this.discountListeners = new CopyOnWriteArrayList<>();
        }
        
        // Helper method to communicate with Server2
        private Message sendToServer2(Message message) throws RemoteException {
            try (Socket socket = new Socket(SERVER2_HOST, SERVER2_PORT);
                 ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
                
                // Send request to Server2
                out.writeObject(message);
                out.flush();
                
                // Receive response from Server2
                Message response = (Message) in.readObject();
                
                return response;
                
            } catch (IOException | ClassNotFoundException e) {
                throw new RemoteException("Error communicating with Server2", e);
            }
        }
        
        // User operations
        @Override
        public boolean registerUser(User user) throws RemoteException {
            Message request = new Message(Message.MessageType.REGISTER_USER, user);
            Message response = sendToServer2(request);
            
            if (!response.isSuccess()) {
                throw new RemoteException(response.getErrorMessage());
            }
            
            return (boolean) response.getData();
        }
        
        @Override
        public boolean deleteUser(String username, String password) throws RemoteException {
            Map<String, String> credentials = new HashMap<>();
            credentials.put("username", username);
            credentials.put("password", password);
            
            Message request = new Message(Message.MessageType.DELETE_USER, credentials);
            Message response = sendToServer2(request);
            
            if (!response.isSuccess()) {
                throw new RemoteException(response.getErrorMessage());
            }
            
            return (boolean) response.getData();
        }
        
        @Override
        public User login(String username, String password) throws RemoteException {
            Map<String, String> credentials = new HashMap<>();
            credentials.put("username", username);
            credentials.put("password", password);
            
            Message request = new Message(Message.MessageType.LOGIN_USER, credentials);
            Message response = sendToServer2(request);
            
            if (!response.isSuccess()) {
                throw new RemoteException(response.getErrorMessage());
            }
            
            return (User) response.getData();
        }
        
        @Override
        public void logout(String username) throws RemoteException {
            // No need to communicate with Server2 for logout
            System.out.println("User logged out: " + username);
        }
        
        // Event operations
        @Override
        public boolean addEvent(Event event, String adminUsername) throws RemoteException {
            Map<String, Object> data = new HashMap<>();
            data.put("event", event);
            data.put("adminUsername", adminUsername);
            
            Message request = new Message(Message.MessageType.ADD_EVENT, data);
            Message response = sendToServer2(request);
            
            if (!response.isSuccess()) {
                throw new RemoteException(response.getErrorMessage());
            }
            
            return (boolean) response.getData();
        }
        
        @Override
        public boolean deactivateEvent(int eventId, String adminUsername) throws RemoteException {
            Map<String, Object> data = new HashMap<>();
            data.put("eventId", eventId);
            data.put("adminUsername", adminUsername);
            
            Message request = new Message(Message.MessageType.DEACTIVATE_EVENT, data);
            Message response = sendToServer2(request);
            
            if (!response.isSuccess()) {
                throw new RemoteException(response.getErrorMessage());
            }
            
            return (boolean) response.getData();
        }
        
        @Override
        public List<Event> getAllEvents() throws RemoteException {
            Message request = new Message(Message.MessageType.GET_EVENTS);
            Message response = sendToServer2(request);
            
            if (!response.isSuccess()) {
                throw new RemoteException(response.getErrorMessage());
            }
            
            return (List<Event>) response.getData();
        }
        
        @Override
        public Event getEventById(int eventId) throws RemoteException {
            Message request = new Message(Message.MessageType.GET_EVENT_BY_ID, eventId);
            Message response = sendToServer2(request);
            
            if (!response.isSuccess()) {
                throw new RemoteException(response.getErrorMessage());
            }
            
            return (Event) response.getData();
        }
        
        @Override
        public List<Event> searchEvents(String keyword) throws RemoteException {
            Message request = new Message(Message.MessageType.SEARCH_EVENTS, keyword);
            Message response = sendToServer2(request);
            
            if (!response.isSuccess()) {
                throw new RemoteException(response.getErrorMessage());
            }
            
            return (List<Event>) response.getData();
        }
        
        // Show operations
        @Override
        public boolean addShow(Show show, String adminUsername) throws RemoteException {
            Map<String, Object> data = new HashMap<>();
            data.put("show", show);
            data.put("adminUsername", adminUsername);
            
            Message request = new Message(Message.MessageType.ADD_SHOW, data);
            Message response = sendToServer2(request);
            
            if (!response.isSuccess()) {
                throw new RemoteException(response.getErrorMessage());
            }
            
            return (boolean) response.getData();
        }
        
        @Override
        public List<Show> getShowsByEventId(int eventId) throws RemoteException {
            Message request = new Message(Message.MessageType.GET_SHOWS, eventId);
            Message response = sendToServer2(request);
            
            if (!response.isSuccess()) {
                throw new RemoteException(response.getErrorMessage());
            }
            
            return (List<Show>) response.getData();
        }
        
        @Override
        public Show getShowById(int showId) throws RemoteException {
            Message request = new Message(Message.MessageType.GET_SHOW_BY_ID, showId);
            Message response = sendToServer2(request);
            
            if (!response.isSuccess()) {
                throw new RemoteException(response.getErrorMessage());
            }
            
            return (Show) response.getData();
        }
        
        // Order operations
        @Override
        public Order createOrder(String username, int showId, int seats) throws RemoteException {
            Map<String, Object> data = new HashMap<>();
            data.put("username", username);
            data.put("showId", showId);
            data.put("seats", seats);
            
            Message request = new Message(Message.MessageType.CREATE_ORDER, data);
            Message response = sendToServer2(request);
            
            if (!response.isSuccess()) {
                throw new RemoteException(response.getErrorMessage());
            }
            
            return (Order) response.getData();
        }
        
        @Override
        public boolean cancelOrder(int orderId, String username) throws RemoteException {
            Map<String, Object> data = new HashMap<>();
            data.put("orderId", orderId);
            data.put("username", username);
            
            Message request = new Message(Message.MessageType.CANCEL_ORDER, data);
            Message response = sendToServer2(request);
            
            if (!response.isSuccess()) {
                throw new RemoteException(response.getErrorMessage());
            }
            
            return (boolean) response.getData();
        }
        
        @Override
        public boolean processPayment(int orderId, String cardholderName, String cardNumber) throws RemoteException {
            Map<String, Object> data = new HashMap<>();
            data.put("orderId", orderId);
            data.put("cardholderName", cardholderName);
            data.put("cardNumber", cardNumber);
            
            Message request = new Message(Message.MessageType.PROCESS_PAYMENT, data);
            Message response = sendToServer2(request);
            
            if (!response.isSuccess()) {
                throw new RemoteException(response.getErrorMessage());
            }
            
            // Check if discount notification should be sent
            if (response.getData() instanceof Map) {
                Map<String, Object> result = (Map<String, Object>) response.getData();
                if (result.containsKey("discount") && (boolean) result.get("discount")) {
                    // Send discount notification to all connected clients
                    int showId = (int) result.get("showId");
                    String eventTitle = (String) result.get("eventTitle");
                    String showDate = (String) result.get("showDate");
                    String showTime = (String) result.get("showTime");
                    
                    notifyDiscountToAll(showId, eventTitle, showDate, showTime);
                    return true;
                }
            }
            
            return (boolean) response.getData();
        }
        
        @Override
        public List<Order> getUserOrders(String username) throws RemoteException {
            Message request = new Message(Message.MessageType.GET_USER_ORDERS, username);
            Message response = sendToServer2(request);
            
            if (!response.isSuccess()) {
                throw new RemoteException(response.getErrorMessage());
            }
            
            return (List<Order>) response.getData();
        }
        
        // Discount notification methods
        @Override
        public void registerForDiscountNotifications(DiscountListener listener) throws RemoteException {
            discountListeners.add(listener);
            System.out.println("Client registered for discount notifications. Total listeners: " + discountListeners.size());
        }
        
        @Override
        public void unregisterForDiscountNotifications(DiscountListener listener) throws RemoteException {
            discountListeners.remove(listener);
            System.out.println("Client unregistered from discount notifications. Total listeners: " + discountListeners.size());
        }
        
        private void notifyDiscountToAll(int showId, String eventTitle, String showDate, String showTime) {
            System.out.println("Sending discount notification to " + discountListeners.size() + " clients");
            
            for (DiscountListener listener : discountListeners) {
                try {
                    listener.notifyDiscount(showId, eventTitle, showDate, showTime);
                } catch (RemoteException e) {
                    System.err.println("Error notifying client: " + e.getMessage());
                    // Remove failed listener
                    discountListeners.remove(listener);
                }
            }
        }
    }
    
    public static void main(String[] args) {
        new Server1();
    }
}
