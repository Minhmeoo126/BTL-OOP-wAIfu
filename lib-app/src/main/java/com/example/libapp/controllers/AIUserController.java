package com.example.libapp.controllers;

import com.example.libapp.SessionManager;
import com.example.libapp.api.AIService;
import com.example.libapp.model.User;
import com.example.libapp.utils.SceneNavigator;
import com.example.libapp.viewmodel.MainViewModel;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.libapp.utils.SceneNavigator.loadView;


public class AIUserController {
    @FXML
    public Button AI;
    @FXML
    public Button myAccount;
    @FXML
    public Button addBook;
    @FXML
    public Button userManagement;
    @FXML
    public Button logout;
    @FXML
    public Button backToMain;
    @FXML
    public Button returnBook;
    @FXML
    public Button borrowBooks;
    @FXML
    public Label UserName;
    @FXML
    private TextField promptField;

    @FXML
    private Button sendButton;

    @FXML
    private VBox chatBox;

    private AIService aiService;

    private final MainViewModel viewModel = new MainViewModel();

    public void initialize() {
        User currentUser = SessionManager.getInstance().getLoggedInUser();
        if (currentUser != null) {
            UserName.setText(currentUser.getUsername());
            aiService = new AIService(); // Khởi tạo AIService

            sendButton.setOnAction(event -> handleSendMessage());
            promptField.setOnAction(event -> handleSendMessage());
        } else {
            UserName.setText("khong co nguoi dung");
        }
    }

    public void openMyAccount() throws IOException {
        viewModel.openMyAccount();
        loadView("User-my-account-view.fxml", myAccount);
    }

    public void Logout() throws IOException {
        viewModel.logout();
        loadView("login-view.fxml", logout);
    }

    public void backToMain(ActionEvent event) {
        SceneNavigator.backToMain(backToMain);
    }

    public void openReturnBook() throws IOException {
        viewModel.openReturnBook();
        loadView("return-book.fxml", returnBook);
    }

    public void openBorrowBook() throws IOException {
        viewModel.openBorrowBook();
        loadView("borrow-book.fxml", borrowBooks);
    }

    public void openAI() throws IOException {
        viewModel.openAI();
        loadView("User-Ai-view.fxml", AI);
    }

    @FXML
    private void handleSendMessage() {
        String prompt = promptField.getText().trim();
        if (!prompt.isEmpty()) {
            // Thêm tin nhắn người dùng
            addMessageWithTypingEffect(prompt, false);  // false vì đây là tin nhắn người dùng

            promptField.clear();

            User currentUser = SessionManager.getInstance().getLoggedInUser();
            if (currentUser != null) {
                // Lấy phản hồi từ AI
                String aiResponse = aiService.getAIResponse(currentUser.getId(), prompt);
                System.out.println("AI Response: " + aiResponse);  // In ra phản hồi của AI

                // Thêm phản hồi AI
                addMessageWithTypingEffect(aiResponse, true);  // true vì đây là phản hồi từ AI
            }
        }
    }

    private void addMessageWithTypingEffect(String message, boolean isAI) {
        // Chia nhỏ tin nhắn thành các đoạn, mỗi đoạn tối đa 50 ký tự
        List<String> messageLines = splitMessageIntoLines(message, 50);

        // Tạo danh sách các Label cho từng dòng
        List<Label> messageLabels = new ArrayList<>();
        for (String line : messageLines) {
            Label label = new Label();
            label.setFont(Font.font("Californian FB", 18));
            messageLabels.add(label);
        }

        // Tạo một VBox để chứa các Label (tạo hiệu ứng xuống dòng)
        VBox textContainer = new VBox(2); // Khoảng cách giữa các dòng là 2
        textContainer.getChildren().addAll(messageLabels);
        textContainer.setPadding(new Insets(10));
        textContainer.setStyle("-fx-background-color: " + (isAI ? "#e0f7fa" : "#c8e6c9") + "; -fx-background-radius: 10;"); // Tùy chỉnh nền

        // Chọn avatar tùy thuộc vào việc tin nhắn là của AI hay người dùng
        ImageView avatar;
        if (isAI) {
            avatar = new ImageView(new Image(getClass().getResourceAsStream("/com/example/libapp/image/Rice_ava.png")));
        } else {
            avatar = new ImageView(new Image(getClass().getResourceAsStream("/com/example/libapp/image/user.png")));
        }
        avatar.setFitHeight(45);
        avatar.setFitWidth(45);

        // Tạo HBox để chứa avatar và textContainer
        HBox messageBox;
        if (isAI) {
            // AI: Avatar bên trái, nội dung bên phải
            messageBox = new HBox(10, avatar, textContainer);
            messageBox.setAlignment(Pos.CENTER_LEFT);
        } else {
            // Người dùng: Nội dung bên trái, avatar bên phải
            messageBox = new HBox(10, textContainer, avatar);
            messageBox.setAlignment(Pos.CENTER_RIGHT);
        }
        messageBox.setPadding(new Insets(10));

        // Thêm vào chatBox
        Platform.runLater(() -> {
            chatBox.getChildren().add(messageBox);
            // Tự động cuộn xuống tin nhắn mới nhất
            if (chatBox.getParent() instanceof ScrollPane scrollPane) {
                scrollPane.setVvalue(1.0);
            }
        });

        // Hiệu ứng gõ chữ cho từng Label
        final int[] lineIndex = {0};
        final int[] charIndex = {0};
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(25), event -> {
            if (lineIndex[0] < messageLines.size()) {
                Label currentLabel = messageLabels.get(lineIndex[0]);
                String currentLine = messageLines.get(lineIndex[0]);

                if (charIndex[0] < currentLine.length()) {
                    currentLabel.setText(currentLine.substring(0, charIndex[0] + 1));
                    charIndex[0]++;
                } else {
                    // Hoàn thành dòng hiện tại, chuyển sang dòng tiếp theo
                    currentLabel.setText(currentLine);
                    lineIndex[0]++;
                    charIndex[0] = 0;
                }
            }
        }));

        timeline.setCycleCount(message.length());
        timeline.play();
    }

    // Hàm chia nhỏ tin nhắn thành các dòng, mỗi dòng tối đa maxLength ký tự
    private List<String> splitMessageIntoLines(String message, int maxLength) {
        List<String> lines = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder();

        for (int i = 0; i < message.length(); i++) {
            currentLine.append(message.charAt(i));

            // Nếu đạt đến maxLength, thêm dòng vào danh sách và reset currentLine
            if (currentLine.length() >= maxLength && (message.charAt(i) == ' ' || i == message.length() - 1)) {
                lines.add(currentLine.toString().trim());
                currentLine = new StringBuilder();
            }
        }

        // Thêm dòng cuối cùng nếu còn nội dung
        if (!currentLine.isEmpty()) {
            lines.add(currentLine.toString().trim());
        }

        return lines;
    }
}

