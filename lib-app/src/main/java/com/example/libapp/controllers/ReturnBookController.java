package com.example.libapp.controllers;

import com.example.libapp.utils.SceneNavigator;
import com.example.libapp.viewmodel.ReturnBookViewModel;
import com.example.libapp.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class ReturnBookController {
    public Button back;
    @FXML
    private TextField recordIdField;
    @FXML
    private Label messageLabel;
    @FXML
    private final ReturnBookViewModel viewModel = new ReturnBookViewModel();

    @FXML
    public void initialize() {
        messageLabel.textProperty().bind(viewModel.messageProperty());
        viewModel.setLoggedInUser(SessionManager.getInstance().getLoggedInUser());
    }

    @FXML
    private void handleReturn() {
        viewModel.returnBook(recordIdField.getText());
        messageLabel.setStyle(viewModel.messageProperty().get().contains("successfully") ?
                "-fx-text-fill: green;" : "-fx-text-fill: red;");
    }

    @FXML
    private void backToMain() {
        SceneNavigator.backToMain(back);
    }
}