package com.example.Library_Management_System.repository;

import com.example.Library_Management_System.dto.Borrow;
import com.example.Library_Management_System.dto.rowmapper.BorrowRowMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Date;
import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BorrowRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private BorrowRepository borrowRepository;

    @Test
    void testGetBorrowFound() {
        int userID = 1;
        int bookID = 1;
        Borrow borrow = new Borrow(1, userID, bookID, null, null,0);

        String sql = "SELECT * FROM Borrows WHERE BookID = ? AND UserID = ? AND ReturnDate IS NULL LIMIT 1";
        when(jdbcTemplate.queryForObject(eq(sql), eq(new Object[]{bookID, userID}), any(BorrowRowMapper.class))).thenReturn(borrow);

        Borrow result = borrowRepository.getBorrow(userID, bookID);

        assertNotNull(result);
        assertEquals(userID, result.getUserID());
        assertEquals(bookID, result.getBookID());
    }

    @Test
    void testGetBorrowNotFound() {
        int userID = 1;
        int bookID = 1;

        String sql = "SELECT * FROM Borrows WHERE BookID = ? AND UserID = ? AND ReturnDate IS NULL LIMIT 1";
        when(jdbcTemplate.queryForObject(eq(sql), eq(new Object[]{bookID, userID}), any(BorrowRowMapper.class)))
                .thenThrow(new EmptyResultDataAccessException(1));

        Borrow result = borrowRepository.getBorrow(userID, bookID);

        assertNull(result);
    }

    @Test
    void testUpdateFineSuccess() {
        int borrowID = 1;
        int fine = 50;

        String sql = "UPDATE Borrows SET Fine = ? WHERE BorrowID = ?";
        when(jdbcTemplate.update(eq(sql), eq(fine), eq(borrowID))).thenReturn(1);

        boolean result = borrowRepository.updateFine(borrowID, fine);

        assertTrue(result);
    }

    @Test
    void testUpdateFineFailure() {
        int borrowID = 1;
        int fine = 50;

        String sql = "UPDATE Borrows SET Fine = ? WHERE BorrowID = ?";
        when(jdbcTemplate.update(eq(sql), eq(fine), eq(borrowID))).thenReturn(0);

        boolean result = borrowRepository.updateFine(borrowID, fine);

        assertFalse(result);
    }

    @Test
    void testBorrowBookSuccess() {
        Borrow borrow = new Borrow(1, 1, 1, Date.valueOf(LocalDate.now()), null,0);

        String sql = "INSERT INTO Borrows (UserID, BookID, BorrowDate) VALUES (?,?,?)";
        when(jdbcTemplate.update(eq(sql), eq(borrow.getUserID()), eq(borrow.getBookID()), eq(borrow.getBorrowDate()))).thenReturn(1);

        boolean result = borrowRepository.borrowBook(borrow);

        assertTrue(result);
    }

    @Test
    void testBorrowBookFailure() {
        Borrow borrow = new Borrow(1, 1, 1, Date.valueOf(LocalDate.now()), null,0);

        String sql = "INSERT INTO Borrows (UserID, BookID, BorrowDate) VALUES (?,?,?)";
        when(jdbcTemplate.update(eq(sql), eq(borrow.getUserID()), eq(borrow.getBookID()), eq(borrow.getBorrowDate()))).thenReturn(0);

        boolean result = borrowRepository.borrowBook(borrow);

        assertFalse(result);
    }

    @Test
    void testReturnBookSuccess() {
        Borrow borrow = new Borrow(1, 1, 1, Date.valueOf(LocalDate.now()), Date.valueOf(LocalDate.now()), 0);

        String sql = "UPDATE Borrows SET ReturnDate = ? WHERE BorrowID = ?";
        when(jdbcTemplate.update(eq(sql), eq(borrow.getReturnDate()), eq(borrow.getBorrowID()))).thenReturn(1);

        boolean result = borrowRepository.returnBook(borrow);

        assertTrue(result);
    }

    @Test
    void testReturnBookFailure() {
        Borrow borrow = new Borrow(1, 1, 1, Date.valueOf(LocalDate.now()), Date.valueOf(LocalDate.now()), 0);

        String sql = "UPDATE Borrows SET ReturnDate = ? WHERE BorrowID = ?";
        when(jdbcTemplate.update(eq(sql), eq(borrow.getReturnDate()), eq(borrow.getBorrowID()))).thenReturn(0);

        boolean result = borrowRepository.returnBook(borrow);

        assertFalse(result);
    }

    @Test
    void testCheckIfBookReturnedTrue() {
        int borrowID = 1;
        Borrow borrow = new Borrow(borrowID, 1, 1, Date.valueOf(LocalDate.now()), null, 0);

        String sql = "SELECT * FROM Borrows WHERE BorrowID = ?";
        when(jdbcTemplate.queryForObject(eq(sql), eq(new Object[]{borrowID}), any(BorrowRowMapper.class))).thenReturn(borrow);

        boolean result = borrowRepository.checkIfBookReturned(borrowID);

        assertTrue(result);
    }

    @Test
    void testCheckIfBookReturnedFalse() {
        int borrowID = 1;
        Borrow borrow = new Borrow(borrowID, 1, 1, Date.valueOf(LocalDate.now()),Date.valueOf(LocalDate.now()),0);

        String sql = "SELECT * FROM Borrows WHERE BorrowID = ?";
        when(jdbcTemplate.queryForObject(eq(sql), eq(new Object[]{borrowID}), any(BorrowRowMapper.class))).thenReturn(borrow);

        boolean result = borrowRepository.checkIfBookReturned(borrowID);
        assertFalse(result);

    }
}
