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

import static com.example.libapp.utils.SceneNavigator.loadView;

public class ChangeUserInformationController {
    @FXML
    public Label UserName, messageLabel;
    @FXML
    public TextField NameAccount, PassWord, Email, FullName;
    @FXML
    public Button SaveUserInformation, myAccount, backToMain, logout;

    private final UserDAO userDAO = new UserDAO();

    public void initialize(){
        User currentUser = SessionManager.getInstance().getLoggedInUser();
        if(currentUser == null){
            UserName.setText("khong co ng dung");
        } else{
            UserName.setText(currentUser.getUsername());
            NameAccount.setText(currentUser.getUsername());
            PassWord.setText(currentUser.getPassword());
            Email.setText(currentUser.getEmail());
            FullName.setText(currentUser.getFullName());
        }
    }

    public void Save() throws SQLException {
        User currentUser = SessionManager.getInstance().getLoggedInUser();
        if(currentUser != null) {
            String newNameAccount = NameAccount.getText().trim();
            String newPassWord = PassWord.getText().trim();
            String newEmail = Email.getText().trim();
            String newFullName = FullName.getText().trim();
            if(NameAccount.getText().isEmpty()){
                newNameAccount = currentUser.getUsername();
            }
            if(PassWord.getText().isEmpty()){
                newPassWord = currentUser.getPassword();
            }
            if(Email.getText().isEmpty()){
                newEmail = currentUser.getEmail();
            }
            if(FullName.getText().isEmpty()){
                newFullName = currentUser.getFullName();
            }

            // check va update thong tin nguoi dung
            if(userDAO.isUsernameTaken(newNameAccount)){
                messageLabel.setText("ten nguoi dung da ton tai");

            } else if(userDAO.isEmailTaken(newEmail)){
                messageLabel.setText("email da ton tai");

            } else{
                boolean isUpdate = UserDAO.updateUserInfo(userDAO.getUserIdByName(currentUser.getUsername()),newNameAccount,newPassWord,newEmail,newFullName,messageLabel);
                if(isUpdate){
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
                } else {
                    messageLabel.setText("Cập nhật thất bại.");
                }
            }
        }
    }
    public void openMyAccount() throws IOException {
        loadView("UserInformation-view.fxml" , myAccount);
    }

    public void backToMain() {
        SceneNavigator.backToMain(backToMain);
    }

    public void Logout() throws IOException {
        loadView("login-view.fxml" , logout);
    }
}
