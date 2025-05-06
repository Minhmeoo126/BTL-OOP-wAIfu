package com.example.libapp.controllers;

import com.example.libapp.SessionManager;
import com.example.libapp.api.AIService;
import com.example.libapp.model.Book;
import com.example.libapp.model.User;
import com.example.libapp.persistence.BookDAO;
import com.example.libapp.utils.BookGridPane;
import com.example.libapp.utils.SceneNavigator;
import com.example.libapp.utils.SearchFunction;
import com.example.libapp.viewmodel.MainViewModel;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import static com.example.libapp.utils.SceneNavigator.loadView;

public class AIController {

    @FXML
    public Button AI,myAccount,addBook,bookManage,userManagement,logout,backToMain,sendButton;
    @FXML
    public Label UserName;
    @FXML
    public StackPane mainPane;
    @FXML
    public Pane searchResultBox;
    @FXML
    public ScrollPane pane;
    @FXML
    public GridPane Box;
    @FXML
    private TextField promptField, search;
    @FXML
    private ScrollPane chatScroll;
    @FXML
    private VBox chatBox;

    private AIService aiService;

    private final MainViewModel viewModel = new MainViewModel();

    public void initialize() {
        pane.setMaxWidth(400);
        pane.setMaxHeight(400);
        searchResultBox.setVisible(false);

        // Định vị searchResultBox ngay dưới TextField khi focus
        search.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                searchResultBox.setLayoutX(400);
                searchResultBox.setLayoutY(50);
                searchResultBox.setPrefWidth(415.0);
                searchResultBox.setPrefHeight(150.0);

                searchResultBox.setVisible(true);
                searchResultBox.toFront();
                System.out.println("SearchBar focused: true → searchResultBox visible at X: " + searchResultBox.getLayoutX() + ", Y: " + searchResultBox.getLayoutY());
            } else {
                PauseTransition pause = new PauseTransition(Duration.millis(150));
                pause.setOnFinished(event -> {
                    if (!pane.isHover() && !search.isFocused()) {
                        searchResultBox.setVisible(false);
                        System.out.println("SearchBar lost focus + searchResultBox not hovered → searchResultBox hidden");
                    }
                });
                pause.play();
            }
        });

        // Thay đổi: Thêm sự kiện mousePressed cho searchResultBox để cho phép nhấp xuyên qua nó
        searchResultBox.setPickOnBounds(false);

        // Thay đổi: Cập nhật logic mousePressed trên mainPane để đảm bảo BorderPane có thể tương tác
        mainPane.setOnMousePressed(event -> {
            if (!search.isFocused()) return;
            search.getParent().requestFocus();// ép search mất focus
            searchResultBox.setVisible(false);
            searchResultBox.toBack();
            search.setText("");
        });

        // Gọi Search khi nội dung TextField thay đổi
        search.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                Search();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        User currentUser = SessionManager.getInstance().getLoggedInUser();
        if (currentUser != null) {
            UserName.setText(currentUser.getUsername());
            aiService = new AIService();

            sendButton.setOnAction(event -> handleSendMessage());
            promptField.setOnAction(event -> handleSendMessage());

            // Đảm bảo chatBox mở rộng theo nội dung
            chatBox.setAlignment(Pos.TOP_LEFT);
            chatBox.setSpacing(10);
        } else {
            UserName.setText("Không có người dùng");
        }
    }

    public void openMyAccount() throws IOException {
        viewModel.openMyAccount();
        loadView("my-account.fxml", myAccount);
    }

    public void addNewBook() throws IOException {
        viewModel.openAddBook();
        loadView("add-book-view.fxml", addBook);
    }

    public void goToBookManage() throws IOException {
        viewModel.openBookManagement();
        loadView("bookmanagement-view.fxml", bookManage);
    }

    public void goToUserManagement() throws IOException {
        viewModel.openUserManagement();
        loadView("Usersmanagement-view.fxml", userManagement);
    }

    public void Logout() throws IOException {
        viewModel.logout();
        loadView("login-view.fxml", logout);
    }

    public void backToMain(ActionEvent event) {
        SceneNavigator.backToMain(backToMain);
    }

    public void goToAI() throws IOException {
        loadView("AI-view.fxml" ,AI);
    }

    public void Search() throws IOException {
        SearchFunction.Search(search,Box,searchResultBox);
    }

    @FXML
    private void handleSendMessage() {
        String prompt = promptField.getText().trim();
        if (!prompt.isEmpty()) {
            addMessageWithTypingEffect(prompt, false); // Hiển thị ngay lập tức
            promptField.clear();

            User currentUser = SessionManager.getInstance().getLoggedInUser();
            if (currentUser != null) {
                new Thread(() -> {
                    String aiResponse = aiService.getAIResponse(currentUser.getId(), prompt);
                    System.out.println("AI Response: " + aiResponse);
                    Platform.runLater(() -> addMessageWithTypingEffect(aiResponse, true)); // gọi lại trên UI thread
                }).start(); //  Chạy AI ở luồng khác
            }
        }
    }

    private void addMessageWithTypingEffect(String message, boolean isAI) {
        List<String> messageLines = splitMessageIntoLines(message, 50);
        List<Label> messageLabels = new ArrayList<>();
        for (String line : messageLines) {
            Label label = new Label();
            label.setFont(Font.font("Arial", 18));
            messageLabels.add(label);
        }

        VBox textContainer = new VBox(2);
        textContainer.getChildren().addAll(messageLabels);
        textContainer.setPadding(new Insets(10));
        textContainer.setStyle("-fx-background-color: " + (isAI ? "#e0f7fa" : "#c8e6c9") + "; -fx-background-radius: 10;");

        ImageView avatar;
        if (isAI) {
            avatar = new ImageView(new Image(getClass().getResourceAsStream("/com/example/libapp/image/Rice_ava.png")));
        } else {
            avatar = new ImageView(new Image(getClass().getResourceAsStream("/com/example/libapp/image/user.png")));
        }
        avatar.setFitHeight(45);
        avatar.setFitWidth(45);

        HBox messageBox;
        if (isAI) {
            messageBox = new HBox(10, avatar, textContainer);
            messageBox.setAlignment(Pos.CENTER_LEFT);
        } else {
            messageBox = new HBox(10, textContainer, avatar);
            messageBox.setAlignment(Pos.CENTER_RIGHT);
        }
        messageBox.setPadding(new Insets(10));

        // Thêm tin nhắn vào chatBox
        Platform.runLater(() -> chatBox.getChildren().add(messageBox));

        // Hiệu ứng gõ chữ
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
                    currentLabel.setText(currentLine);
                    lineIndex[0]++;
                    charIndex[0] = 0;
                }
                Platform.runLater(() -> {
                    chatScroll.layout();
                    chatScroll.setVvalue(1.0);
                });
            }
        }));

        timeline.setCycleCount(message.length());
        timeline.setOnFinished(event -> {
            // Cuộn xuống dưới sau khi hiệu ứng gõ chữ hoàn tất
            Platform.runLater(() -> {
                chatScroll.layout();
                chatScroll.setVvalue(1.0);
            });
        });
        timeline.play();
    }

    private List<String> splitMessageIntoLines(String message, int maxLength) {
        List<String> lines = new ArrayList<>();
        StringBuilder currentLine = new StringBuilder();

        for (int i = 0; i < message.length(); i++) {
            currentLine.append(message.charAt(i));
            if (currentLine.length() >= maxLength && (message.charAt(i) == ' ' || i == message.length() - 1)) {
                lines.add(currentLine.toString().trim());
                currentLine = new StringBuilder();
            }
        }

        if (!currentLine.isEmpty()) {
            lines.add(currentLine.toString().trim());
        }

        return lines;
    }

}