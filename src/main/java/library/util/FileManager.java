package main.java.library.util;

import main.java.library.model.Book;

import java.io.*;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileManager {
    final static String DATA_DIR = "data";
    final static String BOOKS_LIST_CSV = DATA_DIR + "/Book_List.txt";
    public static String BOOKS_TEXT_DIR = DATA_DIR + "/books_text/";

    public static List<Book> loadBooksFromCSV() throws FileNotFoundException {
        List<Book> books = new ArrayList<>();
        File file = new File(BOOKS_LIST_CSV);

        if (!file.exists()) {
            System.out.println("File not found: " + BOOKS_LIST_CSV);
            return books;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean first = true;
            int id = 1;

            while((line = reader.readLine()) != null) {
                if (first) {
                    first = false;
                    continue;
                }

                // trim skips all whitespace characters
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String title = parts[0].trim();
                    String author = parts[1].trim();
                    String publisher = parts[2].trim();
                    int year = Integer.parseInt(parts[3].trim());
                    String path = BOOKS_TEXT_DIR + parts[4].trim();

                    Book book = new Book(title, path, author, publisher, year);
                    books.add(book);
                }
            }
        } catch (IOException e) {
            // prints to standard error (stderr on UNIX)
            System.err.println("There's a problem with " + BOOKS_LIST_CSV);
        } catch (NumberFormatException e) {
            System.err.println("Invalid year: " + e.getMessage());
        }

        return books;
    }

    public static void saveBooksToCSV(List<Book> books) throws IOException {
        File file = new File(BOOKS_LIST_CSV);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("Title,Author,Publisher,Publication Year,Path");
            writer.newLine();

            for (Book book : books) {
                String line = String.format("%s,%s,%s,%d,%s", book.getTitle(), book.getAuthor(), book.getPublisher(), book.getPublicationYear(), book.getTextFilePath());
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Failed to save books: " + e.getMessage());
        }
    }

    public static void writeBookText(String filePath, String content) throws IOException {
        File file = new File(filePath);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content); // no need to catch [as stated in the exercise]
        }
    }

    public static String readFullText(String filePath) {
        File file = new File(filePath);

        if (!file.exists()) {
            return "";
        }

        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append('\n');
            }
        } catch (IOException e) {
            System.err.println("Failed to read file: " + e.getMessage());
            return "";
        }

        return content.toString();
    }
}
