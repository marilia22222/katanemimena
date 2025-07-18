package musicclient;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ClientGUI extends JFrame {
    private JTextField titleField, yearField, genreField, descriptionField;
    private JTextArea outputArea;
    private JButton addButton;

    public ClientGUI() {
        setTitle("Music Library - Add Album");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set background
        JLabel background = new JLabel(new ImageIcon(getClass().getResource("Music.jpg")));
        setContentPane(background);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        // Panel για Προσθήκη Άλμπουμ
        JPanel addAlbumPanel = new JPanel(new GridLayout(5, 2));
        addAlbumPanel.setOpaque(false); // Make it transparent
        addAlbumPanel.add(new JLabel("Title:"));
        titleField = new JTextField();
        addAlbumPanel.add(titleField);

        addAlbumPanel.add(new JLabel("Year:"));
        yearField = new JTextField();
        addAlbumPanel.add(yearField);

        addAlbumPanel.add(new JLabel("Genre:"));
        genreField = new JTextField();
        addAlbumPanel.add(genreField);

        addAlbumPanel.add(new JLabel("Description:"));
        descriptionField = new JTextField();
        addAlbumPanel.add(descriptionField);

        addButton = new JButton("Add Album");
        addAlbumPanel.add(addButton);

        outputArea = new JTextArea();
        addAlbumPanel.add(new JScrollPane(outputArea));

        tabbedPane.add("Add Album", addAlbumPanel);
        add(tabbedPane, BorderLayout.CENTER);

        addButton.addActionListener(e -> {
            String title = titleField.getText();
            int year = Integer.parseInt(yearField.getText());
            String genre = genreField.getText();
            String description = descriptionField.getText();
            List<Song> songs = new ArrayList<>();

            Album album = new Album(title, description, genre, year, songs);
            MusicClient.addAlbum(album);
            outputArea.append("Added: " + title + "\n");
        });

        setVisible(true);
    }
}