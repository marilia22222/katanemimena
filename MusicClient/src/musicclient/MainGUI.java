package musicclient;

import javax.swing.*;
import java.awt.*;

public class MainGUI extends JFrame {
    public MainGUI() {
        setTitle("Music Library");
        setSize(400, 200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(2, 1));

        JButton addAlbumButton = new JButton("Add Album");
        JButton viewAlbumsButton = new JButton("View Albums");

        add(addAlbumButton);
        add(viewAlbumsButton);

        addAlbumButton.addActionListener(e -> new ClientGUI());
        viewAlbumsButton.addActionListener(e -> new ViewAlbumsGUI());

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainGUI::new);
    }
}