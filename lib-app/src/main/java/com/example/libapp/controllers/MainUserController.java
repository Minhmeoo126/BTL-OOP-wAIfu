package com.example.libapp.controllers;

import com.example.libapp.SessionManager;
import com.example.libapp.model.Book;
import com.example.libapp.model.User;
import com.example.libapp.persistence.BookDAO;
import com.example.libapp.utils.SceneNavigator;
import com.example.libapp.viewmodel.MainViewModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
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
    private final BookDAO bookDAO = new BookDAO();
    public GridPane bookContainer;

    public void initialize() {

        User currentUser = SessionManager.getInstance().getLoggedInUser();
        if (currentUser != null) {
            UserName.setText(currentUser.getUsername());
        } else{
            UserName.setText("khong co nguoi dung");
        }
        List<Book> recentlyAdd = new ArrayList<>(recentlyAdded());
        List<Book> allBook = new ArrayList<>(allBooks());
        int col = 0;
        int row = 1;
        try{
            for(Book newBook : recentlyAdd){
                FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource("/com/example/libapp/view/Bookcard-view.fxml"));
                HBox cardBook = loader.load();
                CardController cardController = loader.getController();
                cardController.setData(newBook);
                cardLayout.getChildren().add(cardBook);
            }

            for(Book book : allBook){
                FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource("/com/example/libapp/view/Gridpane_bookcard-view.fxml"));
                VBox bookBox = loader.load();
                BookController bookController = loader.getController();
                bookController.setData(book);

                if(col == 6){
                    col = 0;
                    ++row;
                }
                bookContainer.add(bookBox,col++,row);
                GridPane.setMargin(bookBox,new Insets(10));
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
        List<Book> a = new ArrayList<>(bookDAO.getAllBooks());
        for(int i = a.size() - 1; i >= a.size() - 10 ;i--){
            recentlyAdded.add(a.get(i));
        }
        return recentlyAdded;
    }


    private List<Book> allBooks() {
        List<Book> allbooks = new ArrayList<>();
        List<Book> a = new ArrayList<>(bookDAO.getAllBooks());
        for(int i = 0; i <= 20 ;i++){
            allbooks.add(a.get(i));
        }
        return allbooks;
    }
}
