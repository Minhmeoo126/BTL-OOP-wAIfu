package com.example.libapp.controllers;

import com.example.libapp.SessionManager;
import com.example.libapp.model.BorrowingRecord;
import com.example.libapp.model.User;
import com.example.libapp.utils.SceneNavigator;
import com.example.libapp.viewmodel.MyAccountViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

public class UserInformationController {
    @FXML
    public Label BorrowedBooks,Role,nameAccount;
    @FXML
    public Button myAccount,back;
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


    public void backToUserManagement() throws IOException {
        SceneNavigator.loadView("Usersmanagement-view.fxml", back);
    }

    public void setUser(User user) {
        this.user = user;
        // Gọi lại phương thức initialize để cập nhật giao diện
        initialize();
    }
}
