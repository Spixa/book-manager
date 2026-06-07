package main.java.library.ui;

import main.java.library.model.Book;
import main.java.library.service.LibraryService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MainFrame extends JFrame {
    private final LibraryService service;
    private JPanel contentPanel;
    private Book selectedBook;

    private JTextField titleField, authorField, publisherField, yearField;
    private JLabel lineCountLabel;

    public MainFrame() {
        service = new LibraryService();
        setTitle("Book Management System");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // centers

        showBooksList();
        setVisible(true);
    }

    private void showBooksList() {
        if (contentPanel != null) remove(contentPanel);
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> showBooksList());
        contentPanel.add(refreshBtn);

        List<Book> books = service.getAllBooks();
        for (Book book : books) {
            JButton bookBtn = new JButton(book.toString());
            bookBtn.addActionListener(e -> {
                selectedBook = book;
                showBookMenu();
            });
            contentPanel.add(bookBtn);
        }

        if (books.isEmpty()) {
            contentPanel.add(new JLabel("No books available"));
        }

        add(contentPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void showBookMenu() {
        if (contentPanel != null) remove(contentPanel);
        contentPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout());

        // TODO: display metadata and edit/read buttons here

        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> {
            showBooksList();
        });
        buttonPanel.add(backBtn);

        add(contentPanel);
        add(buttonPanel, BorderLayout.SOUTH);

        revalidate();
        repaint();
    }
}