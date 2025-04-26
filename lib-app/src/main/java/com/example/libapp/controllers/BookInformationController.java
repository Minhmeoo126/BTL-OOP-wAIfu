package com.example.libapp.controllers;

import com.example.libapp.SessionManager;
import com.example.libapp.model.Book;
import com.example.libapp.model.User;
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
            try {
                Image image;
                if (selectedBook.getThumbnail() != null && !selectedBook.getThumbnail().isEmpty()) {
                    image = new Image(selectedBook.getThumbnail(), true); // true để load nền tránh trườn hợp ảnh nặng
                } else {
                    System.out.println("Thumbnail rỗng cho sách: " + selectedBook.getTitle());
                    image = new Image(getClass().getResourceAsStream("/com/example/libapp/image/castorice_book.png"));
                }

                bookImage.setImage(image);
            } catch (Exception e) {
                System.err.println("Lỗi khi load ảnh: " + e.getMessage());
                Image fallback = new Image(getClass().getResourceAsStream("/com/example/libapp/image/castorice_book.png"));
                bookImage.setImage(fallback);
            }
        }
    }

    public void backToMain() {
        SceneNavigator.backToMain(backToMain);
    }

    public void BorrowBook() {
        viewModel.borrowBookByTitle(selectedBook.getTitle());
        messagelabel.setStyle(viewModel.messageProperty().get().contains("successfully") ?
                "-fx-text-fill: green;" : "-fx-text-fill: red;");
    }
}
