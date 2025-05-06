package com.example.libapp.controllers;

import com.example.libapp.SessionManager;
import com.example.libapp.model.User;
import com.example.libapp.persistence.UserDAO;
import com.example.libapp.utils.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;
import java.util.regex.Pattern;

import static com.example.libapp.utils.SceneNavigator.loadView;

public class ChangeUserInformationController {
    @FXML
    public Label UserName, messageLabel;
    @FXML
    public TextField NameAccount, PassWord, Email, FullName;
    @FXML
    public Button SaveUserInformation, myAccount, backToMain, logout;

    private final UserDAO userDAO = new UserDAO();
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public void initialize() {
        User currentUser = SessionManager.getInstance().getLoggedInUser();
        if (currentUser == null) {
            UserName.setText("khong co ng dung");
        } else {
            UserName.setText(currentUser.getUsername());
            NameAccount.setText(currentUser.getUsername());
            PassWord.setText(currentUser.getPassword());
            Email.setText(currentUser.getEmail());
            FullName.setText(currentUser.getFullName());
        }
    }

    public void Save() throws SQLException {
        User currentUser = SessionManager.getInstance().getLoggedInUser();
        if (currentUser != null) {
            String newNameAccount = NameAccount.getText().trim();
            String newPassWord = PassWord.getText().trim();
            String newEmail = Email.getText().trim();
            String newFullName = FullName.getText().trim();
            if(NameAccount.getText().isEmpty() || PassWord.getText().isEmpty() || Email.getText().isEmpty() || FullName.getText().isEmpty()){
                messageLabel.setText("Xin vui lòng nhập đầy đủ thông tin");
                messageLabel.setStyle("-fx-text-fill: red;");
                return;
            }

            // check va update thong tin nguoi dung
            if (userDAO.isUsernameTaken(newNameAccount)) {
                messageLabel.setText("Tên người dùng đã tồn tại");
                messageLabel.setStyle("-fx-text-fill: red;");

            } else if (!isValidEmail(newEmail)) {
                messageLabel.setText("Sai định dạng email");
                messageLabel.setStyle("-fx-text-fill: red;");

            } else if (userDAO.isEmailTaken(newEmail)) {
                messageLabel.setText("Email đã tồn tại");
                messageLabel.setStyle("-fx-text-fill: red;");
            } else {
                boolean isUpdate = UserDAO.updateUserInfo(userDAO.getUserIdByName(currentUser.getUsername()), newNameAccount, newPassWord, newEmail, newFullName, messageLabel);
                if (isUpdate) {
                    User updatedUser = new User();
                    updatedUser.setId(currentUser.getId());
                    updatedUser.setUsername(newNameAccount);
                    updatedUser.setPassword(newPassWord);
                    updatedUser.setEmail(newEmail);
                    updatedUser.setFullName(newFullName);
                    updatedUser.setRole(currentUser.getRole());

                    // Cập nhật lại session với thông tin mới
                    SessionManager.getInstance().setLoggedInUser(updatedUser);
                    UserName.setText(updatedUser.getUsername());

                    messageLabel.setText("Cập nhật thông tin thành công.");
                    messageLabel.setStyle("-fx-text-fill: green;");

                } else {
                    messageLabel.setText("Cập nhật thất bại.");
                    messageLabel.setStyle("-fx-text-fill: red;");
                }
            }
        }
    }

    public void openMyAccount() throws IOException {
        loadView("UserInformation-view.fxml", myAccount);
    }

    public void backToMain() {
        SceneNavigator.backToMain(backToMain);
    }

    public void Logout() throws IOException {
        loadView("login-view.fxml", logout);
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }
}
