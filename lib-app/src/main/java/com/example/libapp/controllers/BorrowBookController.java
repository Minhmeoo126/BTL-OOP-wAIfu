package com.example.libapp.controllers;

import com.example.libapp.model.Book;
import com.example.libapp.model.User;
import com.example.libapp.persistence.BookDAO;
import com.example.libapp.utils.BookGridPane;
import com.example.libapp.utils.SearchFunction;
import com.example.libapp.viewmodel.BorrowBookViewModel;
import com.example.libapp.SessionManager;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import com.example.libapp.utils.SceneNavigator;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;

import static com.example.libapp.utils.SceneNavigator.loadView;

public class BorrowBookController {
    @FXML
    public Button AI, myAccount, returnBook, borrowBooks, logout, backToMain;
    @FXML
    public Label UserName, label, messageLabel;
    @FXML
    public TextField bookNameField,search, bookIdField;
    @FXML
    public ImageView defaultImage, imageBorrow;
    @FXML
    public StackPane mainPane;
    @FXML
    public Pane searchResultBox;
    @FXML
    public ScrollPane pane;
    @FXML
    public GridPane Box;

    private final BorrowBookViewModel viewModel = new BorrowBookViewModel();
    private final BookDAO bookDAO = new BookDAO();
    private final List<Book> allBooks = bookDAO.getAllBooks();

    @FXML
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
        imageBorrow.setVisible(false);
        messageLabel.textProperty().bind(viewModel.messageProperty());
        viewModel.setLoggedInUser(SessionManager.getInstance().getLoggedInUser());
        User currentUser = SessionManager.getInstance().getLoggedInUser();
        if (currentUser != null) {
            UserName.setText(currentUser.getUsername());
        } else {
            UserName.setText("User not found !!");
        }
    }

    @FXML
    private void handleBorrow() {
        label.setText("Choose your book wisely!!");
        label.setStyle("-fx-text-fill: black;");
        if(!bookNameField.getText().isEmpty() && !bookIdField.getText().isEmpty()){
            label.setText("Please!! just choose one");
            label.setStyle("-fx-text-fill: red;");
            return;
        }
        if (bookNameField.getText().isEmpty()) {
            viewModel.borrowBookByISBN(bookIdField.getText());
        } else {
            viewModel.borrowBookByTitle(bookNameField.getText());
        }
        // Thêm hiệu ứng cho thông báo
        messageLabel.setStyle(viewModel.messageProperty().get().contains("successfully") ?
                "-fx-text-fill: green;" : "-fx-text-fill: red;");
        if (viewModel.messageProperty().get().contains("successfully")) {
            defaultImage.setVisible(false);
            imageBorrow.setVisible(true);
            label.setText("Enjoying my books, my master!!");
            label.setStyle("-fx-text-fill: black;");
        }
    }

    public void openMyAccount() throws IOException {
        loadView("User-my-account-view.fxml" ,myAccount);
    }

    public void openReturnBook() throws IOException {
        viewModel.openReturnBook();
        loadView("return-book.fxml", returnBook);
    }

    public void openBorrowBook(ActionEvent event) throws IOException {
        viewModel.openBorrowBook();
        loadView("borrow-book.fxml", borrowBooks);
    }

    public void openAI() throws IOException {
        loadView("User-Ai-view.fxml" , AI);
    }

    public void Logout() throws IOException {
        viewModel.Logout();
        loadView("login-view.fxml", logout);
    }

    public void backToMain(ActionEvent event) {
        SceneNavigator.backToMain(backToMain);
    }

    public void Search() throws IOException {
        SearchFunction.Search(search,Box,searchResultBox);
    }

    @FXML
    public void scanAndBorrowISBN(ActionEvent event) {
        // Cập nhật UI: đang quét
        viewModel.messageProperty().set("Đang quét ISBN từ webcam...");

        Thread cameraThread = new Thread(() -> {
            String scannedIsbn = com.example.libapp.api.ISBNScannerWindow.launchAndScan();

            Platform.runLater(() -> {
                if (scannedIsbn != null && !scannedIsbn.isEmpty()) {
                    bookIdField.setText(scannedIsbn); // Cập nhật vào TextField

                    // Gọi ViewModel xử lý mượn
                    viewModel.borrowBookByISBN(scannedIsbn);

                    // Cập nhật UI theo kết quả
                    String msg = viewModel.messageProperty().get();
                    boolean success = msg.contains("successfully");

                    messageLabel.setText(msg);
                    messageLabel.setStyle(success ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
                    imageBorrow.setVisible(success);
                    defaultImage.setVisible(!success);
                    label.setText(success ? "Hãy tận hưởng cuốn sách nhé!" : "");
                } else {
                    viewModel.messageProperty().set("Không quét được mã ISBN.");
                    messageLabel.setStyle("-fx-text-fill: red;");
                }
            });
        });

        cameraThread.setDaemon(true);
        cameraThread.start();
    }
}