package main.java.library.service;

import main.java.library.model.Book;
import main.java.library.util.FileManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LibraryService {
    List<Book> books;

    public LibraryService() {
        try {
            books = FileManager.loadBooksFromCSV();
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
            books = new ArrayList<>();
        }
    }

    public String readFullText(int bookId) {
        Optional<Book> bookOpt = findBookById(bookId);

        // IntelliJ made me write this
        return bookOpt.map(book -> FileManager.readFullText(book.getTextFilePath())).orElse("");
    }

    public boolean editBookContent(int bookId, String newContent) throws IOException {
        Optional<Book> bookOpt = findBookById(bookId);

        if (bookOpt.isPresent()) {
            FileManager.writeBookText(bookOpt.get().getTextFilePath(), newContent);
            return true;
        }
        return false;
    }

    public Optional<Book> findBookById(int id) {
        Optional<Book> bookOpt = Optional.empty();
        for (Book book : books) {
            if (book.getId() == id) {
                bookOpt = Optional.of(book);
                break;
            }
        }

        return bookOpt;
    }

    public List<Book> getAllBooks() {
        return books;
    }

    public void saveBooks() throws IOException {
        FileManager.saveBooksToCSV(books);
    }

    public boolean editBookMetadata(int bookId, String newTitle, String newAuthor, String newPublisher, Integer newYear) {
        Optional<Book> opt = findBookById(bookId);
        if (opt.isEmpty()) return false;

        Book book = opt.get();
        // shouldn't be empty NOR contain ONLY whitespace characters
        if (newTitle != null && !newTitle.trim().isEmpty()) book.setTitle(newTitle.trim());
        if (newAuthor != null && !newAuthor.trim().isEmpty()) book.setAuthor(newAuthor.trim());
        if (newPublisher != null && !newPublisher.trim().isEmpty()) book.setPublisher(newPublisher.trim());
        if (newYear != null) book.setPublicationYear(newYear);

        try {
            saveBooks();
        } catch (IOException e) {
            System.err.println("Ran into an error: " + e.getMessage());
        }
        return true;
    }

    public List<String> getBookPages(Book book, int linesPerPage) {
        return FileManager.readBookPages(book.getTextFilePath(), linesPerPage);
    }

    public int countLines(Book book) {
        return FileManager.countLines(book.getTextFilePath());
    }
}
