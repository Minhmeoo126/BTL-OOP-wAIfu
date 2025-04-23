package com.example.libapp.viewmodel;

import com.example.libapp.model.BorrowingRecord;
import com.example.libapp.persistence.BorrowingRecordDAO;
import com.example.libapp.model.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BorrowBookViewModel {
    private final BorrowingRecordDAO borrowingRecordDAO = new BorrowingRecordDAO();
    private final StringProperty message = new SimpleStringProperty("");
    private User loggedInUser; // Giả định có cơ chế lấy user đã đăng nhập

    public StringProperty messageProperty() {
        return message;
    }

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    }

    public void borrowBook(String bookIdText) {
        try {
            int bookId = Integer.parseInt(bookIdText.trim());
            if (loggedInUser == null) {
                message.set("Please log in to borrow a book.");
                return;
            }

            BorrowingRecord record = new BorrowingRecord();
            record.setUserId(loggedInUser.getId());
            record.setBookId(bookId);
            record.setBorrowDate(java.time.LocalDate.now().toString());
            record.setReturnDate(null);

            borrowingRecordDAO.addBorrowingRecord(record);
            message.set("Book borrowed successfully!");
        } catch (NumberFormatException e) {
            message.set("Please enter a valid Book ID.");
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