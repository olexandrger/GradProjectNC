package com.grad.project.nc.persistence;

import com.grad.project.nc.model.User;
import com.grad.project.nc.model.mapper.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class UserDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Transactional
    public Optional<User> findByUsername(String username) {
        String sql = "SELECT * FROM users where username = ?;";
        return Optional.ofNullable(jdbcTemplate.queryForObject(
                sql,
                new Object[]{username},
                new UserRowMapper()));
    }

    @Transactional
    public void createUser(User user) {
        String sql = "INSERT INTO users(" +
                "username, password, roles, accountnonexpired, accountnonlocked, credentialsnotexpired, enabled)" +
                " VALUES (?, ?, '{USER}', true, true, true, true);";
        jdbcTemplate.update(sql, new Object[]{user.getUsername(), user.getPassword()});
    }
}
