package com.example.Library_Management_System.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SessionRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private SessionRepository sessionRepository;

    @Test
    void testCheckUserExistFound() {
        int userID = 1;
        String sql = "SELECT 1 FROM Sessions WHERE UserID = ?";

        when(jdbcTemplate.queryForObject(eq(sql), eq(new Object[]{userID}), eq(Integer.class))).thenReturn(1);

        boolean result = sessionRepository.checkUserExist(userID);

        assertTrue(result);
    }

    @Test
    void testCheckUserExistNotFound() {
        int userID = 1;
        String sql = "SELECT 1 FROM Sessions WHERE UserID = ?";

        when(jdbcTemplate.queryForObject(eq(sql), eq(new Object[]{userID}), eq(Integer.class)))
                .thenThrow(new EmptyResultDataAccessException(1));

        boolean result = sessionRepository.checkUserExist(userID);

        assertFalse(result);
    }

    @Test
    void testAddSessionSuccess() {
        int userID = 1;
        String sessionID = "session123";

        String sql = "INSERT INTO Sessions VALUES (?,?)";
        when(jdbcTemplate.update(eq(sql), eq(userID), eq(sessionID))).thenReturn(1);

        boolean result = sessionRepository.addSession(userID, sessionID);

        assertTrue(result);
    }

    @Test
    void testAddSessionFailure() {
        int userID = 1;
        String sessionID = "session123";

        String sql = "INSERT INTO Sessions VALUES (?,?)";
        when(jdbcTemplate.update(eq(sql), eq(userID), eq(sessionID))).thenReturn(0);

        boolean result = sessionRepository.addSession(userID, sessionID);

        assertFalse(result);
    }

    @Test
    void testGetUserIDFound() {
        String sessionID = "session123";
        int userID = 1;

        String sql = "SELECT UserID FROM Sessions WHERE SessionID = ?";
        when(jdbcTemplate.queryForObject(eq(sql), eq(new Object[]{sessionID}), eq(Integer.class))).thenReturn(userID);

        int result = sessionRepository.getUserID(sessionID);

        assertEquals(userID, result);
    }

    @Test
    void testGetUserIDNotFound() {
        String sessionID = "session123";

        String sql = "SELECT UserID FROM Sessions WHERE SessionID = ?";
        when(jdbcTemplate.queryForObject(eq(sql), eq(new Object[]{sessionID}), eq(Integer.class)))
                .thenThrow(new EmptyResultDataAccessException(1));

        int result = sessionRepository.getUserID(sessionID);

        assertEquals(-1, result);
    }

    // Test updateSession method
    @Test
    void testUpdateSessionSuccess() {
        int userID = 1;
        String sessionID = "newSession123";

        String sql = "UPDATE Sessions SET SessionID = ? WHERE UserID = ?";
        when(jdbcTemplate.update(eq(sql), eq(sessionID), eq(userID))).thenReturn(1);

        boolean result = sessionRepository.updateSession(userID, sessionID);

        assertTrue(result);
    }

    @Test
    void testUpdateSessionFailure() {
        int userID = 1;
        String sessionID = "newSession123";

        String sql = "UPDATE Sessions SET SessionID = ? WHERE UserID = ?";
        when(jdbcTemplate.update(eq(sql), eq(sessionID), eq(userID))).thenReturn(0);

        boolean result = sessionRepository.updateSession(userID, sessionID);

        assertFalse(result);
    }
}
