package com.example.libapp.controllers;


import com.example.libapp.persistence.UserDAO;
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
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class LoginController {
    @FXML
    public PasswordField addUserPassword;
    @FXML
    public TextField addUserName;
    @FXML
    public CheckBox Admin;
    @FXML
    public CheckBox User;
    public Label information;
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
    private Tab tabUser;
    @FXML
    private Button CreateAccount;
    private UserDAO userDAO;
    public LoginController() {
        // Khởi tạo DAO khi Controller được tạo
        userDAO = new UserDAO();
    }
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



    public void userlogin(ActionEvent event) {
    }

    public void adminlogin(ActionEvent event) {
    }

    /**
     * ham chuyen huong tu login -> register va nguoc lai.
     */

    public void GoToRegister(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/libapp/view/CreateAccountView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) username.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 600));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}