package com.grad.project.nc.persistence;

import com.grad.project.nc.model.User;
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
        return null;
    }

    @Transactional
    public void createUser(User user) {
    }
}
