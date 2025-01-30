package com.example.Library_Management_System.controller;

import com.example.Library_Management_System.dto.Admin;
import com.example.Library_Management_System.dto.Book;
import com.example.Library_Management_System.dto.BorrowDetails;
import com.example.Library_Management_System.dto.User;
import com.example.Library_Management_System.enums.Membership;
import com.example.Library_Management_System.enums.Role;
import com.example.Library_Management_System.exception.AdminPermissionException;
import com.example.Library_Management_System.exception.SessionExpiredException;
import com.example.Library_Management_System.repository.SessionRepository;
import com.example.Library_Management_System.repository.UserRepository;
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
    private final UserRepository userRepository;

    @Autowired
    public AdminController(AdminService adminService, SessionRepository sessionRepository, UserService userService, UserRepository userRepository) {
        this.adminService = adminService;
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    private void checkIfAdmin(int userID) {
        Admin admin = userRepository.findAdminByID(userID);
        if (!admin.getRole().equals(Role.ADMIN)) {
            throw new AdminPermissionException("User: " + admin.getEmail() + " is not a VALID ADMIN");
        }
    }

    private void isSessionValid(String sessionId) {
        int userID = sessionRepository.getUserID(sessionId);
        if (userID == -1) {
            throw new SessionExpiredException("Session Expired. Please Login Again");
        }
        checkIfAdmin(userID);
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
    public ResponseEntity<Map<String, String>> addBook(@CookieValue(value = "JSESSION") String sessionID, @RequestBody Book book) {
        isSessionValid(sessionID);

        return adminService.addBook(book);
    }

    @DeleteMapping(path = "book/remove/{title}", produces = "application/json")
    public ResponseEntity<Map<String, String>> removeBook(@CookieValue(value = "JSESSION") String sessionID, @PathVariable("title") String title) {
        isSessionValid(sessionID);

        return adminService.removeBook(title);
    }

    @PutMapping(path = "book/update/{title}", produces = "application/json")
    public ResponseEntity<Map<String, String>> updateBook(@CookieValue(value = "JSESSION") String sessionID, @PathVariable("title") String title, @RequestBody Book book) {
        isSessionValid(sessionID);

        return adminService.updateBook(title, book);
    }

    @GetMapping(path = "book/list", produces = "application/json")
    public ResponseEntity<Map<String, List<Book>>> getAllBooks(@CookieValue(value = "JSESSION") String sessionID) {
        isSessionValid(sessionID);

        Map<String, List<Book>> response = new HashMap<>();
        response.put("result", adminService.getAllBooks());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "borrow/get-all-borrows", produces = "application/json")
    public ResponseEntity<Map<String, List<BorrowDetails>>> getAllBorrowDetails(@CookieValue(value = "JSESSION") String sessionID) {
        isSessionValid(sessionID);

        return adminService.getAllBorrowDetails();
    }

    @PutMapping(path = "update-permission", produces = "application/json")
    public ResponseEntity<Map<String, String>> updateUserMembership(@CookieValue(value = "JSESSION") String sessionID, @RequestParam("email") String email, @RequestParam("membership") Membership membership) {
        isSessionValid(sessionID);

        return adminService.updateUserMembership(email, membership);
    }
}
