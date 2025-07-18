package musicclient;

import java.awt.BorderLayout;
import javax.swing.*;
import java.util.List;

public class ViewAlbumsGUI extends JFrame {
    private JTextArea albumListArea;

    public ViewAlbumsGUI() {
        setTitle("Albums Library");
        setSize(400, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Set background
        JLabel background = new JLabel(new ImageIcon("path/to/your/Music.jpg")); // Add your second background image
        setContentPane(background);
        setLayout(new BorderLayout());

        albumListArea = new JTextArea();
        albumListArea.setEditable(false);
        albumListArea.setOpaque(false);

        add(new JScrollPane(albumListArea));

        loadAlbums();

        setVisible(true);
    }

    private void loadAlbums() {
        List<Album> albums = MusicClient.getAlbums();
        if (albums.isEmpty()) {
            albumListArea.setText("No saved albums.");
        } else {
            for (Album album : albums) {
                albumListArea.append(album.toString() + "\n\n");
            }
        }
    }
}