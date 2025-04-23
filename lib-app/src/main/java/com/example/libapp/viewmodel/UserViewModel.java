package com.example.libapp.viewmodel;

import com.example.libapp.model.User;
import com.example.libapp.persistence.UserDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UserViewModel {
    private final UserDAO userDAO = new UserDAO();
    private final StringProperty message = new SimpleStringProperty("");

    public StringProperty messageProperty() {
        return message;
    }

    public void addUser(String username, String password, String role, String email, String fullName) {
        if (username.isEmpty() || email.isEmpty() || fullName.isEmpty()) {
            message.set("Please fill in all fields.");
            return;
        }

        if (userDAO.isUsernameTaken(username)) {
            message.set("Username already taken.");
            return;
        }

        if (userDAO.isEmailTaken(email)) {
            message.set("Email already taken.");
            return;
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);
        user.setEmail(email);
        user.setFullName(fullName);

        userDAO.addUser(user);
        message.set("User added successfully!");
    }

    public void openMyAccount() {
    }

    public void openBookManage() {
    }

    public void openUserManagement() {
    }

    public void openAI() {
    }

    public void Logout() {

    }
}