package com.grad.project.nc.persistence;

import com.grad.project.nc.model.UserOLD;
import com.grad.project.nc.model.mapper.UserOLDRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;

@Component
public class UserOLDDao {
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
        final String INSERT_QUERY = "INSERT INTO users(" +
                "username, password, roles, accountnonexpired, accountnonlocked, credentialsnotexpired, enabled)" +
                " VALUES (?, ?, '{USER}', TRUE, TRUE, TRUE, TRUE);";


        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps =
                                connection.prepareStatement(INSERT_QUERY, new String[]{"id"});
                        ps.setString(1, userOLD.getUsername());
                        ps.setString(2, userOLD.getPassword());
                        return ps;
                    }
                },
                keyHolder);

        userOLD.setId(keyHolder.getKey().longValue());


    }
}
