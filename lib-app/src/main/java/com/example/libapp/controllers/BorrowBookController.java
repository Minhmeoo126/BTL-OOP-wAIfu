package com.example.libapp.controllers;

import com.example.libapp.viewmodel.BorrowBookViewModel;
import com.example.libapp.SessionManager;
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

public class BorrowBookController {
    public Button back;
    @FXML
    private Button borrow;
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

    @FXML
    private void backToMain() {
        SceneNavigator.backToMain(back);
    }
}