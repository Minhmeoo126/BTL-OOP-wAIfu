package com.example.libapp.controllers;

import com.example.libapp.viewmodel.MyAccountViewModel;
import com.example.libapp.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MyAccountController {
    @FXML
    private Label usernameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label fullNameLabel;
    @FXML
    private Label roleLabel;

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
        Stage stage = (Stage) usernameLabel.getScene().getWindow();
        stage.close();
    }
}