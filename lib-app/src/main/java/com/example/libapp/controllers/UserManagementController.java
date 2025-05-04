package com.example.libapp.controllers;

import com.example.libapp.SessionManager;
import com.example.libapp.model.Book;
import com.example.libapp.model.User;
import com.example.libapp.persistence.BookDAO;
import com.example.libapp.persistence.UserDAO;
import com.example.libapp.utils.BookGridPane;
import com.example.libapp.utils.SceneNavigator;
import com.example.libapp.utils.SearchFunction;
import com.example.libapp.viewmodel.UserViewModel;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;

import static com.example.libapp.utils.SceneNavigator.loadView;

public class UserManagementController {
    @FXML
    public StackPane mainPane;
    @FXML
    public Pane searchResultBox;
    @FXML
    public ScrollPane pane;
    @FXML
    public GridPane Box;
    @FXML
    private Button AI, myAccount, addBook, bookManage, userManagement, logout , backToMain, deleteUser;
    @FXML
    private Label UserName;
    @FXML
    private TextField usernameField, emailField, fullNameField, searchField , search;
    @FXML
    private Label messageLabel;


    @FXML private TableView<User> UsersTable;
    @FXML private TableColumn<User, String> nameAccountColumn;
    @FXML private TableColumn<User, String> PassWordColumn;
    @FXML private TableColumn<User, String> EmailColumn;
    @FXML private TableColumn<User, String> fullNameColumn;
    @FXML private TableColumn<User, Void> actionColumn;

    private final ObservableList<User> users = FXCollections.observableArrayList();
    private User selectedUser;

    private final UserViewModel viewModel = new UserViewModel();
    private final BookDAO bookDAO = new BookDAO();
    private final List<Book> allBooks = bookDAO.getAllBooks();


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
        nameAccountColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        PassWordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        EmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        addViewHistoryColumn();
        loadUser();
        messageLabel.textProperty().bind(viewModel.messageProperty());

        User currentUser = SessionManager.getInstance().getLoggedInUser();
        UserName.setText(currentUser != null ? currentUser.getUsername() : "Khong co nguoi dung");

        searchField.textProperty().addListener((observable, oldValue, newValue) -> filterUsers(newValue));
    }

    private void loadUser() {
        UserDAO dao = new UserDAO();
        users.setAll(dao.getAllUsers());
        UsersTable.setItems(users);
    }

    private void addViewHistoryColumn() {
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button viewBtn = new Button("View");
            {
                viewBtn.setStyle("-fx-cursor: hand;");
                viewBtn.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    openUserHistory(user);
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

    private void openUserHistory(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/libapp/view/User-cell-view.fxml"));
            Parent root = loader.load();

            UserInformationController controller = loader.getController();
            controller.setUser(user);

            Stage stage = (Stage) myAccount.getScene().getWindow();
            stage.setTitle("User History - " + user.getUsername());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAddUser() {
        viewModel.addUser(
                usernameField.getText().trim(),
                "password123",
                "USER",
                emailField.getText().trim(),
                fullNameField.getText().trim()
        );
        messageLabel.setStyle(viewModel.messageProperty().get().contains("successfully") ?
                "-fx-text-fill: green;" : "-fx-text-fill: red;");
    }

    @FXML
    private void backToMain() {
        SceneNavigator.backToMain(backToMain);
    }

    @FXML
    public void openMyAccount() throws IOException {
        User currentUser = SessionManager.getInstance().getLoggedInUser();
        if (currentUser != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/libapp/view/my-account.fxml"));
            Parent root = loader.load();

            MyAccountController controller = loader.getController();
            controller.setUser(currentUser);

            Stage stage = (Stage) myAccount.getScene().getWindow();
            stage.setTitle("My Account - " + currentUser.getUsername());
            stage.setScene(new Scene(root));
            stage.show();
        }
    }

    public void addNewBook() throws IOException {
        viewModel.openAddBook();
        loadView("add-book-view.fxml", addBook);
    }

    public void goToBookManage() throws IOException {
        viewModel.openBookManage();
        loadView("bookmanagement-view.fxml", bookManage);
    }

    @FXML
    private void handleDeleteUser(ActionEvent event) {
        User user = UsersTable.getSelectionModel().getSelectedItem();
        if (user == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Không có người dùng được chọn");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng chọn một người dùng để xóa.");
            alert.showAndWait();
            return;
        }

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Xác nhận xóa");
        confirmationAlert.setHeaderText("Bạn có chắc chắn muốn xóa người dùng này?");
        confirmationAlert.setContentText("Tài khoản: " + user.getUsername());

        ButtonType yesBtn = new ButtonType("Xóa", ButtonBar.ButtonData.YES);
        ButtonType noBtn = new ButtonType("Hủy", ButtonBar.ButtonData.NO);

        confirmationAlert.getButtonTypes().setAll(yesBtn, noBtn);

        confirmationAlert.showAndWait().ifPresent(response -> {
            if (response == yesBtn) {
                UserDAO dao = new UserDAO();
                boolean success = dao.deleteUser(user.getId());
                if (success) {
                    users.remove(user);
                    Alert info = new Alert(Alert.AlertType.INFORMATION);
                    info.setTitle("Thành công");
                    info.setHeaderText(null);
                    info.setContentText("Đã xóa người dùng thành công.");
                    info.showAndWait();
                } else {
                    Alert error = new Alert(Alert.AlertType.ERROR);
                    error.setTitle("Lỗi");
                    error.setHeaderText(null);
                    error.setContentText("Không thể xóa người dùng.");
                    error.showAndWait();
                }
            }
        });
    }

    @FXML
    private void handleViewAccount(User selectedUser) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/libapp/view/my-account.fxml"));
            Parent root = loader.load();
            MyAccountController controller = loader.getController();
            controller.setUser(selectedUser);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Account Detail - " + selectedUser.getUsername());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToUserManagement() throws IOException {
        viewModel.openUserManagement();
        loadView("Usersmanagement-view.fxml", userManagement);
    }

    public void goToAI() throws IOException {
        viewModel.openAI();
        loadView("AI-view.fxml", AI);
    }

    public void Logout() throws IOException {
        viewModel.Logout();
        loadView("login-view.fxml", logout);
    }

    private void filterUsers(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            UsersTable.setItems(users);
            addViewHistoryColumn();  // <-- Re-apply to fix lost buttons
            UsersTable.refresh();
            return;
        }

        ObservableList<User> filtered = FXCollections.observableArrayList();

        for (User user : users) {
            if (containsIgnoreCase(user.getUsername(), keyword) ||
                    containsIgnoreCase(user.getPassword(), keyword) ||
                    containsIgnoreCase(user.getEmail(), keyword) ||
                    containsIgnoreCase(user.getFullName(), keyword)) {
                filtered.add(user);
            }
        }

        UsersTable.setItems(filtered);
        addViewHistoryColumn();
        UsersTable.refresh();
    }


    private boolean containsIgnoreCase(String source, String target) {
        return source != null && source.toLowerCase().contains(target.toLowerCase());
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
}
