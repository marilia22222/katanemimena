package musicserver;

import java.awt.BorderLayout;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.*;

class ServerGUI extends JFrame {
    private JTextArea logArea;
   // private static List<Album> library = new ArrayList<>(); // Αρχικοποίηση της βιβλιοθήκης

    public ServerGUI() {
        setTitle("Music Server");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        logArea = new JTextArea();
        logArea.setEditable(false);
        add(new JScrollPane(logArea), BorderLayout.CENTER);

       // new Thread(() -> startServer()).start();
       
       JButton refreshButton = new JButton("Ανανέωση Λίστας Άλμπουμ");
        refreshButton.addActionListener(e -> updateAlbumList());
        add(refreshButton, BorderLayout.SOUTH);

        setVisible(true);
    }
     public void logMessage(String message) {
        logArea.append(message + "\n");
    }
     private void updateAlbumList() {
        logArea.setText("Άλμπουμ στη Βιβλιοθήκη:\n");
        for (Map.Entry<String, Album> entry : MusicServer.getLibrary().entrySet()) {

            logArea.append(entry.getValue().toString() + "\n\n");
        }
    } 
     

//    private void startServer() {
//        try (ServerSocket server = new ServerSocket(5555)) {
//            logArea.append("Server started...\n");
//            while (true) {
//                try (Socket socket = server.accept();
//                     ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
//                     ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())) {
//
//                    String command = (String) in.readObject();
//                    if (command.equals("ADD")) {
//                        Album album = (Album) in.readObject();
//                        library.add(album); // Χρήση της στατικής λίστας
//                        out.writeObject("Album added successfully.");
//                        logArea.append("Added: " + album.getTitle() + "\n");
//                    } else if (command.equals("GET")) {
//                        String title = (String) in.readObject();
//                        Album found = library.stream()
//                                .filter(a -> a.getTitle().equalsIgnoreCase(title))
//                                .findFirst()
//                                .orElse(null);
//                        out.writeObject(found);
//                    }
//                }
//            }
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
}
