package com.example.Library_Management_System.service;

import com.example.Library_Management_System.dto.Book;
import com.example.Library_Management_System.dto.User;
import com.example.Library_Management_System.repository.BookRepository;
import com.example.Library_Management_System.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    private final BookRepository bookRepository;
    private final UserService userService;

    @Autowired
    public AdminService(BookRepository bookRepository, UserService userService) {
        this.bookRepository = bookRepository;
        this.userService = userService;
    }

    public ResponseEntity<Map<String, String>> updateBook(String title, Book book) {
        return bookRepository.updateBook(title, book);
    }

    public ResponseEntity<Map<String, String>> updateBookCount(String title, int newAvailableCopies) {
        return bookRepository.updateBookCount(title, newAvailableCopies);
    }

    public ResponseEntity<Map<String, String>> addBook(Book book) {
        return bookRepository.addBook(book);
    }

    public ResponseEntity<Map<String, String>> removeBook(String title) {
        return bookRepository.removeBook(title);
    }

    public ResponseEntity<Map<String, String>> adminLogin(User admin, HttpSession session) {
        return userService.adminLogin(admin, session);
    }
    public List<Book> getAllBooks() {
        return bookRepository.getAllBooks();
    }
}
