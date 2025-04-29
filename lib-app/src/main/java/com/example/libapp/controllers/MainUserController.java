package com.example.libapp.controllers;

import com.example.libapp.SessionManager;
import com.example.libapp.model.Book;
import com.example.libapp.model.User;
import com.example.libapp.persistence.BookDAO;
import com.example.libapp.utils.SceneNavigator;
import com.example.libapp.utils.SearchFunction;
import com.example.libapp.viewmodel.MainViewModel;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

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
    public GridPane Box;
    private final List<Book> allBooks = bookDAO.getAllBooks();
    public ScrollPane pane;
    public BorderPane mainPane;

    public void initialize() {
        pane.setMaxWidth(400);
        pane.setMaxHeight(400);
        search.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                repositionHistoryListView();
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
        search.widthProperty().addListener((obs, oldVal, newVal) -> {
            pane.setPrefWidth(newVal.doubleValue());
            repositionHistoryListView();
        });

        // Khi hiển thị, tự reposition
        search.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                repositionHistoryListView();
            }
        });
        mainPane.setOnMousePressed(event -> {
            if (!search.isFocused()) return;
            search.getParent().requestFocus(); // ép search mất focus
        });
        User currentUser = SessionManager.getInstance().getLoggedInUser();
        if (currentUser != null) {
            UserName.setText(currentUser.getUsername());
        } else {
            UserName.setText("khong co nguoi dung");
        }
        search.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                Search(); // gọi lại hàm Search mỗi khi thay đổi nội dung
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

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


    public void Search() throws IOException {
        String keyWord = search.getText();
        Box.getChildren().clear();
        ObservableList<Book> allBook = FXCollections.observableArrayList(allBooks);
        ObservableList<Book> searchBook = SearchFunction.searchFunction(allBook, keyWord);

        if (searchBook.isEmpty()) {
            Label noResultLabel = new Label("Không tìm thấy sách nào");
            noResultLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: grey;");
            Box.add(noResultLabel, 0, 0); // Thêm vào ô (0,0)
            GridPane.setMargin(noResultLabel, new Insets(10));
            return;
        }

        int col = 0;
        int row = 1;
        for(int i = 0 ; i < 10 ; i++){
            Book book = searchBook.get(i);
            FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource("/com/example/libapp/view/Bookcard-view.fxml"));
            HBox cardBook = loader.load();
            CardController cardController = loader.getController();
            cardController.setData(book);
            if(col == 1){
                col = 0;
                ++row;
            }
            Box.add(cardBook, col++, row);
            GridPane.setMargin(cardBook, new Insets(6));
        }
    }

    private void repositionHistoryListView() {
        Point2D searchBarPos = search.localToScene(0, 0);
        double listX = 360;
        double listY = 0;
        pane.setLayoutX(listX);
        pane.setLayoutY(listY);
        System.out.println("đã chạy");
    }
}
