package com.example.libapp.viewmodel;

import com.example.libapp.model.BorrowingRecord;
import com.example.libapp.persistence.BorrowingRecordDAO;
import com.example.libapp.model.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import com.example.libapp.model.Book;
import com.example.libapp.persistence.BookDAO;

import java.util.List;

public class ReturnBookViewModel {
    private final BorrowingRecordDAO borrowingRecordDAO = new BorrowingRecordDAO();
    private final StringProperty message = new SimpleStringProperty("");
    private User loggedInUser;
    public StringProperty messageProperty() {
        return message;
    }
    public final BookDAO bookDAO = new BookDAO();

    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    }

    public void returnBookByISBN(String isbnText) {
        try {
            String isbn = isbnText.trim();
            if (isbn.isEmpty()) {
                message.set("Please enter an ISBN.");
                return;
            }

            if (loggedInUser == null) {
                message.set("Please log in to return a book.");
                return;
            }

            // Tìm sách theo ISBN
            Book book = bookDAO.getBookByISBN(isbn);
            if (book == null) {
                message.set("No book found with the given ISBN.");
                return;
            }

            int bookId = book.getId();

            // Tìm bản ghi mượn chưa trả của người dùng cho sách này
            List<BorrowingRecord> records = borrowingRecordDAO.getAllBorrowingRecords();
            BorrowingRecord recordToReturn = null;

            for (BorrowingRecord record : records) {
                if (record.getUserId() == loggedInUser.getId() &&
                        record.getBookId() == bookId &&
                        record.getReturnDate() == null) {
                    recordToReturn = record;
                    break;
                }
            }

            if (recordToReturn == null) {
                message.set("No borrowed book found for this ISBN.");
                return;
            }

            // Cập nhật ngày trả
            recordToReturn.setReturnDate(java.time.LocalDate.now().toString());
            borrowingRecordDAO.updateBorrowingRecord(recordToReturn);

            // Tăng số lượng sách có sẵn
            book.setAvailableCopies(book.getAvailableCopies() + 1);
            bookDAO.updateBookAvailableCopies(bookId, book.getAvailableCopies());

            message.set("Book returned successfully!");

        } catch (Exception e) {
            message.set("Error: " + e.getMessage());
        }
    }

    public void returnBookByTitle(String bookTitle) {
        try {
            if (loggedInUser == null) {
                message.set("Please log in to return a book.");
                return;
            }

            // Lấy tất cả sách mượn của người dùng và tìm sách đầu tiên khớp tiêu đề
            List<BorrowingRecord> records = borrowingRecordDAO.getAllBorrowingRecords();
            BorrowingRecord recordToReturn = null;
            Book matchedBook = null;

            for (BorrowingRecord record : records) {
                if (record.getUserId() == loggedInUser.getId() && record.getReturnDate() == null) {
                    matchedBook = bookDAO.getBookById(record.getBookId());
                    if (matchedBook != null && matchedBook.getTitle().equalsIgnoreCase(bookTitle.trim())) {
                        recordToReturn = record;
                        break;
                    }
                }
            }
            if (recordToReturn == null) {
                message.set("No borrowed book found with the given title.");
                return;
            }

            // Cập nhật ngày trả sách
            recordToReturn.setReturnDate(java.time.LocalDate.now().toString());
            borrowingRecordDAO.updateBorrowingRecord(recordToReturn);

            // Cập nhật số lượng sách có sẵn
            if (matchedBook != null) {
                matchedBook.setAvailableCopies(matchedBook.getAvailableCopies() + 1);
                bookDAO.updateBookAvailableCopies(matchedBook.getId(), matchedBook.getAvailableCopies());
            }

            message.set("Book returned successfully!");
        } catch (Exception e) {
            message.set("Error: " + e.getMessage());
        }
    }


    public void openReturnBook() {
    }

    public void openBorrowBook() {
    }

    public void logout() {

    }

}