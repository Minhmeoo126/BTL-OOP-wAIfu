package com.example.libapp.controllers;

import com.example.libapp.model.Book;
import com.example.libapp.persistence.BookDAO;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class SearchBookController {
    @FXML
    private TextField searchField;
    @FXML
    private TableView<Book> bookTable;
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, String> categoryColumn;
    @FXML
    private TableColumn<Book, Integer> totalCopiesColumn;
    @FXML
    private TableColumn<Book, Integer> availableCopiesColumn;

    private BookDAO bookDAO = new BookDAO();
    private List<Book> allBooks;

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("authorName"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        totalCopiesColumn.setCellValueFactory(new PropertyValueFactory<>("totalCopies"));
        availableCopiesColumn.setCellValueFactory(new PropertyValueFactory<>("availableCopies"));

        allBooks = bookDAO.getAllBooks();
        bookTable.getItems().addAll(allBooks);
    }

    @FXML
    private void handleSearch() {
        String query = searchField.getText().trim().toLowerCase();
        if (query.isEmpty()) {
            bookTable.getItems().setAll(allBooks);
            return;
        }

        List<Book> filteredBooks = allBooks.stream()
                .filter(book -> book.getTitle().toLowerCase().contains(query) ||
                        book.getAuthorName().toLowerCase().contains(query))
                .collect(Collectors.toList());
        bookTable.getItems().setAll(filteredBooks);
    }

    @FXML
    private void backToMain() {
        Stage stage = (Stage) bookTable.getScene().getWindow();
        stage.close();
    }
}