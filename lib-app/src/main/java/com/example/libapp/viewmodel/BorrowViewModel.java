package com.example.libapp.viewmodel;

import com.example.libapp.persistence.BorrowingRecordDAO;

public class BorrowViewModel {
    private BorrowingRecordDAO borrowingRecordDAO = new BorrowingRecordDAO();

    public void borrowBook(int userId, int bookId) {
        borrowingRecordDAO.borrowBook(userId, bookId);
    }

    public void returnBook(int userId, int bookId) {
        borrowingRecordDAO.returnBook(userId, bookId);
    }
}