package com.example.libapp.model;

import java.util.Date;

public class BorrowingRecord {
    private int id;
    private int userId;
    private int bookId;
    private Date borrowDate;
    private Date returnDate;

    public BorrowingRecord(int id, int userId, int bookId, Date borrowDate, Date returnDate) {
        this.id = id;
        this.userId = userId;
        this.bookId = bookId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    // Getters
    public int getId() { return id; }
    public int getUserId() { return userId; }
    public int getBookId() { return bookId; }
    public Date getBorrowDate() { return borrowDate; }
    public Date getReturnDate() { return returnDate; }
}