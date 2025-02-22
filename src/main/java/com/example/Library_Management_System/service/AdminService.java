package com.example.Library_Management_System.service;

import com.example.Library_Management_System.dto.Book;
import com.example.Library_Management_System.dto.BorrowDetails;
import com.example.Library_Management_System.dto.User;
import com.example.Library_Management_System.enums.Membership;
import com.example.Library_Management_System.repository.BookRepository;
import com.example.Library_Management_System.repository.BorrowRepository;
import com.example.Library_Management_System.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    private final BookRepository bookRepository;
    private final UserService userService;
    private final BorrowRepository borrowRepository;
    private final UserRepository userRepository;

    @Autowired
    public AdminService(BookRepository bookRepository, UserService userService, BorrowRepository borrowRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userService = userService;
        this.borrowRepository = borrowRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<Map<String, String>> updateBook(String title, Book book) {
        Book original = bookRepository.getBookByTitle(title);
        if (original != null) {
            if (book.getAuthor() == null) {
                book.setAuthor(original.getAuthor());
            }

            if (book.getCategory() == null) {
                book.setCategory(original.getCategory());
            }

            if (!book.isRare()) {
                book.setRare(original.isRare());
            }

            if (book.getCopiesAvailable() == 0) {
                book.setCopiesAvailable(original.getCopiesAvailable());
            }
        }

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

    public ResponseEntity<Map<String, List<BorrowDetails>>> getAllBorrowDetails() {
        return borrowRepository.getAllBorrowDetails();
    }

    public ResponseEntity<Map<String, String>> updateUserMembership(String email, Membership membership) {
        Map<String, String> response = new HashMap<>();

        if (userRepository.updateUserMembership(email, membership)) {
            response.put("message", "Member " + email + " membership was change to " + membership.name());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("message", "Failed to change membership");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
