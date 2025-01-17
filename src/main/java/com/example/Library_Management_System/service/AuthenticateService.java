package com.example.Library_Management_System.service;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class AuthenticateService {
    private String salt;

    public AuthenticateService() {
        generateSalt();
    }

    private void generateSalt() {
        salt = BCrypt.gensalt(12);
    }

    public String encodePassword(String password) {
        return BCrypt.hashpw(password, salt);
    }

    public boolean checkPassword(String password, String encoded_password) {
        return BCrypt.checkpw(password, encoded_password);
    }
}
