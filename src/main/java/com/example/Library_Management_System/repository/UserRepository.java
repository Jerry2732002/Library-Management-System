package com.example.Library_Management_System.repository;

import com.example.Library_Management_System.dto.User;
import com.example.Library_Management_System.dto.rowmapper.UserRowMapper;
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
            response.put("message", "User Added Successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("message", "Failed To Add User");
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

    public ResponseEntity<List<User>> getAllUsers() {
        String sql = "SELECT * Users WHERE UserID = ?";

        try {
            List<User> users = jdbcTemplate.query(sql, new UserRowMapper());
            Map<String, String> response = new HashMap<>();
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}