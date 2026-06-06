package main.java.library.service;

import main.java.library.model.Book;
import main.java.library.util.FileManager;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

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
}
