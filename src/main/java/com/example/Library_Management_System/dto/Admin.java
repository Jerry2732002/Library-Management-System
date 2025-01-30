package com.example.Library_Management_System.dto;

import com.example.Library_Management_System.enums.Role;

public class Admin {
    private int adminID;
    private String email;
    private String password;
    private Role role = Role.ADMIN;

    public Admin(int adminID, String email, String password, String role) {
        this.adminID = adminID;
        this.email = email;
        this.password = password;
        this.role = Role.valueOf(role);
    }

    public Admin() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getAdminID() {
        return adminID;
    }

    public void setAdminID(int adminID) {
        this.adminID = adminID;
    }
}
