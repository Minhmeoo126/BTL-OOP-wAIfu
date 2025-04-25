package com.example.libapp.controllers;

import com.example.libapp.SessionManager;
import com.example.libapp.model.Book;
import com.example.libapp.model.User;
import com.example.libapp.utils.SceneNavigator;
import com.example.libapp.viewmodel.MainViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.example.libapp.utils.SceneNavigator.loadView;

public class MainUserController {
    @FXML
    public Button AI;
    @FXML
    public Button backToMain;
    @FXML
    public Button myAccount;
    @FXML
    public Button returnBook;
    @FXML
    public Button borrowBooks;
    @FXML
    public Button logout;
    @FXML
    public Label UserName;
    @FXML
    public HBox cardLayout;

    private final MainViewModel viewModel = new MainViewModel();

    public void initialize() {

        User currentUser = SessionManager.getInstance().getLoggedInUser();
        if (currentUser != null) {
            UserName.setText(currentUser.getUsername());
        } else{
            UserName.setText("khong co nguoi dung");
        }
        List<Book> recentlyAdd = new ArrayList<>(recentlyAdded());
        try{
            for(Book newBook : recentlyAdd){
                FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource("/com/example/libapp/view/Bookcard-view.fxml"));
                VBox cardBook = loader.load();
                CardController cardController = loader.getController();
                cardController.setData(newBook);
                cardLayout.getChildren().add(cardBook);
            }
        }catch (IOException e){
            System.err.println("Lỗi khi load Bookcard: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void backToMain() {
        SceneNavigator.backToMain(backToMain);
    }

    public void openMyAccount() throws IOException {
        viewModel.openMyAccount();
        loadView("User-my-account-view.fxml", myAccount);
    }

    public void openReturnBook() throws IOException {
        viewModel.openReturnBook();
        loadView("return-book.fxml", returnBook);
    }

    public void openBorrowBook() throws IOException {
        viewModel.openBorrowBook();
        loadView("borrow-book.fxml", borrowBooks);
    }

    public void openAI() throws IOException {
        viewModel.openAI();
        loadView("User-Ai-view.fxml", AI);
    }

    public void Logout() throws IOException {
        viewModel.logout();
        loadView("login-view.fxml", logout);
    }

    private List<Book> recentlyAdded(){
        List<Book> recentlyAdded = new ArrayList<>();
        Book newBook = new Book();
        newBook.setAuthorName("lam");
        newBook.setTitle("lam");
        newBook.setThumbnail("/com/example/libapp/image/castorice_book.png");
        recentlyAdded.add(newBook);

        Book Book = new Book();
        Book.setAuthorName("gao so cute");
        Book.setTitle("hahah");
        Book.setThumbnail("/com/example/libapp/image/oremoi.jpg");
        recentlyAdded.add(Book);

        Book newbook = new Book();
        newbook.setAuthorName("cas chan");
        newbook.setTitle("hehefh");
        newbook.setThumbnail("/com/example/libapp/image/key.png");
        recentlyAdded.add(newbook);

        return recentlyAdded;
    }
}
