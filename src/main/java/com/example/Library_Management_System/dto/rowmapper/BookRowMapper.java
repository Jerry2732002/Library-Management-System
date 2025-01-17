package com.example.Library_Management_System.dto.rowmapper;

import com.example.Library_Management_System.dto.Book;
import com.example.Library_Management_System.enums.Category;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookRowMapper implements RowMapper<Book> {
    @Override
    public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Book(rs.getInt("BookID"), rs.getString("Title"),
                rs.getString("Author"),
                Category.valueOf(rs.getString("Category")),
                rs.getBoolean("Rare"), rs.getInt("CopiesAvailable"));
    }
}
