package com.example.libapp.viewmodel;

import com.example.libapp.model.BorrowingRecord;
import com.example.libapp.persistence.BorrowingRecordDAO;
import com.example.libapp.controllers.LoginController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

public class BorrowBookViewModel {
    private final BorrowingRecordDAO borrowingRecordDAO = new BorrowingRecordDAO();
    private final StringProperty bookId = new SimpleStringProperty();
    private final StringProperty message = new SimpleStringProperty();

    public StringProperty bookIdProperty() {
        return bookId;
    }

    public StringProperty messageProperty() {
        return message;
    }
/**
    public void borrowBook() {
        try {
            int bookIdValue = Integer.parseInt(bookId.get().trim());
            BorrowingRecord record = new BorrowingRecord();
            record.setUserId(LoginController.getLoggedInUser().getId());
            record.setBookId(bookIdValue);
            record.setBorrowDate(LocalDate.now().toString());
            record.setReturnDate(null);

            borrowingRecordDAO.addBorrowingRecord(record);
            message.set("Book borrowed successfully!");
        } catch (NumberFormatException e) {
            message.set("Please enter a valid Book ID.");
        } catch (Exception e) {
            message.set("Error: " + e.getMessage());
        }
    }
 */
}