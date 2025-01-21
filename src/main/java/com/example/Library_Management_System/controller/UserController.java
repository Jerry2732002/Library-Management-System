package com.example.Library_Management_System.controller;

import com.example.Library_Management_System.dto.Book;
import com.example.Library_Management_System.dto.BorrowDetails;
import com.example.Library_Management_System.dto.User;
import com.example.Library_Management_System.enums.Category;
import com.example.Library_Management_System.repository.SessionRepository;
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
import java.util.Optional;

@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;
    private final SessionRepository sessionRepository;

    @Autowired
    public UserController(UserService userService, SessionRepository sessionRepository) {
        this.userService = userService;
        this.sessionRepository = sessionRepository;
    }

    private int isSessionValid(HttpSession session) {
        String sessionId = session.getId();

        return sessionRepository.getUserID(sessionId);

    }

    @PostMapping(path = "/register", produces = "application/json")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping(path = "/login", produces = "application/json")
    public ResponseEntity<Map<String, String>> userLogin(@RequestBody User user, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        session = request.getSession(true);

        return userService.userLogin(user, session);
    }

    @GetMapping(path = "/borrow-history", produces = "application/json")
    public ResponseEntity<Map<String, List<BorrowDetails>>> getBooksBorrowedByEmail(HttpSession session) {
        int userID = isSessionValid(session);
        if (userID == -1) {
            Map<String, List<BorrowDetails>> response = new HashMap<>();
            response.put("message : Invalid or expired session. Please log in again.", null);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        return userService.getBooksBorrowedByEmail(userID);
    }

    @PostMapping(path = "book/borrow")
    public ResponseEntity<Map<String, String>> borrowBook(@RequestParam("title") String title, HttpSession session) {
        int userID = isSessionValid(session);
        if (userID == -1) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Invalid or expired session. Please log in again");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        return userService.borrowBook(userID, title);
    }

    @PostMapping(path = "book/return")
    public ResponseEntity<Map<String, String>> returnBook(@RequestParam("title") String title, HttpSession session) {
        int userID = isSessionValid(session);
        if (userID == -1) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Invalid or expired session. Please log in again");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }

        return userService.returnBook(userID, title);
    }

    @GetMapping(path = "/list", produces = "application/json")
    public ResponseEntity<Map<String, List<Book>>> getAllBooks(HttpSession session) {
        int userID = isSessionValid(session);
        if (userID == -1) {
            Map<String, List<Book>> response = new HashMap<>();
            response.put("message : Invalid or expired session. Please log in again", null);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        Map<String, List<Book>> response = new HashMap<>();
        response.put("result", userService.getAllBooks());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "book/category/{category}", produces = "application/json")
    public ResponseEntity<Map<String, List<Book>>> getBookByCategory(@PathVariable("category") Category category, HttpSession session) {
        int userID = isSessionValid(session);
        if (userID == -1) {
            Map<String, List<Book>> response = new HashMap<>();
            response.put("message : Invalid or expired session. Please log in again", null);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        Map<String, List<Book>> response = new HashMap<>();
        response.put("result", userService.getBookByCategory(category));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "book/author/{author}", produces = "application/json")
    public ResponseEntity<Map<String, List<Book>>> getBookByAuthor(@PathVariable("author") String author, HttpSession session) {
        int userID = isSessionValid(session);
        if (userID == -1) {
            Map<String, List<Book>> response = new HashMap<>();
            response.put("message : Invalid or expired session. Please log in again", null);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        Map<String, List<Book>> response = new HashMap<>();
        response.put("result", userService.getBookByAuthor(author));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "book/title/{title}", produces = "application/json")
    public ResponseEntity<Map<String, List<Book>>> getBookByTitle(@PathVariable("title") String title, HttpSession session) {
        int userID = isSessionValid(session);
        if (userID == -1) {
            Map<String, List<Book>> response = new HashMap<>();
            response.put("message : Invalid or expired session. Please log in again", null);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        Map<String, List<Book>> response = new HashMap<>();
        response.put("result", userService.getBookByTitle(title));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
