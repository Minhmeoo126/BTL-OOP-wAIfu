package com.example.libapp.viewmodel;

import com.example.libapp.model.Book;
import com.example.libapp.persistence.BookDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SearchBookViewModel {
    private final BookDAO bookDAO = new BookDAO();
    private final ObservableList<Book> books = FXCollections.observableArrayList();

    public ObservableList<Book> getBooks() {
        return books;
    }

    public void loadBooks() {
        books.setAll(bookDAO.getAllBooks());
    }

    public void searchBooks(String query) {
        if (query == null || query.trim().isEmpty()) {
            loadBooks();
        } else {
            String lowerQuery = query.trim().toLowerCase();
            books.setAll(bookDAO.getAllBooks().stream()
                    .filter(book -> book.getTitle().toLowerCase().contains(lowerQuery) ||
                            book.getAuthorName().toLowerCase().contains(lowerQuery))
                    .collect(java.util.stream.Collectors.toList()));
        }
    }

}