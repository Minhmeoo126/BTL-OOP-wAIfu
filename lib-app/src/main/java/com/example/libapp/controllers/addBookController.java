package com.example.libapp.controllers;

import com.example.libapp.SessionManager;
import com.example.libapp.model.Author;
import com.example.libapp.model.Book;
import com.example.libapp.model.User;
import com.example.libapp.persistence.AuthorDAO;
import com.example.libapp.persistence.BookDAO;
import com.example.libapp.utils.SceneNavigator;
import com.example.libapp.viewmodel.MainViewModel;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


import java.io.IOException;
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
    public TextField thumbnail;
    public TextField bookName;
    public TextField AuthorName;
    public TextField description;
    private final BookDAO bookDAO = new BookDAO();
    private final AuthorDAO authorDAO = new AuthorDAO();
    private final List<Book> allBook = bookDAO.getAllBooks();
    private final List<Author> allAuthor = authorDAO.getAllAuthors();
    public Label messageLabel;

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

    public void AddNewBook(ActionEvent event) {
        Book newBook = new Book();
        String newBookName;
        String newBookAuthor;
        String newBookDescription;
        String newBookThumbnail;
        Image newimage;
        if (bookName.getText().isEmpty()) {
            newBookName = "Không có tên sách";
        } else {
            newBookName = bookName.getText();
        }
        if (AuthorName.getText().isEmpty()) {
            newBookAuthor = "Unknown Author";
        } else {
            newBookAuthor = AuthorName.getText();
        }
        if (description.getText().isEmpty()) {
            newBookDescription = "Không có description";
        } else {
            newBookDescription = description.getText();
        }
        if (thumbnail.getText().isEmpty()) {
            newBookThumbnail = "";
            System.out.println("Thumbnail rỗng cho sách: " + bookName);
            newimage = new Image(getClass().getResourceAsStream("/com/example/libapp/image/castorice_book.png"));
        } else {
            newBookThumbnail = thumbnail.getText();
            newimage = new Image(newBookThumbnail, true);
        }
        image.setImage(newimage);

        if (bookDAO.getBookByTitle(newBookName) != null) {
            messageLabel.setText("Tieu de nay da ton tai");
            return;
        }
        Integer authorId = null;
        try {
            // Kiểm tra xem tác giả đã tồn tại trong cơ sở dữ liệu chưa
            authorId = authorDAO.getAuthorIdByName(newBookAuthor);
            if (authorId != null) {
                System.out.println("Tác giả đã tồn tại với ID: " + authorId);
            }
        } catch (SQLException e) {
            messageLabel.setText("Không thể xác định tác giả.");
            e.printStackTrace();
            return;
        }

        if (authorId == null) {
            Author newAuthor = new Author();
            newAuthor.setName(newBookAuthor);
            newAuthor.setBio("");
            try {
                authorDAO.addAuthor(newAuthor);
                // Lấy lại ID của tác giả sau khi thêm
                authorId = authorDAO.getAuthorIdByName(newBookAuthor);
                System.out.println("Tác giả mới đã được thêm với ID: " + authorId);
            } catch (SQLException e) {
                messageLabel.setText("Lỗi khi thêm tác giả.");
                e.printStackTrace();
                return;
            }
        }
        if (authorId == null) {
            messageLabel.setText("Không thể xác định tác giả.");
            return;
        }
        newBook.setTitle(newBookName);
        newBook.setAuthorId(authorId);
        newBook.setAuthorName(newBookAuthor);
        newBook.setDescription(newBookDescription);
        newBook.setThumbnail(newBookThumbnail);
        newBook.setTotalCopies(1);
        newBook.setAvailableCopies(1);
        newBook.setCategoryId(1);
        bookDAO.addBook(newBook);
        messageLabel.setText("success");
    }
}

