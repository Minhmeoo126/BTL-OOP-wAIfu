package com.example.libapp.controllers;

import com.example.libapp.model.Book;
import com.example.libapp.persistence.BookDAO;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class BookViewController {
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

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("authorName"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        totalCopiesColumn.setCellValueFactory(new PropertyValueFactory<>("totalCopies"));
        availableCopiesColumn.setCellValueFactory(new PropertyValueFactory<>("availableCopies"));

        bookTable.getItems().addAll(bookDAO.getAllBooks());
    }

    @FXML
    private void backToMain() {
        Stage stage = (Stage) bookTable.getScene().getWindow();
        stage.close();
    }
}