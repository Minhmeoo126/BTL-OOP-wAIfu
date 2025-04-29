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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

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
    @FXML
    public Button bookManagement;
    @FXML
    public Button userManagement;
    @FXML
    public Button backToMain;
    @FXML
    public Label UserName;
    @FXML
    public HBox cardLayout;
    @FXML
    public GridPane bookContainer;
    @FXML
    public Pagination pagination;

    public static final int BOOKS_PER_PAGE = 12;

    private final MainViewModel viewModel = new MainViewModel();
    private final BookDAO bookDAO = new BookDAO();
    private final List<Book> allBooks = bookDAO.getAllBooks();
    public ScrollPane pane;
    public GridPane Box;
    public TextField search;
    public StackPane mainPane;


    public void initialize() {
        pane.setMaxWidth(400);
        pane.setMaxHeight(400);
        search.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                pane.setVisible(true);
                pane.toFront();
                System.out.println("SearchBar focused: true → Pane visible");
            } else {
                PauseTransition pause = new PauseTransition(Duration.millis(150));
                pause.setOnFinished(event -> {
                    if (!pane.isHover() && !search.isFocused()) {
                        pane.setVisible(false);
                        System.out.println("SearchBar lost focus + Pane not hovered → Pane hidden");
                    }
                });
                pause.play();
            }
        });


        // Khi hiển thị, tự reposition
        mainPane.setOnMousePressed(event -> {
            if (!search.isFocused()) return;
            search.getParent().requestFocus(); // ép search mất focus
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
            UserName.setText("khong co nguoi dung");
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
        String keyWord = search.getText();
        Box.getChildren().clear();
        ObservableList<Book> searchBook = SearchFunction.searchFunction(FXCollections.observableArrayList(allBooks), keyWord);

        if (searchBook.isEmpty()) {
            Label noResultLabel = new Label("Không tìm thấy sách nào");
            noResultLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: grey;");
            Box.add(noResultLabel, 0, 0);
            return;
        }

        BookGridPane.makeGridPaneForHBox(searchBook, 0, Math.min(10, searchBook.size()), Box, 1);

        if (searchBook.size() > 10) {
            Button viewAllButton = new Button("Xem tất cả");
            viewAllButton.setStyle("-fx-font-size: 14px; -fx-text-fill: blue;");

            // Xử lý sự kiện khi nhấn vào button
            viewAllButton.setOnAction(e -> {
                // Tạo một màn hình mới hoặc hiển thị tất cả các kết quả tìm kiếm
                try {
                    showAllBooks(searchBook);  // Hàm để hiển thị tất cả các sách tìm được
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });

            Box.add(viewAllButton, 0, 11);
        }
    }

    private void showAllBooks(ObservableList<Book> searchBook) throws IOException {
        loadView("add-book-view.fxml" , myAccount);
    }

    @FXML
    private void openAI() throws IOException {
        viewModel.openAI();
        loadView("AI-view.fxml", AI);
    }

    public void addNewBook() throws IOException {
        viewModel.openAddBook();
        loadView("add-book-view.fxml", addBook);
    }

    public void Logout() throws IOException {
        viewModel.logout();
        loadView("login-view.fxml", Logout);
    }

    public void goToBookManagement() throws IOException {
        viewModel.openBookManagement();
        loadView("bookmanagement-view.fxml", bookManagement);
    }

    public void goToUserManagement(ActionEvent event) throws IOException {
        viewModel.openUserManagement();
        loadView("Usersmanagement-view.fxml", userManagement);
    }

    @FXML
    private void openMyAccount() throws IOException {
        viewModel.openMyAccount();
        loadView("my-account.fxml", myAccount);
    }

    public void backToMain() {
        SceneNavigator.backToMain(backToMain);
    }
}