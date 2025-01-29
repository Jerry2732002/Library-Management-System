package com.example.Library_Management_System.controller;

import com.example.Library_Management_System.dto.Book;
import com.example.Library_Management_System.dto.BorrowDetails;
import com.example.Library_Management_System.dto.User;
import com.example.Library_Management_System.repository.SessionRepository;
import com.example.Library_Management_System.service.AdminService;
import com.example.Library_Management_System.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AdminController {
    private final AdminService adminService;
    private final SessionRepository sessionRepository;

    @Autowired
    public AdminController(AdminService adminService, SessionRepository sessionRepository, UserService userService) {
        this.adminService = adminService;
        this.sessionRepository = sessionRepository;
    }

    private int isSessionValid(String sessionId) {
        return sessionRepository.getUserID(sessionId);
    }

    @PostMapping(path = "admin/login")
    public ResponseEntity<Map<String, String>> adminLogin(@RequestBody User admin, HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        session = request.getSession(true);

        return adminService.adminLogin(admin, session);
    }

    @PostMapping(path = "book/add")
    public ResponseEntity<Map<String, String>> addBook(@CookieValue(value = "JSESSION", defaultValue = "12345") String sessionID, @RequestBody Book book) {
        int userID = isSessionValid(sessionID);
        if (userID == -1) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Invalid or expired session. Please log in again");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        return adminService.addBook(book);
    }

    @DeleteMapping(path = "book/remove/{title}", produces = "application/json")
    public ResponseEntity<Map<String, String>> removeBook(@CookieValue(value = "JSESSION", defaultValue = "12345") String sessionID, @PathVariable("title") String title) {
        int userID = isSessionValid(sessionID);
        if (userID == -1) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Invalid or expired session. Please log in again");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        return adminService.removeBook(title);
    }

    @PutMapping(path = "book/update/{title}", produces = "application/json")
    public ResponseEntity<Map<String, String>> updateBook(@CookieValue(value = "JSESSION", defaultValue = "12345") String sessionID, @PathVariable("title") String title, @RequestBody Book book) {
        int userID = isSessionValid(sessionID);
        if (userID == -1) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Invalid or expired session. Please log in again");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        return adminService.updateBook(title, book);
    }

    @GetMapping(path = "book/list", produces = "application/json")
    public ResponseEntity<Map<String, List<Book>>>  getAllBooks(@CookieValue(value = "JSESSION", defaultValue = "12345") String sessionID) {
        int userID = isSessionValid(sessionID);
        if (userID == -1) {
            Map<String, List<Book>> response = new HashMap<>();
            response.put("message : Invalid or expired session. Please log in again", null);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        Map<String, List<Book>> response = new HashMap<>();
        response.put("result", adminService.getAllBooks());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "borrow/get-all-borrows", produces = "application/json")
    public ResponseEntity<Map<String, List<BorrowDetails>>> getAllBorrowDetails() {
        return adminService.getAllBorrowDetails();
    }
}
