package com.example.Library_Management_System.dto.rowmapper;

import com.example.Library_Management_System.dto.BorrowDetails;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BorrowDetailsRowMapper implements RowMapper<BorrowDetails> {
    @Override
    public BorrowDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new BorrowDetails(
                rs.getString("Email"),
                rs.getString("FirstName"),
                rs.getString("LastName"),
                rs.getString("Membership"),
                rs.getString("Title"),
                rs.getString("Author"),
                rs.getString("Category"),
                rs.getBoolean("Rare"),
                rs.getInt("CopiesAvailable"),
                rs.getDate("BorrowDate"),
                rs.getDate("ReturnDate"),
                rs.getDouble("Fine")
        );
    }
}
