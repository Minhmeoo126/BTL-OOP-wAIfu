package com.example.libapp.viewmodel;

import com.example.libapp.persistence.UserDAO;

public class LoginViewModel {
    private UserDAO userDAO = new UserDAO();

    public boolean login(String username, String password) {
        return userDAO.validateUser(username, password);
    }
}