package com.grad.project.nc.persistence;

import com.grad.project.nc.model.UserOLD;
import com.grad.project.nc.model.mapper.UserOLDRowMapper;
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
    public Optional<UserOLD> findByUsername(String username) {
        String sql = "SELECT * FROM users where username = ?;";
        return Optional.ofNullable(jdbcTemplate.queryForObject(
                sql,
                new Object[]{username},
                new UserOLDRowMapper()));
    }

    @Transactional
    public void createUser(UserOLD userOLD) {
        String sql = "INSERT INTO users(" +
                "username, password, roles, accountnonexpired, accountnonlocked, credentialsnotexpired, enabled)" +
                " VALUES (?, ?, '{USER}', true, true, true, true);";
        jdbcTemplate.update(sql, new Object[]{userOLD.getUsername(), userOLD.getPassword()});
    }
}
