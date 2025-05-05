package com.example.libapp.controllers;

import com.example.libapp.SessionManager;
import com.example.libapp.model.Book;
import com.example.libapp.model.User;
import com.example.libapp.utils.LoadImage;
import com.example.libapp.utils.SceneNavigator;
import com.example.libapp.viewmodel.BorrowBookViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class BookInformationController {
    @FXML
    public Label BookName;
    @FXML
    public Label AuthorName;
    @FXML
    public Label description;
    @FXML
    public Label bookAvailable;
    @FXML
    public ImageView bookImage;
    @FXML
    public Button backToMain;
    @FXML
    private Label messagelabel;
    @FXML
    private Book selectedBook;
    @FXML
    private Label categoryName;

    private final BorrowBookViewModel viewModel = new BorrowBookViewModel();

    @FXML
    public void initialize() {
        messagelabel.textProperty().bind(viewModel.messageProperty());
        viewModel.setLoggedInUser(SessionManager.getInstance().getLoggedInUser());
    }

    public void loadBookData(Book selectedBook) {
        this.selectedBook = selectedBook;
        if (selectedBook == null) {
            System.err.println("chua co sach duoc chon");
        } else {
            BookName.setText(selectedBook.getTitle());
            AuthorName.setText(selectedBook.getAuthorName());
            categoryName.setText(selectedBook.getCategoryName());
            bookAvailable.setText("Số lượng sách còn lại là: " + selectedBook.getAvailableCopies());
            if (selectedBook.getDescription() == null) {
                description.setText("ko co description");
            } else {
                description.setText(selectedBook.getDescription());
            }
            bookImage.setImage(LoadImage.loadImage(selectedBook));
        }
    }

    public void backToMain() {
        SceneNavigator.backToMain(backToMain);
    }

    public void BorrowBook() {
        viewModel.borrowBook(selectedBook);
        messagelabel.setStyle(viewModel.messageProperty().get().contains("successfully") ?
                "-fx-text-fill: green;" : "-fx-text-fill: red;");
        bookAvailable.setText("Số lượng sách còn lại là: " + (selectedBook.getAvailableCopies() ));
    }
}
