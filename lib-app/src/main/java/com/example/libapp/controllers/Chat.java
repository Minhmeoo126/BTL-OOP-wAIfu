package com.example.libapp.controllers;

import com.example.libapp.viewmodel.GPTViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Chat {
    @FXML
    private TextField inputField;
    @FXML
    private TextArea chatArea;

    private GPTViewModel gptViewModel = new GPTViewModel();

    @FXML
    private void handleSend() {
        String input = inputField.getText();
        if (input.trim().isEmpty()) {
            chatArea.appendText("Vui lòng nhập yêu cầu gợi ý sách!\n");
            return;
        }

        // Thêm hướng dẫn để GPT hiểu là gợi ý sách
        String prompt = "Gợi ý một số sách dựa trên yêu cầu sau: " + input + ". Chỉ trả lời danh sách sách, không giải thích.";
        String response = gptViewModel.getGPTResponse(prompt);
        chatArea.appendText("Bạn: " + input + "\nChatbot: " + response + "\n\n");
        inputField.clear();
    }

    @FXML
    public void initialize() {
        chatArea.appendText("Chào bạn! Tôi là chatbot gợi ý sách. Hãy nhập yêu cầu (ví dụ: 'sách khoa học viễn tưởng' hoặc 'sách lãng mạn').\n\n");
    }
}