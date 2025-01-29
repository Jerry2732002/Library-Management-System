package com.example.Library_Management_System.dto;

import com.example.Library_Management_System.enums.Role;

public class Admin {
    private String email;
    private String password;
    private Role role = Role.ADMIN;

    public Admin(String email, String password) {
        this.email = email;
        this.password = password;
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
}
