package com.example.libapp.controllers;

import com.example.libapp.model.Book;
import com.example.libapp.model.BorrowingRecord;
import com.example.libapp.model.User;
import com.example.libapp.utils.SceneNavigator;
import com.example.libapp.viewmodel.MyAccountViewModel;
import com.example.libapp.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Date;

import static com.example.libapp.utils.SceneNavigator.loadView;

public class MyAccountController {
    @FXML
    public Button AI;
    @FXML
    public Button addBook;
    @FXML
    public Button bookManage;
    @FXML
    public Button userManagement;
    @FXML
    public Button myAccount;
    @FXML
    public Button returnBook;
    @FXML
    public Button logout;
    @FXML
    public Label nameAccount;
    @FXML
    public Label Role;
    @FXML
    public Label BorrowedBooks;
    @FXML
    public Button borrowBooks;
    @FXML
    public TableView<BorrowingRecord> BorrowHistoryTable;
    public TableColumn<BorrowingRecord, Integer> IDColumn;
    public TableColumn<BorrowingRecord, String> nameBookColumn;
    public TableColumn<BorrowingRecord, String> AuthorColumn;
    public TableColumn<BorrowingRecord, String> borowDateColumn;
    public TableColumn<BorrowingRecord, String> returnDateColumn;
    @FXML
    private Button backToMain;

    private final MyAccountViewModel viewModel = new MyAccountViewModel();

    //@FXML
    //public void initialize() {
    //  usernameLabel.textProperty().bind(viewModel.usernameProperty());
    // emailLabel.textProperty().bind(viewModel.emailProperty());
    //fullNameLabel.textProperty().bind(viewModel.fullNameProperty());
    // roleLabel.textProperty().bind(viewModel.roleProperty());
    // viewModel.loadUserInfo(SessionManager.getInstance().getLoggedInUser());
    // }
    public void initialize() {
        User currentUser = SessionManager.getInstance().getLoggedInUser();
        if (currentUser != null) {
            nameAccount.setText("Name: " + currentUser.getUsername());
            Role.setText("Role: " + currentUser.getRole());
            BorrowedBooks.setText("Borrowed: " + "0");
        } else {
            nameAccount.setText("Không có người dùng");
            Role.setText("Không xác định");
            BorrowedBooks.setText("0");
        }
    }

    public void addNewBook(ActionEvent event) {

    }

    public void goToBookManage(ActionEvent event) throws IOException {
        viewModel.openBookMangagement();
        loadView("bookmanagement-view.fxml", bookManage);

    }

    public void goToUserManagement() throws IOException {
        viewModel.openUserManagement();
        loadView("Usersmanagement-view.fxml", userManagement);
    }

    public void openMyAccount() {
        try {
            User currentUser = SessionManager.getInstance().getLoggedInUser();
            String role = currentUser.getRole();
            if (role.equals("ADMIN")) {
                FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource("/com/example/libapp/view/my-account.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) myAccount.getScene().getWindow();
                stage.setScene(new Scene(root, 1100, 600));
                stage.show();
            } else {
                FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource("/com/example/libapp/view/User-my-account-view.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) myAccount.getScene().getWindow();
                stage.setScene(new Scene(root, 1100, 600));
                stage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToAI(ActionEvent event) throws IOException {
        try {
            User currentUser = SessionManager.getInstance().getLoggedInUser();
            String role = currentUser.getRole();
            if (role.equals("ADMIN")) {
                FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource("/com/example/libapp/view/AI-view.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) AI.getScene().getWindow();
                stage.setScene(new Scene(root, 1100, 600));
                stage.show();
            } else {
                FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource("/com/example/libapp/view/User-Ai-view.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) AI.getScene().getWindow();
                stage.setScene(new Scene(root, 1100, 600));
                stage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Logout() throws IOException {
        viewModel.logout();
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

    public void returnBooks() throws IOException {
        viewModel.openReturnBook();
        loadView("return-book.fxml", returnBook);
    }
}