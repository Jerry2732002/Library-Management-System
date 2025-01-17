package com.example.Library_Management_System.dto.rowmapper;

import com.example.Library_Management_System.dto.User;
import com.example.Library_Management_System.enums.Membership;
import com.example.Library_Management_System.enums.Role;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new User(rs.getInt("UserID") ,rs.getString("Email"),
                rs.getString("Password"), rs.getString("FirstName"),
                rs.getString("LastName"),Membership.valueOf(rs.getString("Membership"))
        );
    }
}
