package com.example.libapp.viewmodel;

import com.example.libapp.model.User;
import com.example.libapp.controllers.LoginController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MyAccountViewModel {
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty fullName = new SimpleStringProperty();
    private final StringProperty role = new SimpleStringProperty();
/**
    public MyAccountViewModel() {
        User user = LoginController.getLoggedInUser();
        if (user != null) {
            username.set(user.getUsername());
            email.set(user.getEmail());
            fullName.set(user.getFullName());
            role.set(user.getRole());
        }
    }
*/
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
}