package com.example.libapp.persistence;

import com.example.libapp.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UserDAO {
    private static final Logger logger = Logger.getLogger(UserDAO.class.getName());

    public boolean isUsernameTaken(String username) {
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement("SELECT 1 FROM Users WHERE username = ?")) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            logger.severe("Error checking username: " + e.getMessage());
            return false;
        }
    }

    public boolean isEmailTaken(String email) {
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement("SELECT 1 FROM Users WHERE email = ?")) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            logger.severe("Error checking email: " + e.getMessage());
            return false;
        }
    }

    public void addUser(User user) {
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO Users (username, password, role, email, full_name) VALUES (?, ?, ?, ?, ?)")) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getRole());
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getFullName());
            pstmt.executeUpdate();
            logger.info("Added user: " + user.getUsername());
        } catch (SQLException e) {
            logger.severe("Error adding user: " + e.getMessage());
            throw new RuntimeException("Failed to add user", e);
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Users");
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                user.setEmail(rs.getString("email"));
                user.setFullName(rs.getString("full_name"));
                users.add(user);
            }
            logger.info("Retrieved " + users.size() + " users from database");
        } catch (SQLException e) {
            logger.severe("Error retrieving users: " + e.getMessage());
            throw new RuntimeException("Failed to retrieve users", e);
        }
        return users;
    }
}