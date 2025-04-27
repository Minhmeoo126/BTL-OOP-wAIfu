package com.example.libapp.controllers;

import com.example.libapp.SessionManager;
import com.example.libapp.model.User;
import com.example.libapp.persistence.UserDAO;
import com.example.libapp.utils.SceneNavigator;
import com.example.libapp.viewmodel.UserViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

import static com.example.libapp.utils.SceneNavigator.loadView;

public class UserManagementController {
    @FXML
    private Button AI;
    @FXML
    private Button myAccount;
    @FXML
    private Button addBook;
    @FXML
    private Button bookManage;
    @FXML
    private Button userManagement;
    @FXML
    private Button logout;
    @FXML
    private Label UserName;

    @FXML private TableView<User> UsersTable;
    @FXML private TableColumn<User, String> nameAccountColumn;
    @FXML private TableColumn<User, String> PassWordColumn;
    @FXML private TableColumn<User, String> EmailColumn;
    @FXML private TableColumn<User, String> fullNameColumn;
    @FXML private TableColumn<User, Void> actionColumn;

    private final ObservableList<User> users = FXCollections.observableArrayList();

    @FXML private Button backToMain;
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private TextField fullNameField;
    @FXML private Label messageLabel;

    private final UserViewModel viewModel = new UserViewModel();
    private User selectedUser;

    @FXML
    public void initialize() {
        nameAccountColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        PassWordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        EmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        addViewHistoryColumn();  // Thêm cột chứa nút "View History"

        loadUser();
        messageLabel.textProperty().bind(viewModel.messageProperty());

        User currentUser = SessionManager.getInstance().getLoggedInUser();
        UserName.setText(currentUser != null ? currentUser.getUsername() : "khong co nguoi dung");
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
                    User user = getTableView().getItems().get(getIndex()); // Lấy người dùng của dòng hiện tại
                    openUserHistory(user); // Truyền đúng user vào phương thức mở lịch sử
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
            // Mở cửa sổ lịch sử người dùng với thông tin đúng
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/libapp/view/UserInformation-view.fxml"));
            Parent root = loader.load();

            UserInformationController controller = loader.getController();
            controller.setUser(user);  // Truyền đúng user vào controller

            Stage stage = (Stage) myAccount.getScene().getWindow();
            stage.setTitle("User History - " + user.getUsername());  // Hiển thị đúng tên tài khoản
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
            controller.setUser(currentUser);  // Truyền user hiện tại vào MyAccountController

            Stage stage = (Stage) myAccount.getScene().getWindow();
            stage.setTitle("My Account - " + currentUser.getUsername());
            stage.setScene(new Scene(root));
            stage.show();
        }
    }

    public void addNewBook() throws IOException {
        // Tùy chọn: logic thêm sách
        viewModel.openAddBook();
        loadView("add-book-view.fxml",addBook);
    }

    public void goToBookManage() throws IOException {
        viewModel.openBookManage();
        loadView("bookmanagement-view.fxml", bookManage);
    }

    @FXML
    private void handleViewAccount(User selectedUser) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/libapp/view/my-account.fxml"));
            Parent root = loader.load();
            MyAccountController controller = loader.getController();
            controller.setUser(selectedUser);  // Truyền user được chọn từ bảng

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
}
