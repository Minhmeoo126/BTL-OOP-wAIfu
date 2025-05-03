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
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;

import static com.example.libapp.utils.SceneNavigator.loadView;

public class MainController {
    @FXML
    public Button AI,myAccount, returnBook,Logout,addBook,logout,bookManagement,userManagement,backToMain;
    @FXML
    public Label UserName;
    @FXML
    public HBox cardLayout;
    @FXML
    public GridPane bookContainer, Box;
    @FXML
    public Pagination pagination;
    @FXML
    public TextField search;
    @FXML
    public StackPane mainPane;
    @FXML
    private Pane searchResultBox; // Tham chiếu đến Pane bọc ScrollPane
    @FXML
    public ScrollPane pane; // Tham chiếu đến ScrollPane bên trong Pane

    public static final int BOOKS_PER_PAGE = 12;
    private final MainViewModel viewModel = new MainViewModel();
    private final BookDAO bookDAO = new BookDAO();
    private final List<Book> allBooks = bookDAO.getAllBooks();

    public void initialize() {
        pane.setMaxWidth(400);
        pane.setMaxHeight(400);
        searchResultBox.setVisible(false);

        // Định vị searchResultBox ngay dưới TextField khi focus
        search.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                searchResultBox.setLayoutX(400);
                searchResultBox.setLayoutY(50);
                searchResultBox.setPrefWidth(415.0);
                searchResultBox.setPrefHeight(150.0);

                searchResultBox.setVisible(true);
                searchResultBox.toFront();
                System.out.println("SearchBar focused: true → searchResultBox visible at X: " + searchResultBox.getLayoutX() + ", Y: " + searchResultBox.getLayoutY());
            } else {
                PauseTransition pause = new PauseTransition(Duration.millis(150));
                pause.setOnFinished(event -> {
                    if (!pane.isHover() && !search.isFocused()) {
                        searchResultBox.setVisible(false);
                        System.out.println("SearchBar lost focus + searchResultBox not hovered → searchResultBox hidden");
                    }
                });
                pause.play();
            }
        });

        // Thay đổi: Thêm sự kiện mousePressed cho searchResultBox để cho phép nhấp xuyên qua nó
        searchResultBox.setPickOnBounds(false);

        // Thay đổi: Cập nhật logic mousePressed trên mainPane để đảm bảo BorderPane có thể tương tác
        mainPane.setOnMousePressed(event -> {
            if (!search.isFocused()) return;
            search.getParent().requestFocus();// ép search mất focus
            searchResultBox.setVisible(false);
            searchResultBox.toBack();
            search.setText("");
        });

        // Gọi Search khi nội dung TextField thay đổi
        search.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                Search();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Set userInformation
        User currentUser = SessionManager.getInstance().getLoggedInUser();
        if (currentUser != null) {
            UserName.setText(currentUser.getUsername());
        } else {
            UserName.setText("khong co nguoi dung");
        }

        // Tính toán số trang và hiển thị sách
        int totalPages = (int) Math.ceil(allBooks.size() / (double) BOOKS_PER_PAGE);
        pagination.setPageCount(totalPages);
        pagination.setPageFactory(this::createPage);

        // Set recentlyAddBook
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
            // Thay đổi: Hiển thị searchResultBox mà không gọi toFront()
            searchResultBox.setVisible(true);
            return;
        }

        BookGridPane.makeGridPaneForHBox(searchBook, 0, Math.min(10, searchBook.size()), Box, 1);

        if (searchBook.size() > 10) {
            Button viewAllButton = new Button("Xem tất cả");
            viewAllButton.setStyle("-fx-font-size: 14px; -fx-text-fill: blue;");

            viewAllButton.setOnAction(e -> {
                try {
                    showAllBooks(searchBook);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });

            Box.add(viewAllButton, 0, 11);
        }

        // Thay đổi: Hiển thị searchResultBox mà không gọi toFront()
        searchResultBox.setVisible(true);
    }

    private void showAllBooks(ObservableList<Book> searchBook) throws IOException {
        loadView("add-book-view.fxml", myAccount);
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