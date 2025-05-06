package com.example.libapp.controllers;

import com.example.libapp.SessionManager;
import com.example.libapp.model.Book;
import com.example.libapp.model.User;
import com.example.libapp.persistence.BookDAO;
import com.example.libapp.utils.BookGridPane;
import com.example.libapp.utils.BookHBox;
import com.example.libapp.utils.SceneNavigator;
import com.example.libapp.utils.SearchFunction;
import com.example.libapp.viewmodel.MainViewModel;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;
import java.io.IOException;
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
    @FXML
    public GridPane Box;
    @FXML
    public ScrollPane pane;
    @FXML
    private Pane searchResultBox; // Tham chiếu đến Pane bọc ScrollPane
    @FXML
    public StackPane mainPane;


    private static final int BOOKS_PER_PAGE = 12;
    private final MainViewModel viewModel = new MainViewModel();
    private final BookDAO bookDAO = new BookDAO();
    private final List<Book> allBooks = bookDAO.getAllBooks();

    public void initialize() {
        pane.setMaxWidth(400);
        pane.setMaxHeight(400);
        searchResultBox.setVisible(false);
        search.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                searchResultBox.setLayoutX(400);
                searchResultBox.setLayoutY(50);
                searchResultBox.setPrefWidth(415.0);
                searchResultBox.setPrefHeight(150.0);

                searchResultBox.setVisible(true);
                searchResultBox.toFront();
            } else {
                PauseTransition pause = new PauseTransition(Duration.millis(150));
                pause.setOnFinished(event -> {
                    if (!searchResultBox.isHover() && !search.isFocused()) {
                        searchResultBox.setVisible(false);
                        System.out.println("SearchBar lost focus + Pane not hovered → Pane hidden");
                    }
                });
                pause.play();
            }
        });


        // Khi hiển thị, tự reposition
        mainPane.setOnMousePressed(event -> {
            if (!search.isFocused()) return;
            search.getParent().requestFocus();// ép search mất focus
            searchResultBox.setVisible(false);
            searchResultBox.toBack();
            search.setText("");
        });

        search.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                Search(); // gọi lại hàm Search mỗi khi thay đổi nội dung
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // set userInformation
        User currentUser = SessionManager.getInstance().getLoggedInUser();
        if (currentUser != null) {
            UserName.setText(currentUser.getUsername());
        } else {
            UserName.setText("User not found!!");
        }

        //tinh toan so trang va hien thi cac quyen sach thanh cac trang
        int totalPages = (int) Math.ceil(allBooks.size() / (double) BOOKS_PER_PAGE);
        pagination.setPageCount(totalPages);
        pagination.setPageFactory(this::createPage);

        // set recentlyAddBook;
        BookHBox.setUpRecentlyAddBook(cardLayout);
    }

    private GridPane createPage(int pageIndex) {
        GridPane gridPane = new GridPane();
        int start = pageIndex * BOOKS_PER_PAGE;
        int end = Math.min(start + BOOKS_PER_PAGE, allBooks.size());
        BookGridPane.makeGridPaneForVBox(allBooks, start, end, gridPane, 6);
        return gridPane;
    }

    public void Search() throws IOException {
        SearchFunction.Search(search, Box, searchResultBox);
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
}
