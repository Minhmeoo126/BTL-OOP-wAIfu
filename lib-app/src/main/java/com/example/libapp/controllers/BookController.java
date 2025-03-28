package com.example.libapp.controllers;

import com.example.libapp.viewmodel.BookViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class BookController {
    @FXML
    private TextField titleField;
    @FXML
    private TextField authorField;
    @FXML
    private TextField categoryField;

    private BookViewModel bookViewModel = new BookViewModel();

    @FXML
    private void handleAddBook() {
        String title = titleField.getText();
        String author = authorField.getText();
        String category = categoryField.getText();
        bookViewModel.addBook(title, author, category, 1, 1); // Default total and available copies
    }
}