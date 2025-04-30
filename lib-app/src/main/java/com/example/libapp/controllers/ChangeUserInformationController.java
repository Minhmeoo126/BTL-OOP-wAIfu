package com.example.libapp.controllers;

import com.example.libapp.SessionManager;
import com.example.libapp.model.User;
import com.example.libapp.persistence.UserDAO;
import com.example.libapp.utils.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;

import static com.example.libapp.utils.SceneNavigator.loadView;

public class ChangeUserInformationController {

    public Label UserName;
    public TextField NameAccount;
    public TextField PassWord;
    public TextField Email;
    public TextField FullName;
    public Button SaveUserInformation;
    public Button myAccount;
    public Button backToUserManagement;
    public Button logout;
    private final UserDAO userDAO = new UserDAO();
    public Label messageLabel;

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

    public void openMyAccount(ActionEvent event) {
    }

    public void backToUserManagement(ActionEvent event) {
        SceneNavigator.backToMain(myAccount);
    }

    public void Logout() throws IOException {
        loadView("login-view.fxml" , logout);
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

                    messageLabel.setText("Cập nhật thông tin thành công.");
                } else {
                    messageLabel.setText("Cập nhật thất bại.");
                }
            }
        }

    }
}
