package com.example.libapp.controllers;

import com.example.libapp.SessionManager;
import com.example.libapp.model.Book;
import com.example.libapp.model.User;
import com.example.libapp.viewmodel.BookViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.IOException;

public class BookViewController {
    @FXML
    private Button backToMain;
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

    private final BookViewModel viewModel = new BookViewModel();

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
    private void backToMain() {
        try {
            User currentUser = SessionManager.getInstance().getLoggedInUser();
            String role = currentUser.getRole();
            if (role.equals("ADMIN")) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/libapp/view/main-admin-view.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) backToMain.getScene().getWindow();
                stage.setScene(new Scene(root, 900, 600));
                stage.show();
            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/libapp/view/main.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) backToMain.getScene().getWindow();
                stage.setScene(new Scene(root, 900, 600));
                stage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}