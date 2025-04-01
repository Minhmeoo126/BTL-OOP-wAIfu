package com.example.libapp.viewmodel;

import com.example.libapp.model.Book;
import com.example.libapp.persistence.BookDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class BookViewModel {
    private final BookDAO bookDAO = new BookDAO();
    private final ObservableList<Book> books = FXCollections.observableArrayList();

    public BookViewModel() {
        books.addAll(bookDAO.getAllBooks());
    }

    public ObservableList<Book> getBooks() {
        return books;
    }
}