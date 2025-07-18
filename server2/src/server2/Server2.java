package server2;

import cinema.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Server2 {
    private static final int PORT = 5001;
    private static final String USERS_FILE = "users.dat";
    private static final String EVENTS_FILE = "events.dat";
    private static final String SHOWS_FILE = "shows.dat";
    private static final String ORDERS_FILE = "orders.dat";
    
    private Map<String, User> users;
    private Map<Integer, Event> events;
    private Map<Integer, Show> shows;
    private Map<Integer, Order> orders;
    
    private AtomicInteger eventIdCounter;
    private AtomicInteger showIdCounter;
    private AtomicInteger orderIdCounter;
    
    public Server2() {
        loadData();
    }
    
    @SuppressWarnings("unchecked")
    private void loadData() {
        // Initialize with default values
        users = new ConcurrentHashMap<>();
        events = new ConcurrentHashMap<>();
        shows = new ConcurrentHashMap<>();
        orders = new ConcurrentHashMap<>();
        
        eventIdCounter = new AtomicInteger(1);
        showIdCounter = new AtomicInteger(1);
        orderIdCounter = new AtomicInteger(1);
        
        // Try to load from files
        try {
            File usersFile = new File(USERS_FILE);
            if (usersFile.exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(usersFile))) {
                    users = (Map<String, User>) ois.readObject();
                    System.out.println("Loaded " + users.size() + " users from file");
                }
            } else {
                // Add default admin user
                User admin = new User("admin", "admin123", "System Administrator", 
                                      "1234567890", "admin@example.com", User.UserRole.ADMIN);
                users.put(admin.getUsername(), admin);
                saveUsers();
            }
            
            File eventsFile = new File(EVENTS_FILE);
            if (eventsFile.exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(eventsFile))) {
                    events = (Map<Integer, Event>) ois.readObject();
                    System.out.println("Loaded " + events.size() + " events from file");
                    
                    // Update event ID counter
                    for (Integer id : events.keySet()) {
                        if (id >= eventIdCounter.get()) {
                            eventIdCounter.set(id + 1);
                        }
                    }
                }
            }
            
            File showsFile = new File(SHOWS_FILE);
            if (showsFile.exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(showsFile))) {
                    shows = (Map<Integer, Show>) ois.readObject();
                    System.out.println("Loaded " + shows.size() + " shows from file");
                    
                    // Update show ID counter
                    for (Integer id : shows.keySet()) {
                        if (id >= showIdCounter.get()) {
                            showIdCounter.set(id + 1);
                        }
                    }
                }
            }
            
            File ordersFile = new File(ORDERS_FILE);
            if (ordersFile.exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ordersFile))) {
                    orders = (Map<Integer, Order>) ois.readObject();
                    System.out.println("Loaded " + orders.size() + " orders from file");
                    
                    // Update order ID counter
                    for (Integer id : orders.keySet()) {
                        if (id >= orderIdCounter.get()) {
                            orderIdCounter.set(id + 1);
                        }
                    }
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error loading data: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Add sample data if no events exist
        if (events.isEmpty()) {
            createSampleData();
        }
    }
    
    private void createSampleData() {
        // Create sample events
        Event movie = new Event(eventIdCounter.getAndIncrement(), "Avengers: Endgame", "Movie");
        Event concert = new Event(eventIdCounter.getAndIncrement(), "Taylor Swift Concert", "Concert");
        Event play = new Event(eventIdCounter.getAndIncrement(), "Hamlet", "Theater");
        
        // Add shows to events
        LocalDate today = LocalDate.now();
        
        // Movie shows
        Show movieShow1 = new Show(showIdCounter.getAndIncrement(), movie.getId(), 
                                  today.plusDays(1), LocalTime.of(18, 0), 10.0, 100);
        Show movieShow2 = new Show(showIdCounter.getAndIncrement(), movie.getId(), 
                                  today.plusDays(1), LocalTime.of(21, 0), 12.0, 100);
        Show movieShow3 = new Show(showIdCounter.getAndIncrement(), movie.getId(), 
                                  today.plusDays(2), LocalTime.of(18, 0), 10.0, 100);
        
        shows.put(movieShow1.getId(), movieShow1);
        shows.put(movieShow2.getId(), movieShow2);
        shows.put(movieShow3.getId(), movieShow3);
        
        movie.addShow(movieShow1);
        movie.addShow(movieShow2);
        movie.addShow(movieShow3);
        
        // Concert shows
        Show concertShow = new Show(showIdCounter.getAndIncrement(), concert.getId(), 
                                   today.plusDays(5), LocalTime.of(20, 0), 50.0, 500);
        shows.put(concertShow.getId(), concertShow);
        concert.addShow(concertShow);
        
        // Play shows
        Show playShow1 = new Show(showIdCounter.getAndIncrement(), play.getId(), 
                                 today.plusDays(3), LocalTime.of(19, 0), 25.0, 200);
        Show playShow2 = new Show(showIdCounter.getAndIncrement(), play.getId(), 
                                 today.plusDays(4), LocalTime.of(19, 0), 25.0, 200);
        
        shows.put(playShow1.getId(), playShow1);
        shows.put(playShow2.getId(), playShow2);
        
        play.addShow(playShow1);
        play.addShow(playShow2);
        
        // Save events
        events.put(movie.getId(), movie);
        events.put(concert.getId(), concert);
        events.put(play.getId(), play);
        
        saveEvents();
        saveShows();
    }
    
    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
            oos.writeObject(users);
            System.out.println("Saved " + users.size() + " users to file");
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }
    
    private void saveEvents() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(EVENTS_FILE))) {
            oos.writeObject(events);
            System.out.println("Saved " + events.size() + " events to file");
        } catch (IOException e) {
            System.err.println("Error saving events: " + e.getMessage());
        }
    }
    
    private void saveShows() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SHOWS_FILE))) {
            oos.writeObject(shows);
            System.out.println("Saved " + shows.size() + " shows to file");
        } catch (IOException e) {
            System.err.println("Error saving shows: " + e.getMessage());
        }
    }
    
    private void saveOrders() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ORDERS_FILE))) {
            oos.writeObject(orders);
            System.out.println("Saved " + orders.size() + " orders to file");
        } catch (IOException e) {
            System.err.println("Error saving orders: " + e.getMessage());
        }
    }
    
    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server2 (Database Server) started on port " + PORT);
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connection accepted from " + clientSocket.getInetAddress());
                
                // Handle client connection in a new thread
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }
    
    private class ClientHandler implements Runnable {
        private final Socket clientSocket;
        
        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }
        
        @Override
        public void run() {
            try (ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                 ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream())) {
                
                while (!clientSocket.isClosed()) {
                    try {
                        // Read message from client
                        Message request = (Message) in.readObject();
                        System.out.println("Received request: " + request.getType());
                        
                        // Process request
                        Message response = processRequest(request);
                        
                        // Send response
                        out.writeObject(response);
                        out.flush();
                        
                    } catch (EOFException | SocketException e) {
                        // Client disconnected
                        break;
                    }
                }
            } catch (Exception e) {
                System.err.println("Error handling client: " + e.getMessage());
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    System.err.println("Error closing socket: " + e.getMessage());
                }
            }
        }
        
        private Message processRequest(Message request) {
            Message response = new Message(Message.MessageType.RESPONSE);
            
            try {
                switch (request.getType()) {
                    case REGISTER_USER:
                        User newUser = (User) request.getData();
                        response.setData(registerUser(newUser));
                        break;
                        
                    case DELETE_USER:
                        Map<String, String> credentials = (Map<String, String>) request.getData();
                        response.setData(deleteUser(credentials.get("username"), credentials.get("password")));
                        break;
                        
                    case LOGIN_USER:
                        credentials = (Map<String, String>) request.getData();
                        response.setData(loginUser(credentials.get("username"), credentials.get("password")));
                        break;
                        
                    case ADD_EVENT:
                        Map<String, Object> eventData = (Map<String, Object>) request.getData();
                        Event event = (Event) eventData.get("event");
                        String adminUsername = (String) eventData.get("adminUsername");
                        response.setData(addEvent(event, adminUsername));
                        break;
                        
                    case DEACTIVATE_EVENT:
                        Map<String, Object> deactivateData = (Map<String, Object>) request.getData();
                        int eventId = (int) deactivateData.get("eventId");
                        adminUsername = (String) deactivateData.get("adminUsername");
                        response.setData(deactivateEvent(eventId, adminUsername));
                        break;
                        
                    case GET_EVENTS:
                        response.setData(getAllEvents());
                        break;
                        
                    case GET_EVENT_BY_ID:
                        eventId = (int) request.getData();
                        response.setData(getEventById(eventId));
                        break;
                        
                    case SEARCH_EVENTS:
                        String keyword = (String) request.getData();
                        response.setData(searchEvents(keyword));
                        break;
                        
                    case ADD_SHOW:
                        Map<String, Object> showData = (Map<String, Object>) request.getData();
                        Show show = (Show) showData.get("show");
                        adminUsername = (String) showData.get("adminUsername");
                        response.setData(addShow(show, adminUsername));
                        break;
                        
                    case GET_SHOWS:
                        eventId = (int) request.getData();
                        response.setData(getShowsByEventId(eventId));
                        break;
                        
                    case GET_SHOW_BY_ID:
                        int showId = (int) request.getData();
                        response.setData(getShowById(showId));
                        break;
                        
                    case CREATE_ORDER:
                        Map<String, Object> orderData = (Map<String, Object>) request.getData();
                        String username = (String) orderData.get("username");
                        showId = (int) orderData.get("showId");
                        int seats = (int) orderData.get("seats");
                        response.setData(createOrder(username, showId, seats));
                        break;
                        
                    case CANCEL_ORDER:
                        Map<String, Object> cancelData = (Map<String, Object>) request.getData();
                        int orderId = (int) cancelData.get("orderId");
                        username = (String) cancelData.get("username");
                        response.setData(cancelOrder(orderId, username));
                        break;
                        
                    case PROCESS_PAYMENT:
                        Map<String, Object> paymentData = (Map<String, Object>) request.getData();
                        orderId = (int) paymentData.get("orderId");
                        String cardholderName = (String) paymentData.get("cardholderName");
                        String cardNumber = (String) paymentData.get("cardNumber");
                        response.setData(processPayment(orderId, cardholderName, cardNumber));
                        break;
                        
                    case GET_USER_ORDERS:
                        username = (String) request.getData();
                        response.setData(getUserOrders(username));
                        break;
                        
                    default:
                        response.setSuccess(false);
                        response.setErrorMessage("Unknown request type: " + request.getType());
                }
            } catch (Exception e) {
                response.setSuccess(false);
                response.setErrorMessage("Error processing request: " + e.getMessage());
                e.printStackTrace();
            }
            
            return response;
        }
        
        // User operations
        private boolean registerUser(User user) {
            synchronized (users) {
                if (users.containsKey(user.getUsername())) {
                    return false;
                }
                
                users.put(user.getUsername(), user);
                saveUsers();
                return true;
            }
        }
        
        private boolean deleteUser(String username, String password) {
            synchronized (users) {
                User user = users.get(username);
                if (user != null && user.getPassword().equals(password)) {
                    users.remove(username);
                    saveUsers();
                    return true;
                }
                return false;
            }
        }
        
        private User loginUser(String username, String password) {
            User user = users.get(username);
            if (user != null && user.getPassword().equals(password)) {
                return user;
            }
            return null;
        }
        
        // Event operations
        private boolean addEvent(Event event, String adminUsername) {
            User admin = users.get(adminUsername);
            if (admin == null || admin.getRole() != User.UserRole.ADMIN) {
                return false;
            }
            
            synchronized (events) {
                if (event.getId() <= 0) {
                    event.setId(eventIdCounter.getAndIncrement());
                }
                
                events.put(event.getId(), event);
                saveEvents();
                
                // Save shows if any
                if (!event.getShows().isEmpty()) {
                    synchronized (shows) {
                        for (Show show : event.getShows()) {
                            if (show.getId() <= 0) {
                                show.setId(showIdCounter.getAndIncrement());
                            }
                            show.setEventId(event.getId());
                            shows.put(show.getId(), show);
                        }
                        saveShows();
                    }
                }
                
                return true;
            }
        }
        
        private boolean deactivateEvent(int eventId, String adminUsername) {
            User admin = users.get(adminUsername);
            if (admin == null || admin.getRole() != User.UserRole.ADMIN) {
                return false;
            }
            
            synchronized (events) {
                Event event = events.get(eventId);
                if (event == null) {
                    return false;
                }
                
                event.setActive(false);
                saveEvents();
                return true;
            }
        }
        
        private List<Event> getAllEvents() {
            return new ArrayList<>(events.values());
        }
        
        private Event getEventById(int eventId) {
            return events.get(eventId);
        }
        
        private List<Event> searchEvents(String keyword) {
            if (keyword == null || keyword.trim().isEmpty()) {
                return new ArrayList<>(events.values());
            }
            
            String searchTerm = keyword.toLowerCase();
            List<Event> result = new ArrayList<>();
            
            for (Event event : events.values()) {
                if (event.isActive() && (
                    event.getTitle().toLowerCase().contains(searchTerm) ||
                    event.getType().toLowerCase().contains(searchTerm))) {
                    result.add(event);
                }
            }
            
            return result;
        }
        
        // Show operations
        private boolean addShow(Show show, String adminUsername) {
            User admin = users.get(adminUsername);
            if (admin == null || admin.getRole() != User.UserRole.ADMIN) {
                return false;
            }
            
            synchronized (shows) {
                if (show.getId() <= 0) {
                    show.setId(showIdCounter.getAndIncrement());
                }
                
                shows.put(show.getId(), show);
                
                // Update event with the new show
                Event event = events.get(show.getEventId());
                if (event != null) {
                    event.addShow(show);
                    saveEvents();
                }
                
                saveShows();
                return true;
            }
        }
        
        private List<Show> getShowsByEventId(int eventId) {
            List<Show> result = new ArrayList<>();
            
            for (Show show : shows.values()) {
                if (show.getEventId() == eventId) {
                    result.add(show);
                }
            }
            
            return result;
        }
        
        private Show getShowById(int showId) {
            return shows.get(showId);
        }
        
        // Order operations
        private Order createOrder(String username, int showId, int seats) {
            User user = users.get(username);
            if (user == null) {
                return null;
            }
            
            synchronized (shows) {
                Show show = shows.get(showId);
                if (show == null || !show.reserveSeats(seats)) {
                    return null;
                }
                
                saveShows();
                
                synchronized (orders) {
                    int orderId = orderIdCounter.getAndIncrement();
                    double totalPrice = show.getPrice() * seats;
                    
                    Order order = new Order(orderId, username, showId, seats, totalPrice);
                    orders.put(orderId, order);
                    saveOrders();
                    
                    return order;
                }
            }
        }
        
        private boolean cancelOrder(int orderId, String username) {
            synchronized (orders) {
                Order order = orders.get(orderId);
                if (order == null || !order.getUsername().equals(username)) {
                    return false;
                }
                
                // Check if cancellation is allowed (not on the same day)
                Show show = shows.get(order.getShowId());
                if (show == null) {
                    return false;
                }
                
                if (show.getDate().equals(LocalDate.now())) {
                    return false;
                }
                
                // Release seats
                synchronized (shows) {
                    show.releaseSeats(order.getSeats());
                    saveShows();
                }
                
                // Remove order
                orders.remove(orderId);
                saveOrders();
                
                return true;
            }
        }
        
        private Object processPayment(int orderId, String cardholderName, String cardNumber) {
            synchronized (orders) {
                Order order = orders.get(orderId);
                if (order == null || order.isPaid()) {
                    return false;
                }
                
                // In a real system, we would process the payment here
                // For this example, we'll just mark it as paid
                order.setPaid(true);
                saveOrders();
                
                // Check if discount notification should be sent
                synchronized (shows) {
                    Show show = shows.get(order.getShowId());
                    if (show != null) {
                        // Check if it's a weekday (not Friday, Saturday, or Sunday)
                        int dayOfWeek = show.getDate().getDayOfWeek().getValue();
                        boolean isWeekday = dayOfWeek < 5; // Monday=1, Friday=5
                        
                        if (isWeekday && show.getAvailableSeats() < 15) {
                            // Return true with a special flag to indicate discount notification
                            Map<String, Object> result = new HashMap<>();
                            result.put("success", true);
                            result.put("discount", true);
                            result.put("showId", show.getId());
                            
                            // Get event title
                            Event event = events.get(show.getEventId());
                            if (event != null) {
                                result.put("eventTitle", event.getTitle());
                            }
                            
                            result.put("showDate", show.getDate().toString());
                            result.put("showTime", show.getTime().toString());
                            
                            return result;
                        }
                    }
                }
                
                return true;
            }
        }
        
        private List<Order> getUserOrders(String username) {
            List<Order> userOrders = new ArrayList<>();
            
            for (Order order : orders.values()) {
                if (order.getUsername().equals(username)) {
                    userOrders.add(order);
                }
            }
            
            return userOrders;
        }
    }
    
    public static void main(String[] args) {
        Server2 server = new Server2();
        server.start();
    }
}
