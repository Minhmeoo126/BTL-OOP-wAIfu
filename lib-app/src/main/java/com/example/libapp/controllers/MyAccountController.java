package com.example.libapp.controllers;

import com.example.libapp.SessionManager;
import com.example.libapp.model.BorrowingRecord;
import com.example.libapp.model.User;
import com.example.libapp.persistence.DatabaseConnection;
import com.example.libapp.utils.SceneNavigator;
import com.example.libapp.viewmodel.MyAccountViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.example.libapp.utils.SceneNavigator.loadView;

public class MyAccountController {
    @FXML public Button AI, addBook, bookManage, userManagement, myAccount, returnBook, logout, borrowBooks, backToMain;
    @FXML public Label nameAccount, Role, BorrowedBooks, UserName;
    @FXML public TableView<BorrowingRecord> BorrowHistoryTable;
    public TableColumn<BorrowingRecord, Integer> IDColumn;
    public TableColumn<BorrowingRecord, String> nameBookColumn, AuthorColumn, borrowDateColumn, returnDateColumn;

    private final MyAccountViewModel viewModel = new MyAccountViewModel();
    public Button change;
    private User user;

    @FXML
    public void initialize() {
        if (user == null) {
            user = SessionManager.getInstance().getLoggedInUser();  // Lấy user từ session
        }

        if (user != null) {
            nameAccount.setText("Name: " + user.getUsername());
            Role.setText("Role: " + user.getRole());
            UserName.setText(user.getUsername());
            viewModel.loadUserInfo(user);
        }

        // Setup cột bảng
        IDColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        nameBookColumn.setCellValueFactory(new PropertyValueFactory<>("BookName"));
        AuthorColumn.setCellValueFactory(new PropertyValueFactory<>("AuthorName"));
        borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

        // Load dữ liệu
        loadBorrowHistory();
    }

    private void loadBorrowHistory() {
        viewModel.loadHistory(user);
        BorrowHistoryTable.setItems(viewModel.getRecords());
        BorrowedBooks.setText("Borrowed: " + viewModel.getRecords().stream()
                .filter(record -> record.getReturnDate() == null || record.getReturnDate().isEmpty())
                .count() + "");
    }

    public void addNewBook() throws IOException {
        viewModel.openAddBook();
        loadView("add-book-view.fxml", addBook);
    }

    public void goToBookManage(ActionEvent event) throws IOException {
        viewModel.openBookMangagement();
        loadView("bookmanagement-view.fxml", bookManage);
    }

    public void goToUserManagement() throws IOException {
        viewModel.openUserManagement();
        loadView("Usersmanagement-view.fxml", userManagement);
    }

    public void openMyAccount() {
        try {
            User currentUser = SessionManager.getInstance().getLoggedInUser();
            if (currentUser != null) {
                FXMLLoader loader;
                if (currentUser.getRole().equals("ADMIN")) {
                    loader = new FXMLLoader(SceneNavigator.class.getResource("/com/example/libapp/view/my-account.fxml"));
                } else {
                    loader = new FXMLLoader(SceneNavigator.class.getResource("/com/example/libapp/view/User-my-account-view.fxml"));
                }
                Parent root = loader.load();
                MyAccountController controller = loader.getController();
                controller.setUser(currentUser);
                Stage stage = (Stage) myAccount.getScene().getWindow();
                stage.setScene(new Scene(root, 1100, 600));
                stage.show();
            } else {
                System.out.println("Không có người dùng đăng nhập!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToAI(ActionEvent event) throws IOException {
        try {
            User currentUser = SessionManager.getInstance().getLoggedInUser();
            if (currentUser != null) {
                String role = currentUser.getRole();
                String fxml = role.equals("ADMIN") ? "/com/example/libapp/view/AI-view.fxml" : "/com/example/libapp/view/User-Ai-view.fxml";
                FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource(fxml));
                Parent root = loader.load();
                Stage stage = (Stage) AI.getScene().getWindow();
                stage.setScene(new Scene(root, 1100, 600));
                stage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void Logout() throws IOException {
        SessionManager.getInstance().clearSession();
        loadView("login-view.fxml", logout);
    }

    @FXML
    public void closeAccountWindow() {
        SessionManager.getInstance().clearSession();
        Stage stage = (Stage) myAccount.getScene().getWindow();
        stage.close();
    }

    public void backToMain() {
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

    public void setUser(User user) {
        this.user = user;
        initialize();
    }

    @FXML
    public void returnSelectedBook() {
        BorrowingRecord selectedRecord = BorrowHistoryTable.getSelectionModel().getSelectedItem();

        if (selectedRecord != null) {
            if (selectedRecord.getReturnDate() != null && !selectedRecord.getReturnDate().isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "Book Already Returned", "This book has already been returned.");
                return;
            }

            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Confirm Return");
            confirmationAlert.setHeaderText(null);
            confirmationAlert.setContentText("Are you sure you want to return this book?");

            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    LocalDateTime now = LocalDateTime.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formattedNow = now.format(formatter);

                    try (Connection conn = DatabaseConnection.connect()) {
                        String sql = "UPDATE BorrowingRecord SET return_date = ? WHERE id = ?";
                        PreparedStatement pstmt = conn.prepareStatement(sql);
                        pstmt.setString(1, formattedNow);
                        pstmt.setInt(2, selectedRecord.getId());
                        pstmt.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    // Reload toàn bộ TableView để đồng bộ dữ liệu
                    loadBorrowHistory();

                    showAlert(Alert.AlertType.INFORMATION, "Return Successful", "Book returned successfully!");
                }
            });

        } else {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a record to return.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void changeInformation() throws IOException {
        loadView("UserInformation-view.fxml" , myAccount);
    }
}
