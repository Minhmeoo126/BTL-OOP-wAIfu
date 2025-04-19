package com.example.libapp.controllers;

import com.example.libapp.viewmodel.UserViewModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.regex.Pattern;

public class RegisterController {
    @FXML
    private Button CreateAccount;
    @FXML
    private PasswordField addUserPassword;
    @FXML
    private TextField addUserName;
    @FXML
    private TextField emailField;
    @FXML
    private TextField fullNameField;
    @FXML
    private Label information;

    private final UserViewModel viewModel = new UserViewModel();

    // Regex để kiểm tra định dạng email
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    @FXML
    public void initialize() {
        // Bind Label information với messageProperty của viewModel
        information.textProperty().bind(viewModel.messageProperty());
    }

    // Phương thức kiểm tra định dạng email
    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    @FXML
    public void GoToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/libapp/view/login-view.fxml"));
            if (loader.getLocation() == null) {
                viewModel.messageProperty().set("Error: login-view.fxml not found");
                return;
            }
            Parent root = loader.load();
            Stage stage = (Stage) CreateAccount.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 600));
            stage.setTitle("Login");
            stage.show();
        } catch (IOException e) {
            viewModel.messageProperty().set("Error loading login view: " + e.getMessage());
        }
    }

    @FXML
    public void Register(ActionEvent event) {
        String username = addUserName.getText().trim();
        String password = addUserPassword.getText();
        String email = emailField.getText().trim();
        String fullName = fullNameField.getText().trim();

        // Kiểm tra các trường có rỗng không
        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || fullName.isEmpty()) {
            viewModel.messageProperty().set("Thiếu thông tin: Vui lòng điền đầy đủ các trường.");
            return;
        }

        // Kiểm tra định dạng email
        if (!isValidEmail(email)) {
            viewModel.messageProperty().set("Sai định dạng email.");
            return;
        }

        // Gán vai trò mặc định là "USER"
        String role = "USER";
        viewModel.addUser(username, password, role, email, fullName);
        if (viewModel.messageProperty().get().contains("successfully")) {
            GoToLogin();
        }
    }
}