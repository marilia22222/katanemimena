package cinema;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ReservationService extends Remote {
    
    // User operations
    boolean registerUser(User user) throws RemoteException;
    boolean deleteUser(String username, String password) throws RemoteException;
    User login(String username, String password) throws RemoteException;
    void logout(String username) throws RemoteException;
    
    // Event operations
    boolean addEvent(Event event, String adminUsername) throws RemoteException;
    boolean deactivateEvent(int eventId, String adminUsername) throws RemoteException;
    List<Event> getAllEvents() throws RemoteException;
    Event getEventById(int eventId) throws RemoteException;
    List<Event> searchEvents(String keyword) throws RemoteException;
    
    // Show operations
    boolean addShow(Show show, String adminUsername) throws RemoteException;
    List<Show> getShowsByEventId(int eventId) throws RemoteException;
    Show getShowById(int showId) throws RemoteException;
    
    // Order operations
    Order createOrder(String username, int showId, int seats) throws RemoteException;
    boolean cancelOrder(int orderId, String username) throws RemoteException;
    boolean processPayment(int orderId, String cardholderName, String cardNumber) throws RemoteException;
    List<Order> getUserOrders(String username) throws RemoteException;
    
    // Callback for discount notifications
    void registerForDiscountNotifications(DiscountListener listener) throws RemoteException;
    void unregisterForDiscountNotifications(DiscountListener listener) throws RemoteException;
}
