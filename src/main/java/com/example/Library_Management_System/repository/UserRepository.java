package com.example.Library_Management_System.repository;

import com.example.Library_Management_System.dto.Admin;
import com.example.Library_Management_System.dto.BorrowDetails;
import com.example.Library_Management_System.dto.User;
import com.example.Library_Management_System.dto.rowmapper.AdminRowMapper;
import com.example.Library_Management_System.dto.rowmapper.BorrowDetailsRowMapper;
import com.example.Library_Management_System.dto.rowmapper.UserRowMapper;
import com.example.Library_Management_System.enums.Membership;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ResponseEntity<Map<String, String>> registerUser(User user) {
        String sql = "INSERT INTO Users (Email, Password, Firstname, Lastname, Membership, Role) VALUES (?,?,?,?,?,?)";

        int rowsAffected = jdbcTemplate.update(sql, user.getEmail(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getMembership().name(), user.getRole().name());
        Map<String, String> response = new HashMap<>();
        if (rowsAffected == 1) {
            response.put("Message", "User Added Successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("Message", "Failed To Add User");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Map<String, String>> deleteUser(int userID) {
        String sql = "DELETE FROM Users WHERE UserID = ?";

        int rowsAffected = jdbcTemplate.update(sql, userID);
        Map<String, String> response = new HashMap<>();
        if (rowsAffected == 1) {
            response.put("message", "User Deleted Successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("message", "Failed To Delete User");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    public User findUserByEmail(String email) {
        String sql = "SELECT * FROM Users WHERE Email = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{email}, new UserRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public User findUserByID(int userID) {
        String sql = "SELECT * FROM Users WHERE UserID = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{userID}, new UserRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Admin findAdminByEmail(String email) {
        String sql = "SELECT * FROM Users WHERE Email = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{email}, new AdminRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public ResponseEntity<List<User>> getAllUsers() {
        String sql = "SELECT * Users";

        try {
            List<User> users = jdbcTemplate.query(sql, new UserRowMapper());
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public ResponseEntity<Map<String, List<BorrowDetails>>> getBooksBorrowedByEmail(int userID) {
        String sql = "SELECT * FROM Users u\n" +
                "JOIN Borrows br ON br.UserID = u.UserID\n" +
                "JOIN Books b ON b.BookID = br.BookID\n" +
                "WHERE u.UserID = ?;";

        try {
            List<BorrowDetails> borrows = jdbcTemplate.query(sql, new BorrowDetailsRowMapper(),userID);
            Map<String, List<BorrowDetails>> response = new HashMap<>();
            response.put("Result", borrows);
            return new ResponseEntity<>(response , HttpStatus.OK);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public boolean updateUserMembership(String email, Membership membership) {
        String sql = "UPDATE Users SET Membership = ? WHERE Email = ?";

        int rowsAffect = jdbcTemplate.update(sql, membership.name(), email);

        return rowsAffect == 1;
    }

    public Admin findAdminByID(int userID) {
        String sql = "SELECT * FROM Users WHERE UserID = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{userID}, new AdminRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}