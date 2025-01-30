package com.example.Library_Management_System.controller;

import com.example.Library_Management_System.dto.Book;
import com.example.Library_Management_System.dto.BorrowDetails;
import com.example.Library_Management_System.dto.User;
import com.example.Library_Management_System.enums.Category;
import com.example.Library_Management_System.exception.SessionExpiredException;
import com.example.Library_Management_System.repository.SessionRepository;
import com.example.Library_Management_System.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


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

    private int isSessionValid(String sessionId) {
        int userID = sessionRepository.getUserID(sessionId);
        if (userID == -1) {
            throw new SessionExpiredException("Session Expired. Please Login Again");
        }
        return userID;
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
    public ResponseEntity<Map<String, List<BorrowDetails>>> getBooksBorrowedByEmail(@CookieValue(value = "JSESSION")String sessionID) {

        int userID = isSessionValid(sessionID);

        return userService.getBooksBorrowedByEmail(userID);
    }

    @PostMapping(path = "book/borrow")
    public ResponseEntity<Map<String, String>> borrowBook(@CookieValue(value = "JSESSION")String sessionID , @RequestParam("title") String title, HttpSession session) {
        int userID = isSessionValid(sessionID);

        return userService.borrowBook(userID, title);
    }

    @PostMapping(path = "book/return")
    public ResponseEntity<Map<String, String>> returnBook(@CookieValue(value = "JSESSION")String sessionID, @RequestParam("title") String title, HttpSession session) {
        int userID = isSessionValid(sessionID);

        return userService.returnBook(userID, title);
    }

    @GetMapping(path = "book/list", produces = "application/json")
    public ResponseEntity<Map<String, List<Book>>> getAllBooks(@CookieValue(value = "JSESSION")String sessionID) {
        isSessionValid(sessionID);

        Map<String, List<Book>> response = new HashMap<>();
        response.put("books", userService.getAllBooks());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "book/search", produces = "application/json")
    public ResponseEntity<Map<String, List<Book>>> searchBooks(@CookieValue(value = "JSESSION")String sessionID
            , @RequestParam(name = "author", defaultValue = "default_author", required = false)String author
            , @RequestParam(name = "title",defaultValue = "default_title", required = false) String title
            , @RequestParam(name = "category", required = false) Category category
            , @RequestParam(name = "pagination", required = false, defaultValue = "20")int pagination) {

        isSessionValid(sessionID);

        Map<String, List<Book>> response = new HashMap<>();

        response.put("books", userService.searchBooks(author,title,category,pagination));
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

}
