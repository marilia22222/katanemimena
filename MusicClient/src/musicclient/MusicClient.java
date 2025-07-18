package musicclient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class MusicClient {
    private static ObjectOutputStream out;
    private static ObjectInputStream in;
    
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> new ClientGUI());
    }
    
    static void connectToServer() {
        try {
            Socket socket = new Socket("localhost", 5555);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            System.out.println("Connected to server.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    static void addAlbum(Album album) {
        try {
            out.writeObject("ADD");
            out.writeObject(album);
            out.flush();
            String response = (String) in.readObject();
            System.out.println("Server: " + response);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    static List<Album> getAlbums() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
