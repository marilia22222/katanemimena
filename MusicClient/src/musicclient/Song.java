package musicclient;

import java.io.Serializable;

public class Song implements Serializable {
    private String title, artist;
    private double duration;
    
    public Song(String title, String artist, double duration) {
        this.title = title;
        this.artist = artist;
        this.duration = duration;
    }
    
    public String getTitle() { return title; }
    public String getArtist() { return artist; }
    public double getDuration() { return duration; }
    
    @Override
    public String toString() {
        return title + " by " + artist + " (" + duration + " min)";
    }
}
