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
            try {
                Image image = null;
                String thumbnail = selectedBook.getThumbnail();

                if (thumbnail != null && !thumbnail.isEmpty()) {
                    if (thumbnail.startsWith("http://") || thumbnail.startsWith("https://")) {
                        // Load từ URL
                        image = new Image(thumbnail, true);
                    } else {
                        // Giả sử là đường dẫn tương đối tới thư mục self_published
                        File imageFile = new File("self_published", thumbnail); // tự động nối thư mục và tên file
                        if (imageFile.exists()) {
                            image = new Image(imageFile.toURI().toString());
                        } else {
                            System.out.println("Không tìm thấy ảnh trong thư mục self_published: " + imageFile.getPath());
                        }
                    }
                }

                // Nếu không load được ảnh thì dùng mặc định
                if (image == null || image.isError()) {
                    System.out.println("Không thể load ảnh, dùng ảnh mặc định.");
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
        if (selectedBook.getAvailableCopies() > 0) {
            bookAvailable.setText("Số lượng sách còn lại là: " + (selectedBook.getAvailableCopies() - 1));
        }
    }
}
