package com.example.Library_Management_System.repository;

import com.example.Library_Management_System.dto.Borrow;
import com.example.Library_Management_System.dto.BorrowDetails;
import com.example.Library_Management_System.dto.rowmapper.BorrowDetailsRowMapper;
import com.example.Library_Management_System.dto.rowmapper.BorrowRowMapper;
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
public class BorrowRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BorrowRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Borrow getBorrow(int userID, int bookID) {
        String sql = "SELECT * FROM Borrows WHERE BookID = ? AND UserID = ? AND ReturnDate IS NULL LIMIT 1";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{bookID,userID}, new BorrowRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public boolean updateFine(int borrowID, int fine) {
        String sql = "UPDATE Borrows SET Fine = ? WHERE BorrowID = ?";
        int rowsAffected = jdbcTemplate.update(sql, fine, borrowID);
        return rowsAffected > 0;
    }

    public boolean borrowBook(Borrow borrow) {
        String sql = "INSERT INTO Borrows (UserID, BookID, BorrowDate) VALUES (?,?,?)";
        int rowsAffected = jdbcTemplate.update(sql, borrow.getUserID(), borrow.getBookID(), borrow.getBorrowDate());
        return rowsAffected == 1;
    }

    public boolean returnBook(Borrow borrow) {
        String sql = "UPDATE Borrows SET ReturnDate = ? WHERE BorrowID = ?";
        int rowsAffected = jdbcTemplate.update(sql, borrow.getReturnDate(), borrow.getBorrowID());
        return rowsAffected > 0;
    }


    public boolean checkIfBookReturned(int borrowID) {
        String sql = "SELECT * FROM Borrows WHERE BorrowID = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{borrowID}, new BorrowRowMapper()).getReturnDate() == null;
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
    }

    public ResponseEntity<Map<String, List<BorrowDetails>>> getAllBorrowDetails() {
        String sql = "SELECT * FROM Users u\n" +
                "JOIN Borrows br ON br.UserID = u.UserID\n" +
                "JOIN Books b ON b.BookID = br.BookID;";

        try {
            List<BorrowDetails> borrows = jdbcTemplate.query(sql, new BorrowDetailsRowMapper());
            Map<String, List<BorrowDetails>> response = new HashMap<>();
            response.put("Result", borrows);
            return new ResponseEntity<>(response , HttpStatus.OK);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
