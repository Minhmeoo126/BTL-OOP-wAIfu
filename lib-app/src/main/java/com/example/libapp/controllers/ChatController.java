package com.example.libapp.controllers;

import com.example.libapp.viewmodel.GPTViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class ChatController {
    @FXML
    private TextArea chatArea;
    @FXML
    private TextField promptField;

    private GPTViewModel viewModel;

    public void initialize() {
        viewModel = new GPTViewModel();
        promptField.textProperty().bindBidirectional(viewModel.promptProperty());
        viewModel.responseProperty().addListener((obs, oldValue, newValue) -> {
            chatArea.appendText("GPT: " + newValue + "\n\n");
        });
    }

    @FXML
    private void handleSend() {
        String prompt = promptField.getText();
        if (prompt != null && !prompt.trim().isEmpty()) {
            chatArea.appendText("Bạn: " + prompt + "\n");
            viewModel.getGPTResponse();
            promptField.clear();
        }
    }
}