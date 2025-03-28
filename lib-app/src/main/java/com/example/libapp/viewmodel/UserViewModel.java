package com.example.libapp.viewmodel;

import com.example.libapp.persistence.UserDAO;

public class UserViewModel {
    private UserDAO userDAO = new UserDAO();

    public void addUser(String username, String password, String role, String email, String fullName) {
        userDAO.addUser(username, password, role, email, fullName);
    }
}