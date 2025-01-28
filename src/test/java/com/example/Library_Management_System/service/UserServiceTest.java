package com.example.Library_Management_System.service;

import com.example.Library_Management_System.dto.Book;
import com.example.Library_Management_System.dto.Borrow;
import com.example.Library_Management_System.dto.User;
import com.example.Library_Management_System.exception.IncorrectPasswordException;
import com.example.Library_Management_System.exception.UserNotFoundException;
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

@ExtendWith(MockitoExtension.class)
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

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Login Successful", response.getBody().get("message"));
        assertEquals("somesessionid", response.getBody().get("SessionID"));
    }

    @Test
    void testUserLogin_InvalidEmail() {
        User user = new User();
        user.setEmail("jerrrry@gmail.com");

        when(userRepository.findUserByEmail("jerrrry@gmail.com")).thenReturn(null);
        Exception exception = assertThrows(UserNotFoundException.class, () -> userService.userLogin(user, httpSession));
        String actualMessage = "Invalid Email";
        assertEquals(actualMessage, exception.getMessage());

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

        Exception exception = assertThrows(IncorrectPasswordException.class, () -> userService.userLogin(user, httpSession));

        String actualMessage = "Incorrect password";
        assertEquals(actualMessage, exception.getMessage());
    }

    @Test
    void borrowBookTest_Success() {
        User user = new User();
        user.setUserID(1);
        user.setEmail("jerry@gmail.com");

        Book book = new Book();
        book.setBookID(1);
        book.setTitle("The Great Gatsby");
        book.setCopiesAvailable(5);

        when(userRepository.findUserByID(1)).thenReturn(user);
        when(bookRepository.getBookByTitle("The Great Gatsby")).thenReturn(book);
        when(borrowRepository.borrowBook(any(Borrow.class))).thenReturn(true);

        ResponseEntity<Map<String, String>> response = userService.borrowBook(1, "The Great Gatsby");

        verify(userRepository).findUserByID(1);
        verify(bookRepository).getBookByTitle("The Great Gatsby");
        verify(borrowRepository).borrowBook(any(Borrow.class));
        verify(bookRepository).updateBookCount("The Great Gatsby", 4);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Borrow Successful", response.getBody().get("message"));
    }

    @Test
    void borrowBookTest_BookNotFound() {
        User user = new User();
        user.setUserID(1);

        when(bookRepository.getBookByTitle("Nonexistent Book")).thenThrow(new RuntimeException("Book Not Found"));

        Exception exception = assertThrows(RuntimeException.class, () -> bookRepository.getBookByTitle("Nonexistent Book"));

        String actualMessage = "Book Not Found";

        assertEquals(actualMessage, exception.getMessage());
    }

    @Test
    void borrowBookTest_UserNotFound() {
        Book book = new Book();
        book.setBookID(1);
        book.setTitle("The Great Gatsby");


        when(bookRepository.getBookByTitle("The Great Gatsby")).thenReturn(book);

        Exception exception = assertThrows(RuntimeException.class, () -> userService.borrowBook(12, "The Great Gatsby"));

        String actualMessage = "User Not Found";

        assertEquals(actualMessage, exception.getMessage());
    }


}
