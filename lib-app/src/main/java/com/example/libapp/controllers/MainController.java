package com.example.libapp.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainController {
    @FXML
    private void handleBookView() throws IOException {
        loadView("book-view.fxml");
    }

    @FXML
    private void handleBorrowView() throws IOException {
        loadView("borrow-view.fxml");
    }

    @FXML
    private void handleUserView() throws IOException {
        loadView("user-view.fxml");
    }

    @FXML
    private void handleLoginView() throws IOException {
        loadView("login-view.fxml");
    }

    @FXML
    private void handleChatView() throws IOException {
        loadView("chat-view.fxml");
    }

    private void loadView(String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/libapp/view/" + fxmlFile));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
}