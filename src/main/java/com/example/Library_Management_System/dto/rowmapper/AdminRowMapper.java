package com.example.Library_Management_System.dto.rowmapper;

import com.example.Library_Management_System.dto.Admin;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminRowMapper implements RowMapper<Admin> {
    @Override
    public Admin mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Admin(rs.getInt("UserID"),rs.getString("Email"), rs.getString("Password"));
    }
}
