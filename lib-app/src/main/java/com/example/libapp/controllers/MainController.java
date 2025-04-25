package com.example.libapp.controllers;

import com.example.libapp.SessionManager;
import com.example.libapp.model.Book;
import com.example.libapp.model.User;
import com.example.libapp.persistence.BookDAO;
import com.example.libapp.utils.SceneNavigator;
import com.example.libapp.viewmodel.MainViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.libapp.utils.SceneNavigator.loadView;

public class MainController {
    @FXML
    public Button AI;
    @FXML
    public Button viewBooks;
    @FXML
    public Button searchBooks;
    @FXML
    public Button myAccount;
    @FXML
    public Button borrowingHistory;
    @FXML
    public Button borrowBooks;
    @FXML
    public Button returnBook;
    @FXML
    public Button Logout;
    @FXML
    public Button addBook;
    @FXML
    public Button logout;

    private final MainViewModel viewModel = new MainViewModel();
    public Button bookManagement;
    public Button userManagement;
    public Button backToMain;
    public Label UserName;
    public HBox cardLayout;

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
                VBox cardBook = loader.load();
                CardController cardController = loader.getController();
                cardController.setData(newBook);
                cardLayout.getChildren().add(cardBook);
            }

            for(Book book : allBook){
                FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource("/com/example/libapp/view/bookCardTest.fxml"));
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

    @FXML
    private void openBookView() throws IOException {
        viewModel.openBookView();
        loadView("book-view.fxml", viewBooks);
    }

    @FXML
    private void openSearchBook() throws IOException {
        viewModel.openSearchBook();
        loadView("search-book.fxml", searchBooks);
    }

    @FXML
    private void openMyAccount() throws IOException {
        viewModel.openMyAccount();
        loadView("my-account.fxml", myAccount);
    }

    @FXML
    private void openBorrowingHistory() throws IOException {
        viewModel.openBorrowingHistory();
        loadView("borrow-history.fxml", borrowingHistory);
    }

    @FXML
    private void openBorrowBook() throws IOException {
        viewModel.openBorrowBook();
        loadView("borrow-book.fxml", borrowBooks);
    }

    @FXML
    private void openReturnBook() throws IOException {
        viewModel.openReturnBook();
        loadView("return-book.fxml", returnBook);
    }
    @FXML
    private void openAI() throws IOException {
        viewModel.openAI();
        loadView("AI-view.fxml", AI);
    }

    public void addNewBook() throws IOException {
        viewModel.openAddBook();
        loadView("add-book-view.fxml",addBook);
    }

    public void Logout() throws IOException {
        viewModel.logout();
        loadView("login-view.fxml",Logout);
    }

    public void goToBookManagement() throws IOException {
        viewModel.openBookManagement();
        loadView("bookmanagement-view.fxml" , bookManagement);
    }

    public void goToUserManagement(ActionEvent event) throws IOException {
        viewModel.openUserManagement();
        loadView("Usersmanagement-view.fxml" , userManagement);
    }

    public void backToMain() {
        SceneNavigator.backToMain(backToMain);
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