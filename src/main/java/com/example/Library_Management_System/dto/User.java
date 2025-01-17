package com.example.Library_Management_System.dto;

import com.example.Library_Management_System.enums.Membership;
import com.example.Library_Management_System.enums.Role;
import jakarta.validation.constraints.*;

public class User {
    private int userID;

    @NotNull(message = "Email cannot be null")
    @Size(min = 3, max = 100, message = "Email should be between 3 and 100 characters")
    @Email(message = "Invalid Email")
    private String email;

    @NotNull(message = "Password cannot be null")
    @Size(min = 8, max = 20, message = "Password should be between 8 and 20 characters")
    private String password;

    @Size(min = 3, max = 30, message = "First Name should be between 3 and 30 characters")
    @Pattern(regexp = "[A-Za-z]*", message = "First Name should only have alphabets")
    private String firstName;

    @Size(min = 3, max = 30, message = "Last Name should be between 3 and 30 characters")
    @Pattern(regexp = "[A-Za-z]*", message = "Last Name should only have alphabets")
    private String lastName;


    private Membership membership;
    private Role role = Role.USER;

    public User() {}

    public User(int userID, String email, String password, String firstName, String lastName, Membership membership) {
        this.userID = userID;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.membership = membership;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Membership getMembership() {
        return membership;
    }

    public void setMembership(Membership membership) {
        this.membership = membership;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
