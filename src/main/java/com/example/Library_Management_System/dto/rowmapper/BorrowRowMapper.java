package com.example.Library_Management_System.dto.rowmapper;

import com.example.Library_Management_System.dto.Borrow;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BorrowRowMapper implements RowMapper<Borrow> {
    @Override
    public Borrow mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Borrow(rs.getInt("BorrowID"), rs.getInt("UserID"), rs.getInt("BookID"), rs.getDate("BorrowDate"), rs.getDate("ReturnDate"), rs.getDouble("Fine"));
    }
}
