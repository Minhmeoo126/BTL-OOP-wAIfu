package com.example.libapp.controllers;

import com.example.libapp.model.Book;
import com.example.libapp.utils.SceneNavigator;
import com.example.libapp.viewmodel.SearchBookViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class SearchBookController {
    @FXML
    private Button backToMain;
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

    private final SearchBookViewModel viewModel = new SearchBookViewModel();

    @FXML
    public void initialize() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("authorName"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        totalCopiesColumn.setCellValueFactory(new PropertyValueFactory<>("totalCopies"));
        availableCopiesColumn.setCellValueFactory(new PropertyValueFactory<>("availableCopies"));

        bookTable.setItems(viewModel.getBooks());
        viewModel.loadBooks();
    }

    @FXML
    private void handleSearch() {
        viewModel.searchBooks(searchField.getText());
    }

    @FXML
    private void backToMain() {
        SceneNavigator.backToMain(backToMain);
    }
}