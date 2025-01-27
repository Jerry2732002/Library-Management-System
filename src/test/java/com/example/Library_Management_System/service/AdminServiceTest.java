package com.example.Library_Management_System.service;

import com.example.Library_Management_System.dto.Book;
import com.example.Library_Management_System.dto.User;
import com.example.Library_Management_System.repository.BookRepository;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)  // Ensures Mockito is enabled
class AdminServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private UserService userService;

    @Mock
    private HttpSession httpSession;

    @InjectMocks
    private AdminService adminService;

    @Test
    void testUpdateBook() {
        Book book = new Book();
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        when(bookRepository.updateBook("Test Book", book)).thenReturn(ResponseEntity.ok(Map.of("message", "Book updated successfully")));
        ResponseEntity<Map<String, String>> response = adminService.updateBook("Test Book", book);
        verify(bookRepository).updateBook("Test Book", book);
        assertEquals(200, response.getStatusCodeValue());  // HTTP Status OK
        assertEquals("Book updated successfully", response.getBody().get("message"));
    }

    @Test
    void testUpdateBookCount() {
        when(bookRepository.updateBookCount("Test Book", 5)).thenReturn(ResponseEntity.ok(Map.of("message", "Book count updated successfully")));
        ResponseEntity<Map<String, String>> response = adminService.updateBookCount("Test Book", 5);
        verify(bookRepository).updateBookCount("Test Book", 5);

        assertEquals(200, response.getStatusCodeValue());  // HTTP Status OK
        assertEquals("Book count updated successfully", response.getBody().get("message"));
    }

    @Test
    void testAddBook() {
        Book book = new Book();
        book.setTitle("New Book");
        book.setAuthor("New Author");

        when(bookRepository.addBook(book)).thenReturn(ResponseEntity.ok(Map.of("message", "Book added successfully")));
        ResponseEntity<Map<String, String>> response = adminService.addBook(book);

        verify(bookRepository).addBook(book);
        assertEquals(200, response.getStatusCodeValue());  // HTTP Status OK
        assertEquals("Book added successfully", response.getBody().get("message"));
    }

    @Test
    void testRemoveBook() {
        when(bookRepository.removeBook("Test Book")).thenReturn(ResponseEntity.ok(Map.of("message", "Book removed successfully")));
        ResponseEntity<Map<String, String>> response = adminService.removeBook("Test Book");
        verify(bookRepository).removeBook("Test Book");
        assertEquals(200, response.getStatusCodeValue());  // HTTP Status OK
        assertEquals("Book removed successfully", response.getBody().get("message"));
    }

    @Test
    void testAdminLogin_Success() {
        User admin = new User();
        admin.setEmail("admin@example.com");
        admin.setPassword("adminPassword");
        when(userService.adminLogin(admin, httpSession)).thenReturn(ResponseEntity.ok(Map.of("message", "Login Successful")));
        ResponseEntity<Map<String, String>> response = adminService.adminLogin(admin, httpSession);
        verify(userService).adminLogin(admin, httpSession);
        assertEquals(200, response.getStatusCodeValue());  // HTTP Status OK
        assertEquals("Login Successful", response.getBody().get("message"));
    }

    @Test
    void testGetAllBooks() {
        Book book1 = new Book();
        book1.setTitle("Book 1");
        book1.setAuthor("Author 1");

        Book book2 = new Book();
        book2.setTitle("Book 2");
        book2.setAuthor("Author 2");
        when(bookRepository.getAllBooks()).thenReturn(List.of(book1, book2));

        List<Book> books = adminService.getAllBooks();

        verify(bookRepository).getAllBooks();

        assertEquals(2, books.size());  // Should return two books
        assertEquals("Book 1", books.get(0).getTitle());  // Verify book title
        assertEquals("Book 2", books.get(1).getTitle());  // Verify book title
    }
}
