
package musicserver;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author user
 */class Album implements Serializable {
    private String title, description, genre;
    private int year;
    private List<Song> songs;
    
    public Album(String title, String description, String genre, int year, List<Song> songs) {
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.year = year;
        this.songs = songs;
    }

    Album(String title, String description, String genre, int year) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getGenre() { return genre; }
    public int getYear() { return year; }
    public List<Song> getSongs() { return songs; }
    
    public void setDescription(String description) { this.description = description; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setYear(int year) { this.year = year; }
    
    public String toString() {
        return title + " (" + year + ") - " + genre + "\n" + description;
    }
}
