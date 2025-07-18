package cinema;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Show implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private int eventId;
    private LocalDate date;
    private LocalTime time;
    private double price;
    private int totalSeats;
    private int availableSeats;
    
    public Show() {
    }
    
    public Show(int id, int eventId, LocalDate date, LocalTime time, double price, int totalSeats) {
        this.id = id;
        this.eventId = eventId;
        this.date = date;
        this.time = time;
        this.price = price;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getEventId() {
        return eventId;
    }
    
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public LocalTime getTime() {
        return time;
    }
    
    public void setTime(LocalTime time) {
        this.time = time;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public int getTotalSeats() {
        return totalSeats;
    }
    
    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }
    
    public int getAvailableSeats() {
        return availableSeats;
    }
    
    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }
    
    public boolean reserveSeats(int count) {
        if (availableSeats >= count) {
            availableSeats -= count;
            return true;
        }
        return false;
    }
    
    public void releaseSeats(int count) {
        availableSeats += count;
        if (availableSeats > totalSeats) {
            availableSeats = totalSeats;
        }
    }
    
    @Override
    public String toString() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        return "Show{" + "id=" + id + ", date=" + date.format(dateFormatter) + 
               ", time=" + time.format(timeFormatter) + ", price=" + price + 
               ", available=" + availableSeats + "/" + totalSeats + '}';
    }
}
