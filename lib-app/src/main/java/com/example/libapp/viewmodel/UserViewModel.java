package com.example.libapp.viewmodel;

import com.example.libapp.model.User;
import com.example.libapp.persistence.DatabaseConnection;
import com.example.libapp.persistence.UserDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UserViewModel {
    private final UserDAO userDAO = new UserDAO();
    private final ObservableList<User> users = FXCollections.observableArrayList();
    private final StringProperty message = new SimpleStringProperty();

    // Thuộc tính để binding với form thêm/sửa người dùng
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    private final StringProperty role = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty fullName = new SimpleStringProperty();

    public UserViewModel() {
        users.addAll(userDAO.getAllUsers());
    }

    public ObservableList<User> getUsers() {
        return users;
    }

    public StringProperty messageProperty() {
        return message;
    }

    public StringProperty usernameProperty() {
        return username;
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public StringProperty roleProperty() {
        return role;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public StringProperty fullNameProperty() {
        return fullName;
    }

    // Phương thức addUser sử dụng binding
    public void addUser() {
        try {
            User user = new User();
            user.setUsername(username.get());
            user.setPassword(password.get());
            user.setRole(role.get());
            user.setEmail(email.get());
            user.setFullName(fullName.get());

            userDAO.addUser(user);
            users.add(user);
            message.set("User added successfully!");
            clearForm();
        } catch (Exception e) {
            message.set("Error: " + e.getMessage());
        }
    }

    // Phương thức addUser nhận tham số trực tiếp
    public void addUser(String username, String password, String role, String email, String fullName) {
        try {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setRole(role);
            user.setEmail(email);
            user.setFullName(fullName);

            userDAO.addUser(user);
            users.add(user);
            message.set("User added successfully!");
        } catch (Exception e) {
            message.set("Error: " + e.getMessage());
        }
    }

    public void deleteUser(User user) {
        try {
            try (var conn = DatabaseConnection.connect();
                 var pstmt = conn.prepareStatement("DELETE FROM Users WHERE id = ?")) {
                pstmt.setInt(1, user.getId());
                pstmt.executeUpdate();
            }
            users.remove(user);
            message.set("User deleted successfully!");
        } catch (Exception e) {
            message.set("Error: " + e.getMessage());
        }
    }

    public void updateUser(User user) {
        try {
            user.setUsername(username.get());
            user.setPassword(password.get());
            user.setRole(role.get());
            user.setEmail(email.get());
            user.setFullName(fullName.get());

            try (var conn = DatabaseConnection.connect();
                 var pstmt = conn.prepareStatement(
                         "UPDATE Users SET username = ?, password = ?, role = ?, email = ?, full_name = ? WHERE id = ?")) {
                pstmt.setString(1, user.getUsername());
                pstmt.setString(2, user.getPassword());
                pstmt.setString(3, user.getRole());
                pstmt.setString(4, user.getEmail());
                pstmt.setString(5, user.getFullName());
                pstmt.setInt(6, user.getId());
                pstmt.executeUpdate();
            }
            users.set(users.indexOf(user), user);
            message.set("User updated successfully!");
            clearForm();
        } catch (Exception e) {
            message.set("Error: " + e.getMessage());
        }
    }

    public void setUserForEdit(User user) {
        username.set(user.getUsername());
        password.set(user.getPassword());
        role.set(user.getRole());
        email.set(user.getEmail());
        fullName.set(user.getFullName());
    }

    private void clearForm() {
        username.set("");
        password.set("");
        role.set("");
        email.set("");
        fullName.set("");
    }
}