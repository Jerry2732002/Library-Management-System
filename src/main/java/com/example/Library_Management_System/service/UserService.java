package com.example.Library_Management_System.service;

import com.example.Library_Management_System.dto.Book;
import com.example.Library_Management_System.dto.Borrow;
import com.example.Library_Management_System.dto.BorrowDetails;
import com.example.Library_Management_System.dto.User;
import com.example.Library_Management_System.repository.BookRepository;
import com.example.Library_Management_System.repository.BorrowRepository;
import com.example.Library_Management_System.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticateService authenticateService;
    private final BorrowRepository borrowRepository;
    private final BookRepository bookRepository;

    @Autowired
    public UserService(UserRepository userRepository, AuthenticateService authenticateService, BorrowRepository borrowRepository, BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.authenticateService = authenticateService;
        this.borrowRepository = borrowRepository;
        this.bookRepository = bookRepository;
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
        if (authenticateService.checkPassword(user.getPassword(), original.getPassword())) {

            Map<String, String> response = new HashMap<>();
            response.put("message", "Login Successful");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {

            Map<String, String> response = new HashMap<>();
            response.put("message", "Incorrect password");

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Map<String, String>> borrowBook(String email, String title) {
        Book book = bookRepository.getBookByTitle(title);
        if (book == null) {
            throw new RuntimeException("Book Not Found");
        }
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new RuntimeException("User Not Found");
        }

        int userID = user.getUserID();
        int bookID = book.getBookID();

        if (borrowRepository.borrowBook(new Borrow(userID, bookID, Date.valueOf(LocalDate.now())))) {
            bookRepository.updateBookCount(title, book.getCopiesAvailable() - 1);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Borrow Successful");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {

            Map<String, String> response = new HashMap<>();
            response.put("message", "Failed to Borrow");

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

    }

    public int checkForFine(User user, Book book) {
        Borrow borrow = borrowRepository.getBorrow(user.getUserID(), book.getBookID());

        LocalDate borrowDate = borrow.getBorrowDate().toLocalDate();
        LocalDate returnDate = borrow.getReturnDate().toLocalDate();

        int allowedDays = book.isRare() ? 7 : user.getMembership().getDayLimit();
        long difference = ChronoUnit.DAYS.between(returnDate, borrowDate);

        if (difference > allowedDays) {
            return (int) difference - allowedDays;
        }
        return -1;
    }

    public ResponseEntity<Map<String, String>> returnBook(String email, String title) {
        Book book = bookRepository.getBookByTitle(title);
        if (book == null) {
            throw new RuntimeException("Book Not Found");
        }
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new RuntimeException("User Not Found");
        }

        int userID = user.getUserID();
        int bookID = book.getBookID();

        Borrow borrow = new Borrow();
        borrow.setBookID(bookID);
        borrow.setUserID(userID);
        borrow.setBorrowDate(Date.valueOf(LocalDate.now()));

        if (borrowRepository.returnBook(borrow)) {
            bookRepository.updateBookCount(title, book.getCopiesAvailable() + 1);
            Map<String, String> response = new HashMap<>();
            int fine = checkForFine(user, book);
            if (fine > 0) {
                borrowRepository.updateFine(userID,bookID,fine);
                response.put("message", "Book Return After Due Date. Fine: " + fine + "$");
            } else {
                response.put("message", "Book Return Successful");
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Failed to Return Book");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<List<BorrowDetails>> getBooksBorrowedByEmail(String email) {
        return userRepository.getBooksBorrowedByEmail(email);
    }


}
