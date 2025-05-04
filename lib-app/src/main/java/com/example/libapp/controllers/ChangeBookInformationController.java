package com.example.libapp.controllers;

import com.example.libapp.SessionManager;
import com.example.libapp.model.Book;
import com.example.libapp.model.User;
import com.example.libapp.persistence.AuthorDAO;
import com.example.libapp.persistence.BookDAO;
import com.example.libapp.persistence.CategoryDAO;
import com.example.libapp.persistence.UserDAO;
import com.example.libapp.utils.AuthorAndCategoryInDatabase;
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

    public void initialize() {
        if (book == null) {
            return;
        } else {
            Image bookImage;

            if (Objects.equals(book.getThumbnail(), "")) {

                bookImage = new Image(getClass().getResourceAsStream("/com/example/libapp/image/castorice_book.png"));
            } else {
                try {
                    bookImage = new Image(book.getThumbnail(), true);
                    if (bookImage.isError()) throw new IllegalArgumentException();
                } catch (Exception e) {
                    bookImage = new Image(getClass().getResourceAsStream("/com/example/libapp/image/castorice_book.png"));
                }
            }
            image.setImage(bookImage);
            thumbnail.setText(book.getThumbnail());
            AuthorName.setText(book.getAuthorName());
            bookName.setText(book.getTitle());
            description.setText(book.getDescription());
            category.setText(book.getCategoryName());
        }
    }

    public void goToBookManagement(ActionEvent event) throws IOException {
        loadView("bookmanagement-view.fxml", backToBookManagement);
    }

    public void goToChangeBook(ActionEvent event) throws IOException {
        Image bookImage;

        if (Objects.equals(book.getThumbnail(), "")) {

            bookImage = new Image(getClass().getResourceAsStream("/com/example/libapp/image/castorice_book.png"));
        } else {
            try {
                bookImage = new Image(book.getThumbnail(), true);
                if (bookImage.isError()) throw new IllegalArgumentException();
            } catch (Exception e) {
                bookImage = new Image(getClass().getResourceAsStream("/com/example/libapp/image/castorice_book.png"));
            }
        }
        image.setImage(bookImage);
        thumbnail.setText(book.getThumbnail());
        AuthorName.setText(book.getAuthorName());
        bookName.setText(book.getTitle());
        description.setText(book.getDescription());
        category.setText(book.getCategoryName());
    }

    public void changeBookInformation() throws SQLException {
        String newThumbnail = thumbnail.getText();
        String newTitle = bookName.getText();
        String newAuthor = AuthorName.getText();
        String newCategory = category.getText();
        String newDescription = description.getText();
        int authorID = authorDAO.getAuthorIdByName(book.getAuthorName());
        int categoryID = categoryDAO.getCategoryIdByName(book.getCategoryName());
        if(thumbnail.getText().isEmpty()){
            newThumbnail = book.getThumbnail();
        }
        if(bookName.getText().isEmpty()){
            newTitle = book.getTitle();
        }
        if(AuthorName.getText().isEmpty()){
            newAuthor = book.getAuthorName();
        } else{
            authorID = AuthorAndCategoryInDatabase.checkAndAddAuthor(newAuthor,messageLabel);
        }
        if(description.getText().isEmpty()){
            newDescription = book.getDescription();
        }
        if(category.getText().isEmpty()){
            newCategory = book.getCategoryName();
        } else{
            categoryID = AuthorAndCategoryInDatabase.checkAndAddCategory(newCategory,messageLabel);
        }
        boolean isUpdate = BookDAO.updateBookInfo(book.getId(),newThumbnail,newTitle,categoryID,authorID,newDescription,messageLabel);
        if(isUpdate){
            messageLabel.setText("success");
        } else{
            messageLabel.setText("error");
        }
    }

    public void chooseImageFromFileSystem(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn ảnh thumbnail");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(chooseImageButton.getScene().getWindow());

        if (selectedFile != null) {
            try {
                // Đường dẫn đích: lib-app/self_published/
                File destDir = new File("self_published");
                if (!destDir.exists()) destDir.mkdirs();

                // Tên file giữ nguyên
                File destFile = new File(destDir, selectedFile.getName());

                // Sao chép file
                Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Preview ảnh
                Image newImage = new Image(destFile.toURI().toString());
                image.setImage(newImage);

                // Lưu đường dẫn tương đối
                thumbnail.setText(selectedFile.getName());

            } catch (Exception e) {
                e.printStackTrace();
                messageLabel.setText("Không thể tải ảnh");
            }
        }
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
