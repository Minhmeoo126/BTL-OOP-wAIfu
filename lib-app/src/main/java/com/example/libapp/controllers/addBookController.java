package com.example.libapp.controllers;

import com.example.libapp.SessionManager;
import com.example.libapp.model.Author;
import com.example.libapp.model.Book;
import com.example.libapp.model.User;
import com.example.libapp.persistence.AuthorDAO;
import com.example.libapp.persistence.BookDAO;
import com.example.libapp.utils.AuthorAndCategoryInDatabase;
import com.example.libapp.utils.SceneNavigator;
import com.example.libapp.viewmodel.MainViewModel;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.List;

import static com.example.libapp.utils.SceneNavigator.loadView;

public class addBookController {

    public Button AI;
    public Button backToMain;
    public Button myAccount;
    public Button addBook;
    public Button bookManagement;
    public Button userManagement;
    public Button logout;
    public Label UserName;
    private final MainViewModel viewModel = new MainViewModel();
    public ImageView image;
    public TextField imageLink;
    public TextField bookName;
    public TextField AuthorName;
    private final BookDAO bookDAO = new BookDAO();
    private final AuthorDAO authorDAO = new AuthorDAO();
    private final List<Book> allBook = bookDAO.getAllBooks();
    private final List<Author> allAuthor = authorDAO.getAllAuthors();
    public Label messageLabel;
    public TextArea description;
    public Button ChooseImage;
    public TextField category;
    private String selectedImagePath;// biến lưu đường dẫn tương đối ảnh để lưu vào DB

    public void initialize() {
        User currentUser = SessionManager.getInstance().getLoggedInUser();
        if (currentUser != null) {
            UserName.setText(currentUser.getUsername());
        } else {
            UserName.setText("khong co nguoi dung");
        }
    }

    public void backToMain() {
        SceneNavigator.backToMain(backToMain);
    }

    public void openMyAccount() throws IOException {
        viewModel.openMyAccount();
        loadView("my-account.fxml", myAccount);

    }

    public void addNewBook() throws IOException {
        viewModel.openAddBook();
        loadView("add-book-view.fxml", addBook);
    }

    public void goToBookManagement() throws IOException {
        viewModel.openBookManagement();
        loadView("bookmanagement-view.fxml", bookManagement);
    }

    public void goToUserManagement() throws IOException {
        viewModel.openUserManagement();
        loadView("Usersmanagement-view.fxml", userManagement);
    }

    public void openAI() throws IOException {
        viewModel.openAI();
        loadView("AI-view.fxml", AI);
    }

    public void Logout() throws IOException {
        viewModel.logout();
        loadView("login-view.fxml", logout);
    }


    public void AddNewBook(ActionEvent event) throws SQLException {
        Book newBook = new Book();
        String newBookName = "Không có tên sách";
        String newBookAuthor = "Unknown Author";
        if(!bookName.getText().isEmpty()){
            newBookName = bookName.getText();
        }
        if(!AuthorName.getText().isEmpty()){
            newBookAuthor = AuthorName.getText();
        }

        if (bookDAO.getBookByTitle(newBookName) != null && authorDAO.getAuthorIdByName(newBookAuthor) != null) {
            messageLabel.setText("Cuon sach nay da ton tai");
            return;// neu muon xu li override thi them tai day
        }

        String newBookDescription;
        if (description.getText().isEmpty()) {
            newBookDescription = null;
        } else {
            newBookDescription = description.getText();
        }

        String newBookImageLink;
        Image newimage;
        if (imageLink.getText().isEmpty()) {
            newBookImageLink = null;
            System.out.println("Thumbnail rỗng cho sách: " + bookName);
            newimage = new Image(getClass().getResourceAsStream("/com/example/libapp/image/castorice_book.png"));
        } else {
            newBookImageLink = imageLink.getText();
            newimage = new Image(newBookImageLink, true);
        }
        image.setImage(newimage);



        AuthorAndCategoryInDatabase.checkAndAddIfAuthorNotInDataBase(newBookAuthor, messageLabel, newBook);
        String newBookCategory = "General";
        if(!category.getText().isEmpty()){
            newBookCategory = category.getText();
        }
        AuthorAndCategoryInDatabase.checkAndAddIfCategoryNotInDataBase(newBookCategory,messageLabel,newBook);

        newBook.setTitle(newBookName);
        newBook.setAuthorName(newBookAuthor);
        newBook.setDescription(newBookDescription);
        newBook.setThumbnail(newBookImageLink);
        newBook.setTotalCopies(1);
        newBook.setAvailableCopies(1);
        newBook.setCategoryId(1);
        bookDAO.addBook(newBook);
        System.out.println("da them sach vao sql");
        messageLabel.setText("success");

    }
    
}

