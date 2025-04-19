package com.example.libapp.viewmodel;

import com.example.libapp.model.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

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
}