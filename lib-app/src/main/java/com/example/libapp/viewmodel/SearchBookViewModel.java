package com.example.libapp.viewmodel;

import com.example.libapp.model.Book;
import com.example.libapp.persistence.BookDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.stream.Collectors;

public class SearchBookViewModel {
    private final BookDAO bookDAO = new BookDAO();
    private final ObservableList<Book> allBooks = FXCollections.observableArrayList();
    private final ObservableList<Book> filteredBooks = FXCollections.observableArrayList();
    private final StringProperty searchQuery = new SimpleStringProperty();

    public SearchBookViewModel() {
        allBooks.addAll(bookDAO.getAllBooks());
        filteredBooks.addAll(allBooks);
    }

    public StringProperty searchQueryProperty() {
        return searchQuery;
    }

    public ObservableList<Book> getFilteredBooks() {
        return filteredBooks;
    }

    public void search() {
        String query = searchQuery.get() != null ? searchQuery.get().trim().toLowerCase() : "";
        if (query.isEmpty()) {
            filteredBooks.setAll(allBooks);
        } else {
            List<Book> filtered = allBooks.stream()
                    .filter(book -> book.getTitle().toLowerCase().contains(query) ||
                            book.getAuthorName().toLowerCase().contains(query))
                    .collect(Collectors.toList());
            filteredBooks.setAll(filtered);
        }
    }
}