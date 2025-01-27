package com.example.Library_Management_System.service;

import com.example.Library_Management_System.dto.User;
import com.example.Library_Management_System.repository.UserRepository;
import com.example.Library_Management_System.repository.SessionRepository;
import com.example.Library_Management_System.repository.BookRepository;
import com.example.Library_Management_System.repository.BorrowRepository;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;


import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)  // Automatically initialize mocks
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticateService authenticateService;

    @Mock
    private BorrowRepository borrowRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private HttpSession httpSession;

    @InjectMocks
    private UserService userService;

    @Test
    void testUserLogin_Success() {

        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password123");

        User storedUser = new User();
        storedUser.setUserID(1);
        storedUser.setEmail("test@example.com");
        storedUser.setPassword("encoded_password");

        when(userRepository.findUserByEmail("test@example.com")).thenReturn(storedUser);
        when(authenticateService.checkPassword("password123", "encoded_password")).thenReturn(true);
        when(sessionRepository.checkUserExist(1)).thenReturn(false);
        when(sessionRepository.addSession(1, "mockedSessionId")).thenReturn(true);
        when(httpSession.getId()).thenReturn("mockedSessionId");

        ResponseEntity<Map<String, String>> response = userService.userLogin(user, httpSession);

        verify(userRepository).findUserByEmail("test@example.com");
        verify(authenticateService).checkPassword("password123", "encoded_password");
        verify(sessionRepository).addSession(1, "mockedSessionId");

        assertEquals(200, response.getStatusCodeValue());  // HTTP Status OK
        assertEquals("Login Successful", response.getBody().get("message"));
        assertEquals("mockedSessionId", response.getBody().get("SessionID"));
    }

    @Test
    void testUserLogin_InvalidEmail() {
        User user = new User();
        user.setEmail("invalid@example.com");

        when(userRepository.findUserByEmail("invalid@example.com")).thenReturn(null);
        ResponseEntity<Map<String, String>> response = userService.userLogin(user, httpSession);


        assertEquals(400, response.getStatusCodeValue());  // HTTP Status Bad Request
        assertEquals("Invalid Email(Email not found)", response.getBody().get("message"));
    }

    @Test
    void testUserLogin_IncorrectPassword() {

        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("wrongPassword");

        User storedUser = new User();
        storedUser.setEmail("test@example.com");
        storedUser.setPassword("encoded_password");

        when(userRepository.findUserByEmail("test@example.com")).thenReturn(storedUser);
        when(authenticateService.checkPassword("wrongPassword", "encoded_password")).thenReturn(false);

        ResponseEntity<Map<String, String>> response = userService.userLogin(user, httpSession);

        assertEquals(400, response.getStatusCodeValue());  // HTTP Status Bad Request
        assertEquals("Incorrect password", response.getBody().get("message"));
    }
}
