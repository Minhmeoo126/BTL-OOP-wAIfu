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

    public void returnBookByID(String bookIdText) {
        try {
            int bookId = Integer.parseInt(bookIdText.trim());
            if (loggedInUser == null) {
                message.set("Please log in to return a book.");
                return;
            }

            // Tìm kiếm bản ghi mượn sách với bookId và userId
            List<BorrowingRecord> records = borrowingRecordDAO.getAllBorrowingRecords();
            BorrowingRecord recordToReturn = null;

            for (BorrowingRecord record : records) {
                if (record.getUserId() == loggedInUser.getId() && record.getBookId() == bookId && record.getReturnDate() == null) {
                    recordToReturn = record;
                    break;
                }
            }

            if (recordToReturn == null) {
                message.set("No borrowed book found with the given ID.");
                return;
            }

            // Cập nhật ngày trả sách
            recordToReturn.setReturnDate(java.time.LocalDate.now().toString());
            borrowingRecordDAO.updateBorrowingRecord(recordToReturn); // Cập nhật thông tin trả sách

            // Cập nhật số lượng sách có sẵn
            Book book = bookDAO.getBookById(bookId);
            if (book != null) {
                book.setAvailableCopies(book.getAvailableCopies() + 1);
                bookDAO.updateBookAvailableCopies(bookId, book.getAvailableCopies());
            }

            message.set("Book returned successfully!");
        } catch (NumberFormatException e) {
            message.set("Please enter a valid Book ID.");
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