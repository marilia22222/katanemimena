package cinema;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Event implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String title;
    private String type;
    private boolean active;
    private List<Show> shows;
    
    public Event() {
        this.shows = new ArrayList<>();
        this.active = true;
    }
    
    public Event(int id, String title, String type) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.active = true;
        this.shows = new ArrayList<>();
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public List<Show> getShows() {
        return shows;
    }
    
    public void setShows(List<Show> shows) {
        this.shows = shows;
    }
    
    public void addShow(Show show) {
        this.shows.add(show);
    }
    
    @Override
    public String toString() {
        return "Event{" + "id=" + id + ", title=" + title + ", type=" + type + ", active=" + active + ", shows=" + shows.size() + '}';
    }
}
