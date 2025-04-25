package com.example.libapp.viewmodel;

import com.example.libapp.model.BorrowingRecord;
import com.example.libapp.persistence.BorrowingRecordDAO;
import com.example.libapp.model.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import com.example.libapp.model.Book;
import com.example.libapp.model.BorrowingRecord;
import com.example.libapp.persistence.BookDAO;
import com.example.libapp.persistence.BorrowingRecordDAO;

import java.util.List;


public class BorrowBookViewModel {
    private final BorrowingRecordDAO borrowingRecordDAO = new BorrowingRecordDAO();
    private final StringProperty message = new SimpleStringProperty("");
    private User loggedInUser; // Giả định có cơ chế lấy user đã đăng nhập
    private final BookDAO bookDAO = new BookDAO();

    public StringProperty messageProperty() {
        return message;
    }

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    }

    public void borrowBookByID(String bookIdText) {
        try {
            int bookId = Integer.parseInt(bookIdText.trim());  // Parse the book ID
            if (loggedInUser == null) {
                message.set("Please log in to borrow a book.");
                return;
            }

            // Find the book by ID in the database
            Book book = bookDAO.getBookById(bookId);
            if (book == null) {
                message.set("No book found with the given ID.");
                return;
            }

            if (book.getAvailableCopies() <= 0) {
                message.set("This book is currently unavailable.");
                return;
            }

            // Create a new BorrowingRecord for the book and user
            BorrowingRecord record = new BorrowingRecord();
            record.setUserId(loggedInUser.getId());
            record.setBookId(book.getId());
            record.setBorrowDate(java.time.LocalDate.now().toString());
            record.setReturnDate(null);

            borrowingRecordDAO.addBorrowingRecord(record);

            // Update the available copies of the book
            book.setAvailableCopies(book.getAvailableCopies() - 1);
            bookDAO.updateBookAvailableCopies(book.getId(), book.getAvailableCopies());

            message.set("Book borrowed successfully!");
        } catch (NumberFormatException e) {
            message.set("Please enter a valid Book ID.");
        } catch (Exception e) {
            message.set("Error: " + e.getMessage());
        }
    }

    public void borrowBookByTitle(String bookTitle) {
        try {
            if (loggedInUser == null) {
                message.set("Please log in to borrow a book.");
                return;
            }

            // Find the book by title in the database
            Book book = bookDAO.getBookByTitle(bookTitle.trim());
            if (book == null) {
                message.set("No book found with the given title.");
                return;
            }

            if (book.getAvailableCopies() <= 0) {
                message.set("This book is currently unavailable.");
                return;
            }

            // Create a new BorrowingRecord for the book and user
            BorrowingRecord record = new BorrowingRecord();
            record.setUserId(loggedInUser.getId());
            record.setBookId(book.getId());
            record.setBorrowDate(java.time.LocalDate.now().toString());
            record.setReturnDate(null);

            borrowingRecordDAO.addBorrowingRecord(record);

            // Update the available copies of the book
            book.setAvailableCopies(book.getAvailableCopies() - 1);
            bookDAO.updateBookAvailableCopies(book.getId(), book.getAvailableCopies());

            message.set("Book borrowed successfully!");
        } catch (Exception e) {
            message.set("Error: " + e.getMessage());
        }
    }



    public void openReturnBook() {
    }

    public void openBorrowBook() {
    }

    public void Logout() {

    }
}