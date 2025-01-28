package com.example.Library_Management_System.exception_handler;

import com.example.Library_Management_System.exception.IncorrectPasswordException;
import com.example.Library_Management_System.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class LibraryExceptionHandler {
    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<Map<String, String>> incorrectPasswordHandler() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Incorrect Password");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> userNotFoundHandler() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "User not found");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
}
