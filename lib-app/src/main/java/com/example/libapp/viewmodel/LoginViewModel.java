package com.example.libapp.viewmodel;

import com.example.libapp.model.User;
import com.example.libapp.persistence.UserDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.util.List;

public class LoginViewModel {
    private final UserDAO userDAO = new UserDAO();
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private final StringProperty errorMessage = new SimpleStringProperty();
    private User loggedInUser;

    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public StringProperty errorMessageProperty() {
        return errorMessage;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public boolean login() {
        String usernameValue = username.get();
        String passwordValue = password.get();

        if (usernameValue == null || usernameValue.isEmpty() || passwordValue == null || passwordValue.isEmpty()) {
            errorMessage.set("Please enter both username and password.");
            return false;
        }

        List<User> users = userDAO.getAllUsers();
        for (User user : users) {
            if (user.getUsername().equals(usernameValue) && user.getPassword().equals(passwordValue)) {
                loggedInUser = user;
                errorMessage.set("");
                return true;
            }
        }

        errorMessage.set("Invalid username or password.");
        return false;
    }
}