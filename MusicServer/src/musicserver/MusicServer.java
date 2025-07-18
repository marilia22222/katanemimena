package musicserver;

import java.util.HashMap;
import java.util.Map;

public class MusicServer {
    private static Map<String, Album> library = new HashMap<>();

    public static void main(String[] args) {
        new ServerGUI();
    }

    public static Map<String, Album> getLibrary() {
        return library;
    }

    public static void addAlbum(Album album) {
        library.put(album.getTitle(), album);
    }
}
