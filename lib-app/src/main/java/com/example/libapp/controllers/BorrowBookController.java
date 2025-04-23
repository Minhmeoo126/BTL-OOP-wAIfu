package com.example.libapp.controllers;

import com.example.libapp.model.User;
import com.example.libapp.viewmodel.BorrowBookViewModel;
import com.example.libapp.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import com.example.libapp.utils.SceneNavigator;
import java.io.IOException;

import static com.example.libapp.utils.SceneNavigator.loadView;

public class BorrowBookController {
    public Button AI;
    public Button myAccount;
    public Button returnBook;
    public Button borrowBooks;
    public Button logout;
    public Button backToMain;
    @FXML
    private TextField bookIdField;
    @FXML
    private Label messageLabel;

    private final BorrowBookViewModel viewModel = new BorrowBookViewModel();

    @FXML
    public void initialize() {
        messageLabel.textProperty().bind(viewModel.messageProperty());
        viewModel.setLoggedInUser(SessionManager.getInstance().getLoggedInUser());
    }

    @FXML
    private void handleBorrow() {
        viewModel.borrowBook(bookIdField.getText());
        // Thêm hiệu ứng cho thông báo
        messageLabel.setStyle(viewModel.messageProperty().get().contains("successfully") ?
                "-fx-text-fill: green;" : "-fx-text-fill: red;");
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

    public void openReturnBook() throws IOException {
        viewModel.openReturnBook();
        loadView("return-book.fxml",returnBook);
    }

    public void openBorrowBook(ActionEvent event) throws IOException {
        viewModel.openBorrowBook();
        loadView("borrow-book.fxml",borrowBooks);
    }

    public void openAI() {
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
        viewModel.Logout();
        loadView("login-view.fxml" , logout);
    }

    public void backToMain(ActionEvent event) {
        SceneNavigator.backToMain(backToMain);
    }
}