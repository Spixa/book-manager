package main.java.library.model;

public class Book {
    private final int id;
    private String title;
    private String textFilePath;
    private String author;
    private String publisher;
    private int publicationYear;

    private static int totalBooks = 0;

    public Book(String title, String path, String author, String publisher, int publicationYear) {
        this.title = title;
        this.textFilePath = path;
        this.author = author;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.id = ++totalBooks;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTextFilePath() {
        return textFilePath;
    }

    public void setTextFilePath(String path) {
        this.textFilePath = path;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int year) {
        this.publicationYear = year;
    }

    public String toString() {
        return "[" + id + "] " + title + " | " + author + " | " + publisher + " | " + publicationYear;
    }
}
