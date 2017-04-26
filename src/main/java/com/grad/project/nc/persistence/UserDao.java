package com.grad.project.nc.persistence;

import com.grad.project.nc.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Roman Savuliak on 26.04.2017.
 */
public class UserDao implements CrudDao<User>{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional
    public User add(User user) {

        SimpleJdbcInsert insertUserQuery = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("user")
                .usingGeneratedKeyColumns("user_id");

        Map<String, Object> parameters = new HashMap<String, Object>(5);
        parameters.put("email", user.getEmail());
        parameters.put("password", user.getPassword());
        parameters.put("first_name", user.getFirstName());
        parameters.put("last_name", user.getLastName());
        parameters.put("phone_number", user.getPhoneNumber());

        Number newId = insertUserQuery.executeAndReturnKey(parameters);
        user.setUser_id(newId.longValue());
        return user;
    }

    @Override
    @Transactional
    public User update(User user) {
        final String UPDATE_QUERY = "UPDATE user SET" +
                " email = ?" +
                ", password = ?" +
                ", first_name = ? " +
                ", last_name = ? " +
                ", phone_number = ? " +
                "WHERE user_id = ? ";

        jdbcTemplate.update(UPDATE_QUERY, new Object[]{
                user.getEmail()
                ,user.getPassword()
                ,user.getFirstName()
                ,user.getLastName()
                ,user.getPhoneNumber()
                ,user.getUser_id()});

        return user;
    }

    @Override
    @Transactional
    public User find(long id) {
        final String SELECT_QUERY = "SELECT user_id, email, password, first_name, last_name, phone_number FROM user WHERE user_id = ?";
        User user = jdbcTemplate.queryForObject(SELECT_QUERY, new Object[]{id}, new UserRowMapper());
        return user;
    }

    @Override
    @Transactional
    public Collection<User> findAll() {
        final String SELECT_QUERY = "SELECT user_id, email, password, first_name, last_name, phone_number FROM user";
        return jdbcTemplate.query(SELECT_QUERY, new UserRowMapper());
    }

    @Override
    @Transactional
    public void delete(User user) {
        final String DELETE_QUERY = "DELETE FROM user WHERE user_id = ?";
        jdbcTemplate.update(DELETE_QUERY, user.getUser_id());
    }

    @Transactional
    public Optional<User> findByUsername(String email) {
        String sql = "SELECT * FROM users where email = ?;";
        return Optional.ofNullable(jdbcTemplate.queryForObject(
                sql,
                new Object[]{email},
                new UserRowMapper()));
    }

    private static final class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();

            user.setUser_id(rs.getLong("user_id"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            user.setPhoneNumber(rs.getString("phone_number"));

            return user;
        }
    }
}
