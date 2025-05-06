package com.example.libapp.persistence;

import com.example.libapp.SessionManager;
import com.example.libapp.model.User;
import javafx.scene.control.Label;

import java.awt.*;
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
        User currentUser = SessionManager.getInstance().getLoggedInUser();
        if(currentUser != null && username.equals(currentUser.getUsername())){
            return false;
        }
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
        User currentUser = SessionManager.getInstance().getLoggedInUser();
        if(currentUser != null && email.equals(currentUser.getEmail())){
            return false;
        }
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

    public static boolean updateUserInfo(int id, String username, String password, String email, String fullName, Label messageLabel) {
        // Câu lệnh SQL để cập nhật thông tin người dùng
        String sql = "UPDATE Users SET username = ?, password = ?, email = ?, full_name = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Thiết lập các tham số cho câu lệnh SQL
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, email);
            stmt.setString(4, fullName);
            stmt.setInt(5, id);
            stmt.executeUpdate();
            // Thực thi câu lệnh SQL
            int rowsUpdated = stmt.executeUpdate();

            // Cập nhật giao diện người dùng sau khi cập nhật cơ sở dữ liệu
            if (rowsUpdated > 0) {
                System.out.println("Success");
                messageLabel.setText("Success");
                return true;
            } else {
                System.out.println("Fail");
                messageLabel.setText("Fail");
                return false;
            }

        } catch (SQLException e) {
            // Xử lý lỗi nếu có
            e.printStackTrace();
            messageLabel.setText("Lỗi khi cập nhật thông tin");
            return false;
        }
    }

    public Integer getUserIdByName(String name) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Integer userId = null;

        try {
            conn = DatabaseConnection.connect();
            String sql = "SELECT id FROM Users WHERE username = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                userId = rs.getInt("id");
                System.out.println(userId);
            } else {
                System.out.println("Nguoi dung khong ton tai");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return userId;
    }

    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            int affectedRows = stmt.executeUpdate();

            return affectedRows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}