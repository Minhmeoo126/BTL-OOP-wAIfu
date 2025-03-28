package com.example.libapp.model;

import java.util.Date;

public class Review {
    private int id;
    private int userId;
    private int bookId;
    private int rating;
    private String comment;
    private Date reviewDate;

    public Review(int id, int userId, int bookId, int rating, String comment, Date reviewDate) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = reviewDate;
    }

    // Getters
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public int getBookId() { return bookId; }
    public int getRating() { return rating; }
    public String getComment() { return comment; }
    public Date getReviewDate() { return reviewDate; }
}