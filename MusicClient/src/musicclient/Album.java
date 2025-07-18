/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author user
 */
package musicclient;

import java.io.Serializable;
import java.util.List;

public class Album implements Serializable {
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
    
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getGenre() { return genre; }
    public int getYear() { return year; }
    public List<Song> getSongs() { return songs; }
    
    public void setDescription(String description) { this.description = description; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setYear(int year) { this.year = year; }
    
    @Override
    public String toString() {
        return title + " (" + year + ") - " + genre + "\n" + description;
    }
}

