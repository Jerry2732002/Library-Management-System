package com.example.Library_Management_System.exception_handler;

import com.example.Library_Management_System.exception.AdminPermissionException;
import com.example.Library_Management_System.exception.IncorrectPasswordException;
import com.example.Library_Management_System.exception.SessionExpiredException;
import com.example.Library_Management_System.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class LibraryExceptionHandler {
    Map<String, String> response = new HashMap<>();

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<Map<String, String>> incorrectPasswordHandler(IncorrectPasswordException ex) {

        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> userNotFoundHandler(UserNotFoundException ex) {
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(SessionExpiredException.class)
    public ResponseEntity<Map<String, String>> sessionExpiredHandler(SessionExpiredException ex) {
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AdminPermissionException.class)
    public ResponseEntity<Map<String, String>> handleAdminPermission(AdminPermissionException ex) {
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }
}