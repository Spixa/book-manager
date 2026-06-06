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

    private Optional<Book> findBookById(int id) {
        Optional<Book> bookOpt = Optional.empty();
        for (Book book : books) {
            if (book.getId() == id) {
                bookOpt = Optional.of(book);
                break;
            }
        }

        return bookOpt;
    }
}
