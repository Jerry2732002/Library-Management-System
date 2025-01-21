package com.example.Library_Management_System.repository;

import com.example.Library_Management_System.dto.Book;
import com.example.Library_Management_System.dto.rowmapper.BookRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class BookRepository {

    JdbcTemplate jdbcTemplate;

    @Autowired
    public BookRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Book getBookByTitle(String title) {
        String sql = "SELECT * FROM Books WHERE Title = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{title}, new BookRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<Book> getAllBooks() {
        String sql = "SELECT * FROM Books";
        try {
            return jdbcTemplate.query(sql, new BookRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public ResponseEntity<Map<String, String>> updateBook(String title, Book book) {
        String sql = "UPDATE Books SET Title = ?, Author = ?, Category = ?, Rare = ?, CopiesAvailable = ? WHERE Title = ?";

        int rowsAffected = jdbcTemplate.update(sql, book.getTitle(), book.getAuthor(), book.getCategory().name(), book.isRare(), book.getCopiesAvailable(), title);
        Map<String, String> response = new HashMap<>();

        if (rowsAffected == 1) {
            response.put("message", "Book Updated Successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("message", "Failed To Update Book");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Map<String, String>> updateBookCount(String title, int newAvailableCopies) {
        String sql = "UPDATE Books SET CopiesAvailable = ? WHERE Title = ?";
        if (newAvailableCopies < 0) {
            throw new RuntimeException("Book: " + title + " out of stock");
        }
        int rowsAffected = jdbcTemplate.update(sql, newAvailableCopies, title);
        Map<String, String> response = new HashMap<>();

        if (rowsAffected > 0) {
            response.put("message", "Available Copies Updated Successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("message", "Failed To Update Available Copies");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Map<String, String>> addBook(Book book) {
        Book existingBook = getBookByTitle(book.getTitle());

        if (existingBook == null) {
            String sql = "INSERT INTO Books (Title, Author, Category, Rare, CopiesAvailable) VALUES (?,?,?,?,?)";

            int rowsAffected = jdbcTemplate.update(sql, book.getTitle(), book.getAuthor(), book.getCategory().name(), book.isRare(), book.getCopiesAvailable());
            Map<String, String> response = new HashMap<>();

            if (rowsAffected == 1) {
                response.put("message", "Book Added Successfully");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                response.put("message", "Failed To Add Book");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } else {
            return updateBookCount(book.getTitle(), existingBook.getCopiesAvailable() + 1);
        }
    }

    public ResponseEntity<Map<String, String>> removeBook(String title) {
        String sql = "DELETE FROM Books WHERE Title = ?";

        int rowsAffected = jdbcTemplate.update(sql, title);
        Map<String, String> response = new HashMap<>();

        if (rowsAffected == 1) {
            response.put("message", "Book Updated Successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.put("message", "Failed To Update Book");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}