package com.example.libapp.model;

public class User {
    private int id;
    private String username;
    private String password;
    private String role;
    private String email;
    private String fullName;

    public User(int id, String username, String password, String role, String email, String fullName) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
        this.email = email;
        this.fullName = fullName;
    }

    // Getters
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getEmail() { return email; }
    public String getFullName() { return fullName; }
}