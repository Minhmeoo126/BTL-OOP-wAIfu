package com.example.libapp.utils;

import com.example.libapp.SessionManager;
import com.example.libapp.controllers.SearchResultController;
import com.example.libapp.model.Book;
import com.example.libapp.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneNavigator {
    public static void backToMain(Button button) {
        try {
            User currentUser = SessionManager.getInstance().getLoggedInUser();
            String role = currentUser.getRole();
            if (role.equals("ADMIN")) {
                FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource("/com/example/libapp/view/main-admin-view.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) button.getScene().getWindow();
                stage.setScene(new Scene(root, 1100, 600));
                stage.show();
            } else {
                FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource("/com/example/libapp/view/main.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) button.getScene().getWindow();
                stage.setScene(new Scene(root, 1100, 600));
                stage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadView(String fxmlFile, Button button) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource("/com/example/libapp/view/" + fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) button.getScene().getWindow();
            stage.setScene(new Scene(root, 1100, 600));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadSearchResult(TextField search) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource("/com/example/libapp/view/SearchResults-view.fxml"));
        Parent root = loader.load();

        SearchResultController controller = loader.getController();
        controller.setSearchKeyword(search.getText());

        Stage stage = (Stage) search.getScene().getWindow();
        stage.setScene(new Scene(root, 1100, 600));
        stage.show();
    }
}
