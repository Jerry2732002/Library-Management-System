package com.example.Library_Management_System.repository;

import com.example.Library_Management_System.dto.User;
import com.example.Library_Management_System.dto.rowmapper.UserRowMapper;
import com.example.Library_Management_System.enums.Membership;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource("classpath:application-test.properties")
@Sql(scripts = "classpath:test-database.sql")
class UserRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private UserRepository userRepository;

    @Test
    void testRegisterUserSuccess() {
        String sql = "INSERT INTO Users (Email, Password, Firstname, Lastname, Membership, Role) VALUES (?,?,?,?,?,?)";
        when(jdbcTemplate.update(eq(sql), any(), any(), any(), any(), any(), any())).thenReturn(1);

        ResponseEntity<Map<String, String>> response = userRepository.registerUser(new User(1, "test@gmail.com", "Test", "User", "LastName", Membership.GOLD));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User Added Successfully", response.getBody().get("Message"));
    }

    @Test
    void testRegisterUserFailure() {
        String sql = "INSERT INTO Users (Email, Password, Firstname, Lastname, Membership, Role) VALUES (?,?,?,?,?,?)";
        when(jdbcTemplate.update(eq(sql), any(), any(), any(), any(), any(), any())).thenReturn(0);

        ResponseEntity<Map<String, String>> response = userRepository.registerUser(new User(1, "test@gmail.com", "Test", "User", "LastName", Membership.GOLD));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Failed To Add User", response.getBody().get("Message"));
    }

    @Test
    void testFindUserByEmailFound() {
        User user =new User(1, "jerry@gmail.com", "Test", "Jerry", "Sebastian", Membership.GOLD);
        String sql = "SELECT * FROM Users WHERE Email = ?";
        when(jdbcTemplate.queryForObject(eq(sql), eq(new Object[]{ "jerry@gmail.com" }), any(UserRowMapper.class))).thenReturn(user);

        User result = userRepository.findUserByEmail("jerry@gmail.com");

        assertNotNull(result);
        assertEquals("Jerry", result.getFirstName());
        assertEquals("Sebastian", result.getLastName());
    }

    @Test
    void testFindUserByEmailNotFound() {
        String sql = "SELECT * FROM Users WHERE Email = ?";

        User result = userRepository.findUserByEmail("nonexistent@gmail.com");

        assertNull(result);
    }

    @Test
    void testDeleteUserSuccess() {

        String sql = "DELETE FROM Users WHERE UserID = ?";
        when(jdbcTemplate.update(eq(sql), anyInt())).thenReturn(1);

        ResponseEntity<Map<String, String>> response = userRepository.deleteUser(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User Deleted Successfully", response.getBody().get("message"));
    }

    @Test
    void testDeleteUserFailure() {
        String sql = "DELETE FROM Users WHERE UserID = ?";
        when(jdbcTemplate.update(eq(sql), anyInt())).thenReturn(0);

        ResponseEntity<Map<String, String>> response = userRepository.deleteUser(1);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Failed To Delete User", response.getBody().get("message"));
    }

    @Test
    public void testGetAllUsers_NoUsersFound() {

        String sql = "SELECT * Users";
        when(jdbcTemplate.query(eq(sql), any(UserRowMapper.class))).thenReturn(null);

        ResponseEntity<List<User>> response = userRepository.getAllUsers();

        assertNull(response.getBody());
    }

    @Test
    public void testGetAllUsers_UsersFound() {
        List<User> users = Arrays.asList(new User(1, "jerry@gmail.com", "$2a$12$7sKoknBV0ypKJiHzTCEBQuSluXK36WdYKitv4bGYX/VL96Hs5c.9S", "Jerry", "Sebastian", Membership.GOLD));

        String sql = "SELECT * Users";
        when(jdbcTemplate.query(eq(sql), any(UserRowMapper.class))).thenReturn(users);

        ResponseEntity<List<User>> response = userRepository.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
    }

}
