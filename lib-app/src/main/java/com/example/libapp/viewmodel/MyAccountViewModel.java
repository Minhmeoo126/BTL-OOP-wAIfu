package com.example.libapp.viewmodel;

import com.example.libapp.SessionManager;
import com.example.libapp.model.BorrowingRecord;
import com.example.libapp.model.User;
import com.example.libapp.persistence.BorrowingRecordDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyAccountViewModel {
    private final StringProperty username = new SimpleStringProperty("");
    private final StringProperty email = new SimpleStringProperty("");
    private final StringProperty fullName = new SimpleStringProperty("");
    private final StringProperty role = new SimpleStringProperty("");

    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public StringProperty fullNameProperty() {
        return fullName;
    }

    public StringProperty roleProperty() {
        return role;
    }

    public void loadUserInfo(User user) {
        if (user != null) {
            username.set("Username: " + user.getUsername());
            email.set("Email: " + user.getEmail());
            fullName.set("Full Name: " + user.getFullName());
            role.set("Role: " + user.getRole());
        } else {
            username.set("Username: N/A");
            email.set("Email: N/A");
            fullName.set("Full Name: N/A");
            role.set("Role: N/A");
        }
    }

    public void openAI() {
    }

    public void openUserManagement() {
    }

    public void openBookMangagement() {
    }

    public void openmyaccount() {

    }

    public void logout() {
    }

    public void openReturnBook() {
    }

    public void openBorrowBook() {

    }


    // lay tu borrowBookviewModel
    private final BorrowingRecordDAO borrowingRecordDAO = new BorrowingRecordDAO();
    private final ObservableList<BorrowingRecord> records = FXCollections.observableArrayList();
    private final User loggedInUser = SessionManager.getInstance().getLoggedInUser();

    public ObservableList<BorrowingRecord> getRecords() {
        return records;
    }

    public void loadHistory() {
        if (loggedInUser == null) {
            records.clear();
            return;
        }
        records.setAll(borrowingRecordDAO.getAllBorrowingRecords().stream()
                .filter(record -> record.getUserId() == loggedInUser.getId())
                .collect(java.util.stream.Collectors.toList()));
    }
}