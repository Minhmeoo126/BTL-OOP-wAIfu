package com.example.libapp.controllers;

import com.example.libapp.model.User;
import com.example.libapp.persistence.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class RegisterController {
    @FXML
    private Button CreateAccount;
    @FXML
    private UserDAO userDAO;
    @FXML
    public PasswordField addUserPassword;
    @FXML
    public TextField addUserName;
    @FXML
    public CheckBox AdminSelected;
    @FXML
    public CheckBox UserSelected;
    @FXML
    public Label information;
    @FXML
    private TabPane tabPaneLogin;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Label loginlabel;
    @FXML
    private Label userloginlabel;
    @FXML
    private Tab tabUser;
    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_USER = "USER";
    public RegisterController() {
        userDAO = new UserDAO();
    }
    @FXML
    public void GoToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/libapp/view/login-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) CreateAccount.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 600));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void Register(ActionEvent event) {
        String newUserName = addUserName.getText().trim();
        String newUserPassword = addUserPassword.getText();
        boolean adminSelected = AdminSelected.isSelected();
        boolean userSelected = UserSelected.isSelected();
        if (newUserName.isEmpty() || newUserPassword.isEmpty()) {
            information.setText("Tên đăng nhập và mật khẩu không được để trống.");
            return; // Dừng xử lý
        }

        // Kiểm tra việc chọn vai trò (chỉ được chọn 1)
        if ((!adminSelected && !userSelected)) {
            information.setText("Vui lòng chọn một loại tài khoản (Admin hoặc User).");
            return;
        }
        if(adminSelected && userSelected ){
            information.setText("chi duoc chon 1");
            AdminSelected.setText("False");
            UserSelected.setText("False");
            return;
        }
        String selectedRole;
        if(adminSelected){
            selectedRole = ROLE_ADMIN;
        } else {
            selectedRole = ROLE_USER;
        }
        User newUser = new User();
        newUser.setUsername(newUserName);
        newUser.setPassword(newUserPassword);
        newUser.setRole(selectedRole);
        newUser.setEmail("");
        newUser.setFullName("admin");
        if(userDAO.isEmailTaken(newUser.getEmail())){
            information.setText("email da ton tai");
            return;
        }
        if(userDAO.isUsernameTaken(newUser.getUsername())){
            information.setText("ten ng dung da ton tai");
            return;
        }
        userDAO.addUser(newUser);
        GoToLogin();
    }
}
