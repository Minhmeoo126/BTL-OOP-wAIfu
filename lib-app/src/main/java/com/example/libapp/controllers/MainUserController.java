package com.example.libapp.controllers;

import com.example.libapp.SessionManager;
import com.example.libapp.model.Book;
import com.example.libapp.model.User;
import com.example.libapp.persistence.BookDAO;
import com.example.libapp.utils.SceneNavigator;
import com.example.libapp.utils.SearchFunction;
import com.example.libapp.viewmodel.MainViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    @FXML
    public GridPane bookContainer;
    @FXML
    public TextField search;
    @FXML
    public Pagination pagination;

    private static final int BOOKS_PER_PAGE = 12;
    private final MainViewModel viewModel = new MainViewModel();
    private final BookDAO bookDAO = new BookDAO();
    private List<Book> allBooks = bookDAO.getAllBooks();

    public void initialize() {

        User currentUser = SessionManager.getInstance().getLoggedInUser();
        if (currentUser != null) {
            UserName.setText(currentUser.getUsername());
        } else {
            UserName.setText("khong co nguoi dung");
        }
        List<Book> recentlyAdd = new ArrayList<>(recentlyAdded());
        try {
            for (Book newBook : recentlyAdd) {
                FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource("/com/example/libapp/view/Bookcard-view.fxml"));
                HBox cardBook = loader.load();
                CardController cardController = loader.getController();
                cardController.setData(newBook);
                cardLayout.getChildren().add(cardBook);
            }
            int totalPages = (int) Math.ceil(allBooks.size() / (double) BOOKS_PER_PAGE);
            pagination.setPageCount(totalPages);
            pagination.setPageFactory(this::createPage);
        } catch (IOException e) {
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

    private List<Book> recentlyAdded() {
        List<Book> recentlyAdded = new ArrayList<>();
        List<Book> a = new ArrayList<>(bookDAO.getAllBooks());
        for (int i = a.size() - 1; i >= a.size() - 10; i--) {
            recentlyAdded.add(a.get(i));
        }
        return recentlyAdded;
    }


    private GridPane createPage(int pageIndex) {
        GridPane gridPane = new GridPane();
        int start = pageIndex * BOOKS_PER_PAGE;
        int end = Math.min(start + BOOKS_PER_PAGE, allBooks.size());
        int col = 0;
        int row = 1;
        try {
            for (int i = start; i < end; i++) {
                Book book = allBooks.get(i);
                FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource("/com/example/libapp/view/Gridpane_bookcard-view.fxml"));
                VBox bookBox = loader.load();
                BookController bookController = loader.getController();
                bookController.setData(book);

                if (col == 6) {
                    col = 0;
                    ++row;
                }
                gridPane.add(bookBox, col++, row);
                GridPane.setMargin(bookBox, new Insets(6));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return gridPane;
    }


    public void Search() {
        String keyWord = search.getText();
        ObservableList<Book> allBook = FXCollections.observableArrayList(bookDAO.getAllBooks());
        ObservableList<Book> searchBook = SearchFunction.searchFunction(allBook, keyWord);
    }
}
