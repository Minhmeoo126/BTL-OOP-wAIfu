package com.example.libapp.controllers;

import com.example.libapp.SessionManager;
import com.example.libapp.model.Book;
import com.example.libapp.model.User;
import com.example.libapp.persistence.BookDAO;
import com.example.libapp.persistence.DatabaseConnection;
import com.example.libapp.persistence.UserDAO;
import com.example.libapp.utils.BookGridPane;
import com.example.libapp.utils.SceneNavigator;
import com.example.libapp.utils.SearchFunction;
import com.example.libapp.viewmodel.BookViewModel;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static com.example.libapp.utils.SceneNavigator.loadView;

public class BookManagementController {
    @FXML
    public Button AI, myAccount, addBook, bookManage, userManagement, logout, backToMain;
    @FXML
    public Label UserName;
    @FXML
    public StackPane mainPane;
    @FXML
    public Pane searchResultBox;
    @FXML
    public ScrollPane pane;
    @FXML
    public TextField search;
    @FXML
    public GridPane Box;


    @FXML
    private TableView<Book> bookTable;
    @FXML
    private TableColumn<Book, Integer> IDColumn;
    @FXML
    private TableColumn<Book, String> isbnColumn;
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, String> categoryColumn;
    @FXML
    private TableColumn<Book, Integer> totalCopiesColumn;
    @FXML
    private TableColumn<Book, Integer> availableCopiesColumn;
    @FXML
    private TableColumn<Book, Void> changeBookInformationColumn;
    @FXML
    private TextField searchBookField;

    private final BookViewModel viewModel = new BookViewModel();
    private final BookDAO bookDAO = new BookDAO();
    private final List<Book> allBooks = bookDAO.getAllBooks();
    private final ObservableList<Book> books = FXCollections.observableArrayList();

    @FXML
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
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        IDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("authorName"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        totalCopiesColumn.setCellValueFactory(new PropertyValueFactory<>("totalCopies"));
        availableCopiesColumn.setCellValueFactory(new PropertyValueFactory<>("availableCopies"));

        titleColumn.setCellFactory(column -> new TableCell<>() {
            private final Text text = new Text();

            {
                text.wrappingWidthProperty().bind(titleColumn.widthProperty().subtract(10));
                setGraphic(text);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                text.setText(empty || item == null ? "" : item);
            }
        });

        authorColumn.setCellFactory(column -> new TableCell<>() {
            private final Text text = new Text();

            {
                text.wrappingWidthProperty().bind(authorColumn.widthProperty().subtract(10));
                setGraphic(text);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                text.setText(empty || item == null ? "" : item);
            }
        });

        categoryColumn.setCellFactory(column -> new TableCell<>() {
            private final Text text = new Text();

            {
                text.wrappingWidthProperty().bind(categoryColumn.widthProperty().subtract(10));
                setGraphic(text);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                text.setText(empty || item == null ? "" : item);
            }
        });

        FilteredList<Book> filteredBooks = new FilteredList<>(viewModel.getBooks(), b -> true);

        searchBookField.textProperty().addListener((obs, oldVal, newVal) -> {
            filteredBooks.setPredicate(book -> {
                if (newVal == null || newVal.isEmpty()) return true;
                String lower = newVal.toLowerCase();
                return (book.getTitle() != null && book.getTitle().toLowerCase().contains(lower)) ||
                        (book.getAuthorName() != null && book.getAuthorName().toLowerCase().contains(lower)) ||
                        (book.getCategoryName() != null && book.getCategoryName().toLowerCase().contains(lower)) ||
                        (book.getIsbn() != null && book.getIsbn().toLowerCase().contains(lower));
            });
        });
        addViewChangeColumn();
        loadBook();

        bookTable.setItems(filteredBooks);

        viewModel.loadBooks();

        User currentUser = SessionManager.getInstance().getLoggedInUser();
        UserName.setText(currentUser != null ? currentUser.getUsername() : "khong co nguoi dung");
    }

    @FXML
    private void backToMain() {
        SceneNavigator.backToMain(backToMain);
    }

    public void openMyAccount(ActionEvent event) throws IOException {
        viewModel.openMyAccount();
        loadView("my-account.fxml", myAccount);
    }

    public void addNewBook(ActionEvent event) throws IOException {
        viewModel.openAddBook();
        loadView("add-book-view.fxml", addBook);
    }

    public void goToBookManage() throws IOException {
        viewModel.openBookManage();
        loadView("bookmanagement-view.fxml", bookManage);
    }

    public void goToUserManagement() throws IOException {
        viewModel.openUserManage();
        loadView("Usersmanagement-view.fxml", userManagement);
    }

    public void goToAI() throws IOException {
        viewModel.openAI();
        loadView("AI-view.fxml", AI);
    }

    public void Logout() throws IOException {
        viewModel.logout();
        loadView("login-view.fxml", logout);
    }

    @FXML
    public void deleteSelectedBook() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();

        if (selectedBook != null) {
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirm Deletion");
            confirmationAlert.setHeaderText(null);
            confirmationAlert.setContentText("Are you sure you want to delete this book?");

            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try (Connection conn = DatabaseConnection.connect()) {
                        String sql = "DELETE FROM Book WHERE id = ?";
                        PreparedStatement pstmt = conn.prepareStatement(sql);
                        pstmt.setInt(1, selectedBook.getId());
                        pstmt.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    updateBookList();

                    showAlert(Alert.AlertType.INFORMATION, "Delete Successful", "Book deleted successfully!");
                }
            });

        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select book to delete.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void updateBookList() {
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();
        if (selectedBook != null) {
            viewModel.getBooks().remove(selectedBook);
        }
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
        SceneNavigator.loadSearchResult(search);
    }

    private void addViewChangeColumn() {
        changeBookInformationColumn.setCellFactory(param -> new TableCell<>() {
            private final Button viewBtn = new Button("View");

            {
                viewBtn.setStyle("-fx-cursor: hand;");
                viewBtn.setOnAction(event -> {
                    Book book = getTableView().getItems().get(getIndex());
                    if (book != null) {
                        openBookInformation(book);
                    } else {
                        System.out.println("khong co sach duoc chon");
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(5, viewBtn);
                    setGraphic(box);
                }
            }
        });
    }

    public void openBookInformation(Book book) {
        try {
            System.out.println("sach duoc chon la" + book.getTitle());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/libapp/view/changeBookInformation-view.fxml"));
            Parent root = loader.load();
            System.out.println("sach duoc chon la  " + book.getTitle());
            ChangeBookInformationController controller = loader.getController();
            controller.setBook(book);


            Stage stage = (Stage) myAccount.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void loadBook() {
        BookDAO bookDao = new BookDAO();
        books.setAll(bookDao.getAllBooks());
        bookTable.setItems(books);
    }
}
