package com.grad.project.nc.persistence.impl;

import com.grad.project.nc.model.*;
import com.grad.project.nc.model.proxy.UserProxy;
import com.grad.project.nc.persistence.AbstractDao;
import com.grad.project.nc.persistence.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@Slf4j
public class UserDaoImpl extends AbstractDao implements UserDao {

    private static final String PK_COLUMN_NAME = "user_id";

    private final ObjectFactory<UserProxy> proxyFactory;

    @Autowired
    public UserDaoImpl(JdbcTemplate jdbcTemplate, ObjectFactory<UserProxy> proxyFactory) {
        super(jdbcTemplate);
        this.proxyFactory = proxyFactory;
    }

    @Override
    public User add(User user) {
        String insertQuery = "INSERT INTO \"user\" (\"email\", \"password\", \"first_name\", " +
                "\"last_name\", \"phone_number\") VALUES(?, ?, ?, ?, ?)";

        Long userId = executeInsertWithId(insertQuery, PK_COLUMN_NAME, user.getEmail(), user.getPassword(),
                user.getFirstName(), user.getLastName(), user.getPhoneNumber());

        user.setUserId(userId);

        return user;
    }

    @Override
    @Transactional
    public User update(User user) {
        String updateQuery = "UPDATE \"user\" SET \"email\" = ?, \"password\" = ?, " +
                "\"first_name\" = ?, \"last_name\" = ?, \"phone_number\" = ? WHERE \"user_id\" = ?";

        executeUpdate(updateQuery, user.getEmail(), user.getPassword(),
                user.getFirstName(), user.getLastName(), user.getPhoneNumber(), user.getUserId());

        deleteUserRoles(user.getUserId());
        persistUserRoles(user);

        return user;
    }
    @Transactional
    public User updateWithoutPassword(User user) {
        String updateQuery = "UPDATE \"user\" SET \"email\" = ? ," +
                "\"first_name\" = ?, \"last_name\" = ?, \"phone_number\" = ? WHERE \"user_id\" = ?";

        log.info("Updating user with email: " + user.getEmail() + ", First name: " + user.getFirstName() + ", Phone: "
                + user.getPhoneNumber() + ", ID= " + user.getUserId());

        executeUpdate(updateQuery, user.getEmail(),
                user.getFirstName(), user.getLastName(), user.getPhoneNumber(), user.getUserId());

        deleteUserRoles(user.getUserId());
        persistUserRoles(user);

        return user;
    }

    @Override
    public User find(Long id) {
        String findOneQuery = "SELECT \"user_id\", \"email\", \"password\", \"first_name\", " +
                "\"last_name\", \"phone_number\" FROM \"user\" WHERE \"user_id\" = ?";

        return findOne(findOneQuery, new UserRowMapper(), id);
    }

    @Override
    public List<User> findAll() {
        String findAllQuery = "SELECT \"user_id\", \"email\", \"password\", \"first_name\", " +
                "\"last_name\", \"phone_number\" FROM \"user\"";

        return findMultiple(findAllQuery, new UserRowMapper());
    }

    @Override
    public List<User> findByDomainId(Long domainId) {
        String query = "SELECT u.\"user_id\", u.\"email\", u.\"password\", u.\"first_name\", " +
                "u.\"last_name\", u.\"phone_number\" " +
                "FROM \"user\" u " +
                "INNER JOIN \"user_domain\" ud " +
                "ON u.\"user_id\" = ud.\"user_id\" " +
                "WHERE ud.\"domain_id\" = ?";

        return findMultiple(query, new UserRowMapper(), domainId);
    }

    @Override
    public void delete(Long id) {
        String deleteQuery = "DELETE FROM \"user\" WHERE \"user_id\" = ?";

        executeUpdate(deleteQuery, id);
    }

    @Override
    public void deleteUserRoles(Long userId) {
        String deleteQuery = "DELETE FROM \"user_role\" WHERE \"user_id\" = ?";

        executeUpdate(deleteQuery, userId);
    }

    @Override
    public void persistUserRoles(User user) {
        String insertQuery = "INSERT INTO \"user_role\" (\"user_id\", \"role_id\") VALUES(?, ?)";

        List<Object[]> batchArgs = new ArrayList<>();
        for (Role role : user.getRoles()) {
            batchArgs.add(new Object[]{user.getUserId(), role.getRoleId()});
        }

        batchUpdate(insertQuery, batchArgs);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String query = "SELECT \"user_id\", \"email\", \"password\", \"first_name\", " +
                "\"last_name\", \"phone_number\" FROM \"user\" WHERE \"email\" = ?";

        User user = findOne(query, new UserRowMapper(), email);

        return Optional.ofNullable(user);
    }

    @Override
    public User findUserByProductOrderId(Long productOrderId) {
        String query = "SELECT u.\"user_id\", u.\"email\", u.\"password\", u.\"first_name\", u.\"last_name\", " +
                "u.\"phone_number\" " +
                "FROM \"user\" u " +
                "JOIN \"product_order\" po " +
                "ON u.\"user_id\" = po.\"user_id\" " +
                "WHERE po.\"product_order_id\" = ?";

        return findOne(query, new UserRowMapper(), productOrderId);
    }

    @Override
    public User findResponsibleByProductOrderId(Long productOrderId) {
        String query = "SELECT u.\"user_id\", u.\"email\", u.\"password\", u.\"first_name\", u.\"last_name\", " +
                "u.\"phone_number\" " +
                "FROM \"user\" u " +
                "JOIN \"product_order\" po " +
                "ON u.\"user_id\" = po.\"responsible_id\" " +
                "WHERE po.\"product_order_id\" = ?";

        return findOne(query, new UserRowMapper(), productOrderId);
    }

    @Override
    public User findUserByComplainId(Long complainId) {
        String query = "SELECT u.\"user_id\", u.\"email\", u.\"password\", u.\"first_name\", u.\"last_name\", " +
                "u.\"phone_number\" " +
                "FROM \"user\" u " +
                "JOIN \"complain\" c " +
                "ON u.\"user_id\" = c.\"user_id\" " +
                "WHERE c.\"complain_id\" = ?";

        return findOne(query, new UserRowMapper(), complainId);
    }

    @Override
    public User findResponsibleByComplainId(Long complainId) {
        String query = "SELECT u.\"user_id\", u.\"email\", u.\"password\", u.\"first_name\", u.\"last_name\", " +
                "u.\"phone_number\" " +
                "FROM \"user\" u " +
                "JOIN \"complain\" c " +
                "ON u.\"user_id\" = c.\"responsible_id\" " +
                "WHERE c.\"complain_id\" = ?";

        return findOne(query, new UserRowMapper(), complainId);
    }

    @Override
    public void addUserRole(Long userId, Long roleId) {
        String insertQuery = "INSERT INTO \"user_role\" (\"user_id\", \"role_id\") VALUES(?, ?)";

        executeUpdate(insertQuery, userId, roleId);
    }

    @Override
    public void deleteUserRole(Long userId, Long roleId) {
        String deleteQuery = "DELETE FROM \"user_role\" WHERE \"user_id\" = ? AND \"role_id\" = ?";

        executeUpdate(deleteQuery, userId, roleId);
    }

    private class UserRowMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserProxy user = proxyFactory.getObject();

            user.setUserId(rs.getLong("user_id"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            user.setPhoneNumber(rs.getString("phone_number"));

            return user;
        }
    }
}
