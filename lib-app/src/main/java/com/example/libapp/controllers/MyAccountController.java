package com.example.libapp.controllers;

import com.example.libapp.SessionManager;
import com.example.libapp.model.Book;
import com.example.libapp.model.BorrowingRecord;
import com.example.libapp.model.User;
import com.example.libapp.persistence.BookDAO;
import com.example.libapp.persistence.DatabaseConnection;
import com.example.libapp.utils.BookGridPane;
import com.example.libapp.utils.SceneNavigator;
import com.example.libapp.utils.SearchFunction;
import com.example.libapp.viewmodel.MyAccountViewModel;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.example.libapp.utils.SceneNavigator.loadView;

public class MyAccountController {
    @FXML
    public Button AI, addBook, bookManage, userManagement, myAccount, returnBook, logout, borrowBooks, backToMain,change;
    @FXML
    public Label nameAccount, Role, BorrowedBooks, UserName;
    @FXML
    public StackPane mainPane;
    @FXML
    public Pane searchResultBox;
    @FXML
    public ScrollPane pane;
    @FXML
    public GridPane Box;
    @FXML
    public TextField search;

    @FXML public TableView<BorrowingRecord> BorrowHistoryTable;
    public TableColumn<BorrowingRecord, Integer> IDColumn;
    public TableColumn<BorrowingRecord, String> nameBookColumn, AuthorColumn, borrowDateColumn, returnDateColumn;

    private User user;

    private final MyAccountViewModel viewModel = new MyAccountViewModel();

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

        if (user == null) {
            user = SessionManager.getInstance().getLoggedInUser();  // Lấy user từ session
        }

        if (user != null) {
            nameAccount.setText("Name: " + user.getUsername());
            Role.setText("Role: " + user.getRole());
            UserName.setText(user.getUsername());
            viewModel.loadUserInfo(user);
        }

        // Setup cột bảng
        IDColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        nameBookColumn.setCellValueFactory(new PropertyValueFactory<>("BookName"));
        AuthorColumn.setCellValueFactory(new PropertyValueFactory<>("AuthorName"));
        borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));


        nameBookColumn.setCellFactory(column -> new TableCell<>() {
            private final Text text = new Text();

            {
                nameBookColumn.setPrefWidth(300);
                text.wrappingWidthProperty().bind(nameBookColumn.widthProperty().subtract(10));
                setGraphic(text);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                text.setText(empty || item == null ? "" : item);
            }
        });

        returnDateColumn.setCellFactory(column -> new TableCell<>() {
            private final Text text = new Text();

            {
                text.wrappingWidthProperty().bind(returnDateColumn.widthProperty().subtract(10));
                setGraphic(text);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                text.setText(empty || item == null ? "" : item);
            }
        });
        AuthorColumn.setCellFactory(column -> new TableCell<>() {
            private final Text text = new Text();

            {
                AuthorColumn.setPrefWidth(300);
                text.wrappingWidthProperty().bind(AuthorColumn.widthProperty().subtract(10));
                setGraphic(text);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                text.setText(empty || item == null ? "" : item);
            }
        });

        borrowDateColumn.setCellFactory(column -> new TableCell<>() {
            private final Text text = new Text();

            {
                text.wrappingWidthProperty().bind(borrowDateColumn.widthProperty().subtract(10));
                setGraphic(text);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                text.setText(empty || item == null ? "" : item);
            }
        });


        // Load dữ liệu
        loadBorrowHistory();
    }

    public void setUser(User user) {
        this.user = user;
        initialize();
    }

    private void loadBorrowHistory() {
        viewModel.loadHistory(user);
        BorrowHistoryTable.setItems(viewModel.getRecords());
        BorrowedBooks.setText("Borrowing: " + viewModel.getRecords().stream()
                .filter(record -> record.getReturnDate() == null || record.getReturnDate().isEmpty())
                .count() + "");
    }

    public void openMyAccount() {
        try {
            User currentUser = SessionManager.getInstance().getLoggedInUser();
            if (currentUser != null) {
                FXMLLoader loader;
                if (currentUser.getRole().equals("ADMIN")) {
                    loader = new FXMLLoader(SceneNavigator.class.getResource("/com/example/libapp/view/my-account.fxml"));
                } else {
                    loader = new FXMLLoader(SceneNavigator.class.getResource("/com/example/libapp/view/User-my-account-view.fxml"));
                }
                Parent root = loader.load();
                MyAccountController controller = loader.getController();
                controller.setUser(currentUser);
                Stage stage = (Stage) myAccount.getScene().getWindow();
                stage.setScene(new Scene(root, 1100, 600));
                stage.show();
            } else {
                System.out.println("User login not found!!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToAI(ActionEvent event) throws IOException {
        try {
            User currentUser = SessionManager.getInstance().getLoggedInUser();
            if (currentUser != null) {
                String role = currentUser.getRole();
                String fxml = role.equals("ADMIN") ? "/com/example/libapp/view/AI-view.fxml" : "/com/example/libapp/view/User-Ai-view.fxml";
                FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource(fxml));
                Parent root = loader.load();
                Stage stage = (Stage) AI.getScene().getWindow();
                stage.setScene(new Scene(root, 1100, 600));
                stage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    public void returnSelectedBook() {
        BorrowingRecord selectedRecord = BorrowHistoryTable.getSelectionModel().getSelectedItem();

        if (selectedRecord != null) {
            if (selectedRecord.getReturnDate() != null && !selectedRecord.getReturnDate().isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "Book Already Returned", "This book has already been returned.");
                return;
            }

            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirm Return");
            confirmationAlert.setHeaderText(null);
            confirmationAlert.setContentText("Are you sure you want to return this book?");

            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formattedNow = now.format(formatter);

                    try (Connection conn = DatabaseConnection.connect()) {
                        String sql = "UPDATE BorrowingRecord SET return_date = ? WHERE id = ?";
                        PreparedStatement pstmt = conn.prepareStatement(sql);
                        pstmt.setString(1, formattedNow);
                        pstmt.setInt(2, selectedRecord.getId());
                        pstmt.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    // Reload toàn bộ TableView để đồng bộ dữ liệu
                    loadBorrowHistory();

                    showAlert(Alert.AlertType.INFORMATION, "Return Successful", "Book returned successfully!");
                }
            });

        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a record to return.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void changeInformation() throws IOException {
        loadView("UserInformation-view.fxml" , myAccount);
    }

    public void Search() throws IOException {
        SearchFunction.Search(search,Box,searchResultBox);
    }

    public void addNewBook() throws IOException {
        viewModel.openAddBook();
        loadView("add-book-view.fxml", addBook);
    }

    public void goToBookManage(ActionEvent event) throws IOException {
        viewModel.openBookMangagement();
        loadView("bookmanagement-view.fxml", bookManage);
    }

    public void goToUserManagement() throws IOException {
        viewModel.openUserManagement();
        loadView("Usersmanagement-view.fxml", userManagement);
    }

    public void Logout() throws IOException {
        SessionManager.getInstance().clearSession();
        loadView("login-view.fxml", logout);
    }

    public void backToMain() {
        SceneNavigator.backToMain(backToMain);
    }

    public void openReturnBook() throws IOException {
        viewModel.openReturnBook();
        loadView("return-book.fxml", returnBook);
    }

    public void openBorrowBook() throws IOException {
        viewModel.openBorrowBook();
        loadView("borrow-book.fxml", borrowBooks);
    }


}
