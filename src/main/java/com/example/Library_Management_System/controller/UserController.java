package com.example.Library_Management_System.controller;

import com.example.Library_Management_System.dto.BorrowDetails;
import com.example.Library_Management_System.dto.User;
import com.example.Library_Management_System.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/register", produces = "application/json")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping(path = "/login", produces = "application/json")
    public ResponseEntity<Map<String, String>> userLogin(@RequestBody User user) {
        return userService.userLogin(user);
    }

    @GetMapping(path = "/borrow-list/{email}", produces = "application/json")
    public ResponseEntity<List<BorrowDetails>> getBooksBorrowedByEmail(@PathVariable("email") String email) {
        return userService.getBooksBorrowedByEmail(email);
    }

    @PostMapping(path = "/borrow")
    public ResponseEntity<Map<String, String>> borrowBook(@RequestParam("email") String email, @RequestParam("title") String title) {
        return userService.borrowBook(email,title);
    }

    @PostMapping(path = "/return")
    public ResponseEntity<Map<String, String>> returnBook(@RequestParam("email") String email, @RequestParam("title") String title) {
        return userService.returnBook(email, title);
    }

}
