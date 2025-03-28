package com.example.libapp.viewmodel;

import com.example.libapp.model.Book;
import com.example.libapp.persistence.BookDAO;

public class BookViewModel {
    private BookDAO bookDAO = new BookDAO();

    public void addBook(String title, String authorName, String categoryName, int totalCopies, int availableCopies) {
        bookDAO.addBook(title, authorName, categoryName, totalCopies, availableCopies);
    }
}