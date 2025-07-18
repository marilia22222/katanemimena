package cinema;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Order implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String username;
    private int showId;
    private int seats;
    private double totalPrice;
    private LocalDateTime orderTime;
    private boolean paid;
    
    public Order() {
        this.orderTime = LocalDateTime.now();
        this.paid = false;
    }
    
    public Order(int id, String username, int showId, int seats, double totalPrice) {
        this.id = id;
        this.username = username;
        this.showId = showId;
        this.seats = seats;
        this.totalPrice = totalPrice;
        this.orderTime = LocalDateTime.now();
        this.paid = false;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public int getShowId() {
        return showId;
    }
    
    public void setShowId(int showId) {
        this.showId = showId;
    }
    
    public int getSeats() {
        return seats;
    }
    
    public void setSeats(int seats) {
        this.seats = seats;
    }
    
    public double getTotalPrice() {
        return totalPrice;
    }
    
    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
    
    public LocalDateTime getOrderTime() {
        return orderTime;
    }
    
    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }
    
    public boolean isPaid() {
        return paid;
    }
    
    public void setPaid(boolean paid) {
        this.paid = paid;
    }
    
    @Override
    public String toString() {
        return "Order{" + "id=" + id + ", username=" + username + ", showId=" + showId + 
               ", seats=" + seats + ", totalPrice=" + totalPrice + ", paid=" + paid + '}';
    }
}
