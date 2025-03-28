package com.example.libapp.controllers;


import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.mindrot.jbcrypt.BCrypt;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class LoginController {

    @FXML
    private Label lblAdmin;

    @FXML
    private Label lblCreateAccount;

    @FXML
    private Label lblStatus;

    @FXML
    private Label lblUser;

    @FXML
    private Pane slidingPane;

    @FXML
    private Tab tabAdmin;

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
    private StackPane mainpane;
    @FXML
    private Tab tabUser;

    @FXML
    void openAdminTab(MouseEvent event) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(400), lblStatus);
        transition.setToX(0); // Đưa về vị trí ban đầu (bên trái)
        transition.play();
        transition.setOnFinished(e -> lblStatus.setText("ADMINISTRATOR"));
        tabPaneLogin.getSelectionModel().select(tabAdmin);
    }

    @FXML
    void openUserTab(MouseEvent event) {
        TranslateTransition transition = new TranslateTransition(Duration.millis(400), lblStatus);
        transition.setToX(200); // Điều chỉnh giá trị này để đảm bảo trượt đúng vị trí
        transition.play();
        transition.setOnFinished(event1 -> lblStatus.setText("FOR USER"));
        tabPaneLogin.getSelectionModel().select(tabUser);
    }


    @FXML
    private Connection connect() {
        try {
            String url = "jdbc:mysql://localhost:3306/lib?serverTimezone=UTC"; // Thay tên CSDL của bạn
            String user = "root";
            String pass = "meomeomeo";
            return DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @FXML
    private HashMap<String, String> getUserData() {
        HashMap<String, String> userData = new HashMap<>();
        String sql = "SELECT username, password FROM users"; // Truy vấn tất cả username & password

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String user = rs.getString("username");
                String pass = rs.getString("password");
                userData.put(user, pass);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userData;
    }
    @FXML
    public void Login(String expectedRole){
        String userName = username.getText();
        String passWord = password.getText();
        String role = checkLogin(userName,passWord);
        if (role == null) {
            loginlabel.setText("Sai tài khoản hoặc mật khẩu!");
            userloginlabel.setText("Sai tai khoan hoac mat khau");

        } else if (!role.equals(expectedRole)) {
            loginlabel.setText("Bạn không có quyền đăng nhập vào khu vực này!");
            userloginlabel.setText("Bạn không có quyền đăng nhập vào khu vực này!");
        } else {
            loginlabel.setText("Đăng nhập thành công!");
            userloginlabel.setText("Đăng nhập thành công!");
            if (role.equals("admin")) {
                handleLoginButtonAction();
            } else {
                handleLoginButtonAction();
            }
        }
    }

    @FXML
    private void handleLoginButtonAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/libapp/main-view.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) username.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 600));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String checkLogin(String userName, String passWord) {
        String sql = "SELECT password, role FROM users WHERE username = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userName);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString("password");
                String role = rs.getString("role");

                if (BCrypt.checkpw(passWord, hashedPassword)) {
                    return role; // Trả về vai trò (admin hoặc user)
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public void adminlogin(ActionEvent event) {
        Login("admin");
    }
    public void userlogin(ActionEvent event) {
        Login("user");
    }
}
