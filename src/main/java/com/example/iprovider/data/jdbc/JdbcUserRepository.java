package com.example.iprovider.data.jdbc;

import com.example.iprovider.data.UserRepository;
import com.example.iprovider.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcUserRepository implements UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public JdbcUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User findByUsername(String username) {
        List<User> users = jdbcTemplate.query(
                "select * from \"user\" u where u.email=?", this::mapRowToUser, username
        );
        return users.size() == 0 ? null : users.get(0);
    }

    private User mapRowToUser(ResultSet row, int rowNum) throws SQLException {
        return new User(
                row.getLong("user_id"),
                row.getString("email"),
                row.getString("pass"),
                row.getDate("registration_date"),
                User.RoleType.valueOf(row.getString("user_role").toUpperCase()),
                User.UserStatusType.valueOf(row.getString("user_status").toUpperCase()),
                row.getDouble("user_balance"),
                row.getString("firstname"),
                row.getString("surname"),
                row.getString("telephone_number")
        );
    }
}
