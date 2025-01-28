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
        user.setEmail("jerry@gmail.com");
        user.setPassword("password123");

        User storedUser = new User();
        storedUser.setUserID(1);
        storedUser.setEmail("jerry@gmail.com");
        storedUser.setPassword("encoded_password");

        when(userRepository.findUserByEmail("jerry@gmail.com")).thenReturn(storedUser);
        when(authenticateService.checkPassword("password123", "encoded_password")).thenReturn(true);
        when(sessionRepository.checkUserExist(1)).thenReturn(false);
        when(sessionRepository.addSession(1, "somesessionid")).thenReturn(true);
        when(httpSession.getId()).thenReturn("somesessionid");

        ResponseEntity<Map<String, String>> response = userService.userLogin(user, httpSession);

        verify(userRepository).findUserByEmail("jerry@gmail.com");
        verify(authenticateService).checkPassword("password123", "encoded_password");
        verify(sessionRepository).addSession(1, "somesessionid");

        assertEquals(200, response.getStatusCodeValue());  // HTTP Status OK
        assertEquals("Login Successful", response.getBody().get("message"));
        assertEquals("somesessionid", response.getBody().get("SessionID"));
    }

    @Test
    void testUserLogin_InvalidEmail() {
        User user = new User();
        user.setEmail("jerrrry@gmail.com");

        when(userRepository.findUserByEmail("jerrrry@gmail.com")).thenReturn(null);
        ResponseEntity<Map<String, String>> response = userService.userLogin(user, httpSession);


        assertEquals(400, response.getStatusCodeValue());  // HTTP Status Bad Request
        assertEquals("Invalid Email(Email not found)", response.getBody().get("message"));
    }

    @Test
    void testUserLogin_IncorrectPassword() {

        User user = new User();
        user.setEmail("jerry@gmail.com");
        user.setPassword("hsdfhsfdhsdhf");

        User storedUser = new User();
        storedUser.setEmail("jerry@gmail.com");
        storedUser.setPassword("encoded_password");

        when(userRepository.findUserByEmail("jerry@gmail.com")).thenReturn(storedUser);
        when(authenticateService.checkPassword("hsdfhsfdhsdhf", "encoded_password")).thenReturn(false);

        ResponseEntity<Map<String, String>> response = userService.userLogin(user, httpSession);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Incorrect password", response.getBody().get("message"));
    }
}
