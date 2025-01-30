package com.example.Library_Management_System.aspect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpSession;
import com.example.Library_Management_System.dto.User;

import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class LoggerAspect {

    private static final Logger logger = LogManager.getLogger(LoggerAspect.class);

    @Before("execution(* com.example.Library_Management_System.service.UserService.userLogin(..)) && args(user, session)")
    public void logUserLoginAttempt(JoinPoint joinPoint, User user, HttpSession session) {
        logger.info("User login attempt. Email: {}", user.getEmail());
    }

    @AfterReturning("execution(* com.example.Library_Management_System.service.UserService.userLogin(..))")
    public void logUserLoginSuccess(JoinPoint joinPoint) {
        logger.info("User login successful. Email: {}", getUserEmailFromArgs(joinPoint));
    }

    @AfterThrowing(pointcut = "execution(* com.example.Library_Management_System.service.UserService.userLogin(..))", throwing = "ex")
    public void logUserLoginFailure(JoinPoint joinPoint, Exception ex) {
        logger.error("User login failed. Email: {} Error: {}", getUserEmailFromArgs(joinPoint), ex.getMessage());

    }

    @After("execution(* com.example.Library_Management_System.service.UserService.borrowBook(..)) && args(userID, title)")
    public void logBookBorrowing(JoinPoint joinPoint, int userID, String title) {
        logger.info("User with ID: {} borrowed book: {}", userID, title);
    }

    @After("execution(* com.example.Library_Management_System.service.UserService.returnBook(..)) && args(userID, title)")
    public void logBookReturning(JoinPoint joinPoint, int userID, String title) {
        logger.info("User with ID: {} retruned book: {}", userID, title);
    }

    @After("execution(* com.example.Library_Management_System.service.AdminService.addBook(..)) && args(book)")
    public void logAddBookAction(JoinPoint joinPoint, Object book) {
        logger.info("Admin is adding a new book: {}", book.toString());
    }

    @After("execution(* com.example.Library_Management_System.service.AdminService.removeBook(..)) && args(title)")
    public void logRemoveBookAction(JoinPoint joinPoint, String title) {
        logger.info("Admin is removing book with title: {}", title);
    }

    @AfterThrowing(pointcut = "execution(* com.example.Library_Management_System..*(..))", throwing = "ex")
    public void logSystemError(JoinPoint joinPoint, Exception ex) {
        logger.error("System error in method: {} Error: {}", joinPoint.getSignature().toString(), ex.getMessage());
    }

    private String getUserEmailFromArgs(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args != null && args.length > 0 && args[0] instanceof User) {
            return ((User) args[0]).getEmail();
        }
        return "Unknown";
    }
}

