package com.example.libapp.controllers;

import com.example.libapp.SessionManager;
import com.example.libapp.model.Book;
import com.example.libapp.model.User;
import com.example.libapp.persistence.AuthorDAO;
import com.example.libapp.persistence.BookDAO;
import com.example.libapp.persistence.CategoryDAO;
import com.example.libapp.persistence.UserDAO;
import com.example.libapp.utils.AuthorAndCategoryInDatabase;
import com.example.libapp.utils.ChooseImageFromSystem;
import com.example.libapp.utils.LoadImage;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.Objects;

import static com.example.libapp.utils.SceneNavigator.loadView;

public class ChangeBookInformationController {
    public Button backToBookManagement;
    public Button changeBook;
    public Label UserName;
    public ImageView image;
    public TextField thumbnail;
    public TextField bookName;
    public TextField AuthorName;
    public Button chooseImageButton;
    public TextField category;
    public TextArea description;
    public Label messageLabel;
    private Book book;
    private final AuthorDAO authorDAO = new AuthorDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private User currentUser = SessionManager.getInstance().getLoggedInUser();

    public void initialize() {
        UserName.setText(currentUser.getUsername());
        if (book == null) {
            return;
        } else {

            image.setImage(LoadImage.loadImage(book));
            thumbnail.setText(book.getThumbnail());
            AuthorName.setText(book.getAuthorName());
            bookName.setText(book.getTitle());
            description.setText(book.getDescription());
            category.setText(book.getCategoryName());
        }
    }

    public void goToBookManagement() throws IOException {
        loadView("bookmanagement-view.fxml", backToBookManagement);
    }

    public void goToChangeBook(ActionEvent event) throws IOException {
        image.setImage(LoadImage.loadImage(book));
        thumbnail.setText(book.getThumbnail());
        AuthorName.setText(book.getAuthorName());
        bookName.setText(book.getTitle());
        description.setText(book.getDescription());
        category.setText(book.getCategoryName());
    }

    public void changeBookInformation() throws SQLException {
        if (thumbnail.getText().trim().isEmpty() || bookName.getText().trim().isEmpty() || AuthorName.getText().trim().isEmpty()
                || category.getText().trim().isEmpty() || description.getText().trim().isEmpty()) {
            messageLabel.setText("Hãy nhập đầy đủ thông tin sách");
            messageLabel.setStyle("-fx-text-fill: red;");
            return;
        }
        String newThumbnail = thumbnail.getText().trim();
        String newTitle = bookName.getText().trim();
        String newAuthor = AuthorName.getText().trim();
        String newCategory = category.getText().trim();
        String newDescription = description.getText().trim();


        int authorID = book.getAuthorId();
        if (!newAuthor.equals(book.getAuthorName())) {
            authorID = AuthorAndCategoryInDatabase.checkAndAddAuthor(newAuthor, messageLabel);
        }

        int categoryID = book.getCategoryId();
        if (!newCategory.equals(book.getCategoryName())) {
            categoryID = AuthorAndCategoryInDatabase.checkAndAddCategory(newCategory, messageLabel);
        }

        if (!confirmAction("Bạn có muốn sửa thông tin sách không?")) return;

        boolean isUpdate = BookDAO.updateBookInfo(book.getId(), newThumbnail, newTitle, categoryID, authorID, newDescription, messageLabel);

        if (isUpdate) {
            messageLabel.setText("Thông tin sách đã được cập nhật.");
            messageLabel.setStyle("-fx-text-fill: green;");
        } else {
            messageLabel.setText("Có lỗi xảy ra khi cập nhật thông tin sách.");
            messageLabel.setStyle("-fx-text-fill: red;");
        }
    }

    public void chooseImageFromFileSystem(ActionEvent event) {
        ChooseImageFromSystem.chooseImage(chooseImageButton, image, thumbnail, messageLabel);
    }

    private boolean confirmAction(String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận");
        alert.setHeaderText(null);
        alert.setContentText(content);
        return alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK;
    }

    public void setBook(Book book) {
        this.book = book;
        initialize();
    }
}
