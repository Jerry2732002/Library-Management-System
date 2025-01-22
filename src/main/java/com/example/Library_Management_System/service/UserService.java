package com.example.Library_Management_System.service;

import com.example.Library_Management_System.dto.*;
import com.example.Library_Management_System.enums.Category;
import com.example.Library_Management_System.repository.BookRepository;
import com.example.Library_Management_System.repository.BorrowRepository;
import com.example.Library_Management_System.repository.SessionRepository;
import com.example.Library_Management_System.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
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
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final AuthenticateService authenticateService;
    private final BorrowRepository borrowRepository;
    private final BookRepository bookRepository;
    private final SessionRepository sessionRepository;

    @Autowired
    public UserService(UserRepository userRepository, AuthenticateService authenticateService, BorrowRepository borrowRepository, BookRepository bookRepository, SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.authenticateService = authenticateService;
        this.borrowRepository = borrowRepository;
        this.bookRepository = bookRepository;
        this.sessionRepository = sessionRepository;
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


    public ResponseEntity<Map<String, String>> userLogin(User user, HttpSession session) {
        User original = userRepository.findUserByEmail(user.getEmail());

        if (original == null) {

            Map<String, String> response = new HashMap<>();
            response.put("message", "Invalid Email(Email not found)");

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        if (authenticateService.checkPassword(user.getPassword(), original.getPassword())) {
            int userID = userRepository.findUserByEmail(user.getEmail()).getUserID();
            if(!sessionRepository.checkUserExist(userID)){
                sessionRepository.addSession(userID, session.getId());
                session.invalidate();
            } else {
                sessionRepository.updateSession(userID, session.getId());
            }

            Map<String, String> response = new HashMap<>();
            response.put("message", "Login Successful");
            response.put("SessionID", session.getId());

            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {

            Map<String, String> response = new HashMap<>();
            response.put("message", "Incorrect password");

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Map<String, String>> borrowBook(int userID, String title) {
        Book book = bookRepository.getBookByTitle(title);
        if (book == null) {
            throw new RuntimeException("Book Not Found");
        }
        User user = userRepository.findUserByID(userID);
        if (user == null) {
            throw new RuntimeException("User Not Found");
        }

        int bookID = book.getBookID();

        if (borrowRepository.borrowBook(new Borrow(userID, bookID, Date.valueOf(LocalDate.now())))) {
            try {

                bookRepository.updateBookCount(title, book.getCopiesAvailable() - 1);

                Map<String, String> response = new HashMap<>();
                response.put("message", "Borrow Successful");

                return new ResponseEntity<>(response, HttpStatus.OK);
            } catch (RuntimeException e) {
                Map<String, String> response = new HashMap<>();
                response.put("message", e.getMessage());

                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } else {

            Map<String, String> response = new HashMap<>();
            response.put("message", "Failed to Borrow");

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

    }

    public int checkForFine(Borrow borrow, Book book, User user) {
        LocalDate borrowDate = borrow.getBorrowDate().toLocalDate();
        LocalDate returnDate = borrow.getReturnDate().toLocalDate();

        int allowedDays = book.isRare() ? 7 : user.getMembership().getDayLimit();
        long difference = ChronoUnit.DAYS.between(borrowDate, returnDate);

        if (difference > allowedDays) {
            return (int) difference - allowedDays;
        }
        return -1;
    }

    public ResponseEntity<Map<String, String>> returnBook(int userID, String title) {
        Book book = bookRepository.getBookByTitle(title);
        if (book == null) {
            throw new RuntimeException("Book Not Found");
        }
        User user = userRepository.findUserByID(userID);
        if (user == null) {
            throw new RuntimeException("User Not Found");
        }


        int bookID = book.getBookID();

        Borrow borrow = borrowRepository.getBorrow(userID, bookID);

        if (borrow == null) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Book Not Borrowed");

            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        int borrowID = borrow.getBorrowID();
        if (!borrowRepository.checkIfBookReturned(borrowID)) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Book Already Returned");

            return new ResponseEntity<>(response, HttpStatus.OK);
        }

        borrow.setReturnDate(Date.valueOf(LocalDate.now()));

        if (borrowRepository.returnBook(borrow)) {
            bookRepository.updateBookCount(title, book.getCopiesAvailable() + 1);
            Map<String, String> response = new HashMap<>();
            int fine = checkForFine(borrow, book, user);
            if (fine > 0) {
                borrowRepository.updateFine(borrowID, fine);
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

    public ResponseEntity<Map<String, List<BorrowDetails>>> getBooksBorrowedByEmail(int userID) {
        return userRepository.getBooksBorrowedByEmail(userID);
    }

    public List<Book> getBookByCategory(Category category) {
        return bookRepository.getAllBooks().stream().filter(book -> book.getCategory() == category).collect(Collectors.toList());
    }

    public List<Book> getBookByAuthor(String author) {
        return bookRepository.getAllBooks().stream().filter(book -> book.getAuthor().equalsIgnoreCase(author)).collect(Collectors.toList());
    }

    public List<Book> getBookByTitle(String title) {
        return bookRepository.getAllBooks().stream().filter(book -> book.getTitle().equalsIgnoreCase(title)).collect(Collectors.toList());
    }

    public List<Book> getAllBooks() {
        return bookRepository.getAllBooks();
    }

    public ResponseEntity<Map<String, String>> adminLogin(User admin) {
        Admin original = userRepository.findAdminByEmail(admin.getEmail());

        if (original == null) {

            Map<String, String> response = new HashMap<>();
            response.put("message", "Invalid Email(Email not found)");

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        if (authenticateService.checkPassword(admin.getPassword(), original.getPassword())) {

            Map<String, String> response = new HashMap<>();
            response.put("message", "Login Successful");

            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {

            Map<String, String> response = new HashMap<>();
            response.put("message", "Incorrect password");

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
