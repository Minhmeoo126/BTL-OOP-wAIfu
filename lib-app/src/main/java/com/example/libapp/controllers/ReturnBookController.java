package com.example.libapp.controllers;

import com.example.libapp.viewmodel.ReturnBookViewModel;
import com.example.libapp.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ReturnBookController {
    @FXML
    private TextField recordIdField;
    @FXML
    private Label messageLabel;

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
        Stage stage = (Stage) recordIdField.getScene().getWindow();
        stage.close();
    }
}