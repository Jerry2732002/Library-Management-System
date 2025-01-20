package com.example.Library_Management_System.repository;

import com.example.Library_Management_System.dto.Book;
import com.example.Library_Management_System.dto.Borrow;
import com.example.Library_Management_System.dto.rowmapper.BorrowRowMapper;
import com.example.Library_Management_System.dto.rowmapper.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class BorrowRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BorrowRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Borrow getBorrow(int userID, int bookID) {
        String sql = "SELECT * FROM Borrows WHERE BookID = ? AND UserID = ? LIMIT 1";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{bookID,userID}, new BorrowRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public boolean updateFine(int userID, int bookID, int fine) {
        String sql = "UPDATE Borrows SET Fine = ? WHERE UserID = ? AND BookID = ?";
        int rowsAffected = jdbcTemplate.update(sql, fine, userID, bookID);
        return rowsAffected == 1;
    }

    public boolean borrowBook(Borrow borrow) {
        String sql = "INSERT INTO Borrows (UserID, BookID, BorrowDate) VALUES (?,?,?)";
        int rowsAffected = jdbcTemplate.update(sql, borrow.getUserID(), borrow.getBookID(), borrow.getBorrowDate());
        return rowsAffected == 1;
    }

    public boolean returnBook(Borrow borrow) {
        String sql = "UPDATE Borrows SET ReturnDate = ? WHERE UserID = ? AND BookID = ?";
        int rowsAffected = jdbcTemplate.update(sql, borrow.getReturnDate(), borrow.getUserID(), borrow.getBookID());
        return rowsAffected == 1;
    }

}
