package main.java.library.ui;
import main.java.library.model.Book;
import main.java.library.service.LibraryService;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class MainFrame extends JFrame {
    private final LibraryService service;
    private JPanel contentPanel;
    private Book selectedBook;

    private JTextField titleField, authorField, publisherField, yearField;
    private JLabel lineCountLabel;
    private JTextArea pageArea;

    private List<String> pages;
    private int currentPage;

    public MainFrame() {
        service = new LibraryService();
        setTitle("Book Management System");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        showBooksList();
        setVisible(true);
    }

    private void showBooksList() {
        if (contentPanel != null) remove(contentPanel);

        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> showBooksList());
        contentPanel.add(refreshBtn, BorderLayout.NORTH);

        List<Book> books = service.getAllBooks();

        JPanel booksPanel = new JPanel();
        booksPanel.setLayout(new GridLayout(0, 1, 5, 5));

        for (Book book : books) {
            JButton bookBtn = new JButton(book.toString());
            bookBtn.addActionListener(e -> {
                selectedBook = book;
                showBookMenu();
            });
            booksPanel.add(bookBtn);
        }

        if (books.isEmpty()) {
            contentPanel.add(new JLabel("No books available"));
        }

        JScrollPane scrollPane = new JScrollPane(booksPanel);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void showBookMenu() {
        if (contentPanel != null) remove(contentPanel);

        contentPanel = new JPanel(new BorderLayout());

        JPanel metadataPanel = new JPanel(new GridLayout(5, 2, 10, 10));

        titleField = new JTextField(selectedBook.getTitle());
        authorField = new JTextField(selectedBook.getAuthor());
        publisherField = new JTextField(selectedBook.getPublisher());
        yearField = new JTextField(String.valueOf(selectedBook.getPublicationYear()));
        lineCountLabel = new JLabel(String.valueOf(service.countLines(selectedBook)));

        metadataPanel.add(new JLabel("Title:"));
        metadataPanel.add(titleField);

        metadataPanel.add(new JLabel("Author:"));
        metadataPanel.add(authorField);

        metadataPanel.add(new JLabel("Publisher:"));
        metadataPanel.add(publisherField);

        metadataPanel.add(new JLabel("Publication Year:"));
        metadataPanel.add(yearField);

        metadataPanel.add(new JLabel("Line Count:"));
        metadataPanel.add(lineCountLabel);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton saveBtn = new JButton("Save Metadata");
        saveBtn.addActionListener(e -> saveMetadata());

        JButton readBtn = new JButton("Read Book");
        readBtn.addActionListener(e -> openBook(false));

        JButton editBtn = new JButton("Edit Book");
        editBtn.addActionListener(e -> openBook(true));

        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> showBooksList());

        buttonPanel.add(saveBtn);
        buttonPanel.add(readBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(backBtn);

        contentPanel.add(metadataPanel, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(contentPanel, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private void saveMetadata() {
        try {
            Integer year = Integer.parseInt(yearField.getText().trim());

            boolean success = service.editBookMetadata(
                    selectedBook.getId(),
                    titleField.getText(),
                    authorField.getText(),
                    publisherField.getText(),
                    year
            );

            if (success) {
                JOptionPane.showMessageDialog(this, "Book metadata saved successfully.");
                showBookMenu();
            } else {
                JOptionPane.showMessageDialog(this, "Book not found.");
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Publication year must be a number.");
        }
    }

    private void openBook(boolean editable) {
        if (contentPanel != null) remove(contentPanel);

        contentPanel = new JPanel(new BorderLayout());

        pages = service.getBookPages(selectedBook, 20);
        currentPage = 0;

        pageArea = new JTextArea();
        pageArea.setEditable(editable);
        pageArea.setLineWrap(true);
        pageArea.setWrapStyleWord(true);

        if (!pages.isEmpty()) {
            pageArea.setText(pages.get(currentPage));
        }

        JScrollPane scrollPane = new JScrollPane(pageArea);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton prevBtn = new JButton("<");
        prevBtn.addActionListener(e -> {
            saveCurrentPageText();

            if (currentPage > 0) {
                currentPage--;
                pageArea.setText(pages.get(currentPage));
            }
        });

        JButton nextBtn = new JButton(">");
        nextBtn.addActionListener(e -> {
            saveCurrentPageText();

            if (currentPage < pages.size() - 1) {
                currentPage++;
                pageArea.setText(pages.get(currentPage));
            }
        });

        JButton backBtn = new JButton("Back");
        backBtn.addActionListener(e -> showBookMenu());

        buttonPanel.add(prevBtn);
        buttonPanel.add(nextBtn);

        if (editable) {
            JButton applyBtn = new JButton("Apply");
            applyBtn.addActionListener(e -> applyBookChanges());
            buttonPanel.add(applyBtn);
        }

        buttonPanel.add(backBtn);

        contentPanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(contentPanel, BorderLayout.CENTER);

        revalidate();
        repaint();
    }

    private void saveCurrentPageText() {
        if (pages != null && !pages.isEmpty()) {
            pages.set(currentPage, pageArea.getText());
        }
    }

    private void applyBookChanges() {
        saveCurrentPageText();

        StringBuilder fullText = new StringBuilder();

        for (String page : pages) {
            fullText.append(page);

            if (!page.endsWith("\n")) {
                fullText.append("\n");
            }
        }

        try {
            boolean success = service.editBookContent(selectedBook.getId(), fullText.toString());

            if (success) {
                JOptionPane.showMessageDialog(this, "Book content saved successfully.");
            } else {
                JOptionPane.showMessageDialog(this, "Book not found.");
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving book: " + e.getMessage());
        }
    }
}