package com.example.libapp.controllers;

import com.example.libapp.SessionManager;
import com.example.libapp.model.BorrowingRecord;
import com.example.libapp.model.User;
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

import static com.example.libapp.utils.SceneNavigator.loadView;

public class MyAccountController {
    @FXML public Button AI, addBook, bookManage, userManagement, myAccount, returnBook, logout, borrowBooks, backToMain;
    @FXML public Label nameAccount, Role, BorrowedBooks, UserName;
    @FXML public TableView<BorrowingRecord> BorrowHistoryTable;
    public TableColumn<BorrowingRecord, Integer> IDColumn;
    public TableColumn<BorrowingRecord, String> nameBookColumn, AuthorColumn, borrowDateColumn, returnDateColumn;

    private final MyAccountViewModel viewModel = new MyAccountViewModel();
    private User user;

    @FXML
    public void initialize() {
        if (user == null) {
            user = SessionManager.getInstance().getLoggedInUser();  // Dùng khi không có thông tin từ bên ngoài
        }

        if (user != null) {
            nameAccount.setText("Name: " + user.getUsername());
            Role.setText("Role: " + user.getRole());
            UserName.setText(user.getUsername());
            viewModel.loadUserInfo(user);
        }

        // Các cột trong bảng mượn sách
        IDColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        nameBookColumn.setCellValueFactory(new PropertyValueFactory<>("BookName"));
        AuthorColumn.setCellValueFactory(new PropertyValueFactory<>("AuthorName"));
        borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

        // Load lịch sử mượn sách cho user
        viewModel.loadHistory(user);
        BorrowHistoryTable.setItems(viewModel.getRecords());
        BorrowedBooks.setText("Borrowed: " + viewModel.getRecords().size());
    }

    public void addNewBook(ActionEvent event) {}
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
                FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource("/com/example/libapp/view/my-account.fxml"));
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

    public void returnBooks() throws IOException {
        viewModel.openReturnBook();
        loadView("return-book.fxml", returnBook);
    }

    public void setUser(User user) {
        this.user = user;
        // Gọi lại phương thức initialize để cập nhật giao diện
        initialize();
    }
}
