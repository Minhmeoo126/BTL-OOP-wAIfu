package com.example.libapp.model;

public class Book {
    private int id;
    private String title;
    private int authorId;
    private int categoryId;
    private int totalCopies;
    private int availableCopies;

    public Book(int id, String title, int authorId, int categoryId, int totalCopies, int availableCopies) {
        this.id = id;
        this.title = title;
        this.authorId = authorId;
        this.categoryId = categoryId;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
    }

    // Getters and setters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public int getAuthorId() { return authorId; }
    public int getCategoryId() { return categoryId; }
    public int getTotalCopies() { return totalCopies; }
    public int getAvailableCopies() { return availableCopies; }
}