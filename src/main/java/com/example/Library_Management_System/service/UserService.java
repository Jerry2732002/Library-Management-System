package com.example.Library_Management_System.service;

import com.example.Library_Management_System.dto.User;
import com.example.Library_Management_System.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticateService authenticateService;

    @Autowired
    public UserService(UserRepository userRepository, AuthenticateService authenticateService) {
        this.userRepository = userRepository;
        this.authenticateService = authenticateService;
    }

    public ResponseEntity<Map<String, String>> registerUser(User user) {
        String hashed_password = authenticateService.encodePassword(user.getPassword());
        user.setPassword(hashed_password);
        return userRepository.registerUser(user);
    }

    public ResponseEntity<Map<String, String>> deleteUser(int userID) {
        return userRepository.deleteUser(userID);
    }


    public ResponseEntity<List<User>> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public ResponseEntity<Map<String, String>> userLogin(User user) {
        User original = userRepository.findUserByEmail(user.getEmail());

        if (original == null) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Invalid Email(Email not found)");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        if (authenticateService.checkPassword(user.getPassword(),original.getPassword())) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Login Successful");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Incorrect password");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }
}
