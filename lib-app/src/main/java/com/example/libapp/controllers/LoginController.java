package com.example.libapp.controllers;

import com.example.libapp.model.User;
import com.example.libapp.viewmodel.LoginViewModel;
import com.example.libapp.SessionManager;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import java.io.IOException;

public class LoginController {
    @FXML
    private TextField adminUsername;
    @FXML
    private PasswordField adminPassword;
    @FXML
    private TextField userUsername;
    @FXML
    private PasswordField userPassword;
    @FXML
    private Label loginlabel;
    @FXML
    private Label userloginlabel;
    @FXML
    private TabPane tabPaneLogin;
    @FXML
    private Tab tabAdmin;
    @FXML
    private Tab tabUser;
    @FXML
    private Label lblAdmin;
    @FXML
    private Label lblUser;
    @FXML
    private Label lblStatus;
    @FXML
    private Pane slidingPane;

    private final LoginViewModel viewModel = new LoginViewModel();

    @FXML
    public void initialize() {
        loginlabel.textProperty().bind(viewModel.messageProperty());
        userloginlabel.textProperty().bind(viewModel.messageProperty());
    }

    @FXML
    void openAdminTab(MouseEvent event) {
        TranslateTransition transition = new TranslateTransition(javafx.util.Duration.millis(400), lblStatus);
        transition.setToX(0);
        transition.play();
        transition.setOnFinished(e -> lblStatus.setText("ADMINISTRATOR"));
        tabPaneLogin.getSelectionModel().select(tabAdmin);
    }

    @FXML
    void openUserTab(MouseEvent event) {
        TranslateTransition transition = new TranslateTransition(javafx.util.Duration.millis(400), lblStatus);
        transition.setToX(200);
        transition.play();
        transition.setOnFinished(event1 -> lblStatus.setText("FOR USER"));
        tabPaneLogin.getSelectionModel().select(tabUser);
    }

    @FXML
    public void adminlogin(ActionEvent event) {
        User user = viewModel.login(adminUsername.getText(), adminPassword.getText(), "ADMIN");
        if (user != null) {
            SessionManager.getInstance().setLoggedInUser(user);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/libapp/view/main-admin-view.fxml"));
                if (loader.getLocation() == null) {
                    viewModel.messageProperty().set("Error: main.fxml not found");
                    return;
                }
                Parent root = loader.load();
                Stage stage = (Stage) adminUsername.getScene().getWindow();
                stage.setScene(new Scene(root, 1100, 600));
                stage.setTitle("Library App");
                stage.show();
            } catch (IOException e) {
                viewModel.messageProperty().set("Error loading main view: " + e.getMessage());
            }
        }
    }

    @FXML
    public void userlogin(ActionEvent event) {
        User user = viewModel.login(userUsername.getText(), userPassword.getText(), "USER");
        if (user != null) {
            SessionManager.getInstance().setLoggedInUser(user);
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/libapp/view/main.fxml"));
                if (loader.getLocation() == null) {
                    viewModel.messageProperty().set("Error: main.fxml not found");
                    return;
                }
                Parent root = loader.load();
                Stage stage = (Stage) userUsername.getScene().getWindow();
                stage.setScene(new Scene(root, 1100, 600));
                stage.setTitle("Library App");
                stage.show();
            } catch (IOException e) {
                System.err.println("Lỗi khi load Mainview: " + e.getMessage());
                viewModel.messageProperty().set("Error loading main view: " + e.getMessage());
            }
        }
    }

    @FXML
    public void GoToRegister(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/libapp/view/CreateAccountView.fxml"));
            if (loader.getLocation() == null) {
                viewModel.messageProperty().set("Error: CreateAccountView.fxml not found");
                return;
            }
            Parent root = loader.load();
            Stage stage = (Stage) userUsername.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 600));
            stage.setTitle("Register");
            stage.show();
        } catch (IOException e) {
            viewModel.messageProperty().set("Error loading register view: " + e.getMessage());
        }
    }
}