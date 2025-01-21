package com.example.Library_Management_System.repository;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SessionRepository {
    private final JdbcTemplate jdbcTemplate;

    public SessionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean checkUserExist(int userID) {
        String sql = "SELECT 1 FROM Sessions WHERE UserID = ?";

        try {
            jdbcTemplate.queryForObject(sql, new Object[]{userID}, Integer.class);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    public boolean addSession(int userID, String sessionID) {
        String sql = "INSERT INTO Sessions VALUES (?,?)";

        int rowsAffected = jdbcTemplate.update(sql, userID, sessionID);

        return rowsAffected > 0;
    }

    public int getUserID(String sessionID) {
        String sql = "SELECT UserID FROM Sessions WHERE SessionID = ?";

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{sessionID}, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            return -1;
        }
    }

    public boolean updateSession(int userID, String sessionID) {
        String sql = "UPDATE Sessions SET SessionID = ? WHERE UserID = ?";

        int rowsAffected = jdbcTemplate.update(sql, sessionID, userID);

        return rowsAffected > 0;
    }

}
