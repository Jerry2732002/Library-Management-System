package com.example.Library_Management_System.aspect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpSession;
import com.example.Library_Management_System.dto.User;

@Aspect
@Component
public class LoggerAspect {

    private static final Logger logger = LogManager.getLogger(LoggerAspect.class);

    @Before("execution(* com.example.Library_Management_System.service.UserService.userLogin(..)) && args(user, session)")
    public void logUserLoginAttempt(JoinPoint joinPoint, User user, HttpSession session) {
        logger.info("User login attempt. Email: {}", user.getEmail());
    }

    @After("execution(* com.example.Library_Management_System.service.UserService.userLogin(..)) && returnValue(*))")
    public void logUserLoginSuccess(JoinPoint joinPoint) {
        logger.info("User login successful. Email: {}", getUserEmailFromArgs(joinPoint));
    }

    @AfterThrowing(pointcut = "execution(* com.example.Library_Management_System.service.UserService.userLogin(..))", throwing = "ex")
    public void logUserLoginFailure(JoinPoint joinPoint, Exception ex) {
        logger.error("User login failed. Email: {} Error: {}", getUserEmailFromArgs(joinPoint), ex.getMessage());
    }

    @Before("execution(* com.example.Library_Management_System.service.UserService.borrowBook(..)) && args(userID, title)")
    public void logBookBorrowing(JoinPoint joinPoint, int userID, String title) {
        logger.info("User with ID: {} is attempting to borrow book: {}", userID, title);
    }

    @Before("execution(* com.example.Library_Management_System.service.UserService.returnBook(..)) && args(userID, title)")
    public void logBookReturning(JoinPoint joinPoint, int userID, String title) {
        logger.info("User with ID: {} is attempting to return book: {}", userID, title);
    }

    @Before("execution(* com.example.Library_Management_System.service.AdminService.addBook(..)) && args(book)")
    public void logAddBookAction(JoinPoint joinPoint, Object book) {
        logger.info("Admin is adding a new book: {}", book.toString());
    }

    @Before("execution(* com.example.Library_Management_System.service.AdminService.removeBook(..)) && args(title)")
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

