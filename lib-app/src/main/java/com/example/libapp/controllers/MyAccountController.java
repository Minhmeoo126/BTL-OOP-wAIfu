package com.example.libapp.controllers;

import com.example.libapp.viewmodel.MyAccountViewModel;
import com.example.libapp.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class MyAccountController {
    @FXML
    private Label usernameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label fullNameLabel;
    @FXML
    private Label roleLabel;
    @FXML
    private Button backToMain;
    private final MyAccountViewModel viewModel = new MyAccountViewModel();

    @FXML
    public void initialize() {
        usernameLabel.textProperty().bind(viewModel.usernameProperty());
        emailLabel.textProperty().bind(viewModel.emailProperty());
        fullNameLabel.textProperty().bind(viewModel.fullNameProperty());
        roleLabel.textProperty().bind(viewModel.roleProperty());

        viewModel.loadUserInfo(SessionManager.getInstance().getLoggedInUser());
    }

    @FXML
    private void backToMain() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/libapp/view/main.fxml" ));
            Parent root = loader.load();
            Stage stage = (Stage) backToMain.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 600));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}