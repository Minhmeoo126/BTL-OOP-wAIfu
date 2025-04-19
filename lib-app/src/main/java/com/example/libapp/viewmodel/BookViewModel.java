package com.example.libapp.viewmodel;

import com.example.libapp.model.Book;
import com.example.libapp.persistence.BookDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BookViewModel {
    private final BookDAO bookDAO = new BookDAO();
    private final ObservableList<Book> books = FXCollections.observableArrayList();

    public ObservableList<Book> getBooks() {
        return books;
    }

    public void loadBooks() {
        books.setAll(bookDAO.getAllBooks());
    }

    // Method to get a specific book by ID (useful for Book Info view)
    public Book getBookById(int bookId) {
        return books.stream()
                .filter(book -> book.getId() == bookId)
                .findFirst()
                .orElse(null);
    }
}