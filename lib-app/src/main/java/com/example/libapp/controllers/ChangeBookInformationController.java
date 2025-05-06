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
        if(!confirmAction("bạn có muốn sửa thông tin sách không ?")) return;
        boolean isUpdate = BookDAO.updateBookInfo(book.getId(),newThumbnail,newTitle,categoryID,authorID,newDescription,messageLabel);
        if(isUpdate){
            messageLabel.setText("success");
        } else{
            messageLabel.setText("error");
        }
    }

    public void chooseImageFromFileSystem(ActionEvent event) {
        ChooseImageFromSystem.chooseImage(chooseImageButton,image,thumbnail,messageLabel);
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
