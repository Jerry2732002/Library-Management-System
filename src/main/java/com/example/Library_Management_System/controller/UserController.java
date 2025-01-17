package com.example.Library_Management_System.controller;

import com.example.Library_Management_System.dto.User;
import com.example.Library_Management_System.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}
