package com.grad.project.nc.persistence;

import com.grad.project.nc.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@Repository
public class UserDao extends AbstractDao<User> {

    RoleDao roleDao;
    DomainDao domainDao;
    ProductOrderDao productOrderDao;
    ComplainDao complainDao;

    @Autowired
    UserDao(JdbcTemplate jdbcTemplate, RoleDao roleDao, DomainDao domainDao, ProductOrderDao productOrderDao,
            ComplainDao complainDao) {
        super(jdbcTemplate);

        this.roleDao = roleDao;
        this.domainDao = domainDao;
        this.complainDao = complainDao;
        this.productOrderDao = productOrderDao;
    }

    @Transactional
    private void saveUserRoles(User user) {
        user.getRoles(); //TODO addUserRole() and removeUserRole()
        executeUpdate(connection -> {
            String query = "DELETE FROM user_role WHERE user_id=?";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setLong(1, user.getUserId());

            return statement;
        });

        if (user.getRoles() != null && user.getRoles().size() > 0) {
            executeUpdate(connection -> {
                String query = "INSERT INTO user_role(user_id, role_id) VALUES (?, ?)";
                for (int i = 1; i < user.getRoles().size(); i++) {
                    query += ",(?, ?)";
                }

                PreparedStatement statement = connection.prepareStatement(query);

                int i = 1;
                for (Role role : user.getRoles()) {
                    statement.setLong(i, user.getUserId());
                    i++;
                    statement.setLong(i, role.getRoleId());
                    i++;
                }
                return statement;
            });
        }
    }

    @Transactional
    private void saveUserDomains(User user) {

        Collection<Domain> domains = user.getDomains(); //TODO
        executeUpdate(connection -> {
            String query = "DELETE  FROM user_domain WHERE user_id =?";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setLong(1, user.getUserId());
            return preparedStatement;
        });
        if (domains != null && domains.size() > 0) {
            executeUpdate(connection -> {
                String query = "INSERT INTO user_domain(user_id, domain_id) VALUES (?, ?)";
                for (int i = 1; i < domains.size(); i++) {
                    query += ",(?, ?)";
                }
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                int i = 0;
                for (Domain domain : domains) {
                    preparedStatement.setLong(++i, user.getUserId());
                    preparedStatement.setLong(++i, domain.getDomainId());
                }
                return preparedStatement;
            });


        }
    }

    @Override
    public User add(User user){
        KeyHolder keyHolder = executeInsert(connection -> {
            String statement = "INSERT INTO \"user\" (email, password, first_name, last_name, phone_number)" +
                    " VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getFirstName());
            preparedStatement.setString(4, user.getLastName());
            preparedStatement.setString(5, user.getPhoneNumber());

            return preparedStatement;
        });

        user.setUserId(getLongValue(keyHolder, "user_id"));

        saveUserRoles(user);

        return user;
    }

    @Override
    public User update(User user) {

        executeUpdate(connection -> {
            String query = "UPDATE \"user\" SET email = ?, password = ?," +
                "first_name = ? , last_name = ? , phone_number = ? WHERE user_id = ? ";

            PreparedStatement preparedStatement = connection.prepareStatement(query);

            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getFirstName());
            preparedStatement.setString(4, user.getLastName());
            preparedStatement.setString(5, user.getPhoneNumber());

            return preparedStatement;
        });

        saveUserRoles(user);

        return user;
    }

    @Override
    public User find(long id) {
        return findOne(connection -> {
            String statement = "SELECT user_id, email, password, first_name, last_name, phone_number FROM \"user\" WHERE user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setLong(1, id);

            return preparedStatement;
        }, new UserRowMapper());
    }

    @Override
    @Transactional
    public Collection<User> findAll() {
        return findMultiple(connection -> {
            String statement = "SELECT user_id, email, password, first_name, last_name, phone_number FROM \"user\"";
            return connection.prepareStatement(statement);
        }, new UserRowMapper());
    }

    @Override
    public void delete(User user) {
        executeUpdate(connection -> {
            String statement = "DELETE FROM \"user\" WHERE user_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setLong(1, user.getUserId());
            return preparedStatement;
        });
    }

    @Transactional
    public Optional<User> findByEmail(String email) {
        User result = findOne(connection -> {
            String statement = "SELECT user_id, email, password, first_name, last_name, phone_number FROM \"user\" WHERE email = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setString(1, email);

            return preparedStatement;
        }, new UserRowMapper());

        return Optional.ofNullable(result);
    }
/*
    @Transactional
    public void addUserRole(User user, Role role){

        SimpleJdbcInsert insertUserQuery = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("user_role");

        Map<String, Object> parameters = new HashMap<>(2);
        parameters.put("user_id", user.getUser_id());
        parameters.put("role_id", role.getRoleId());

        insertUserQuery.execute(parameters);
    }
*/
    private class UserProxy extends User {
        @Override
        public List<Role> getRoles() {
            if (super.getRoles() == null) {
                super.setRoles(new LinkedList<>(roleDao.getRolesByUser(this)));
            }

            return super.getRoles();
        }

        @Override
        public List<Domain> getDomains() {
            if (super.getDomains() == null) {
                super.setDomains(new LinkedList<>(domainDao.getDomainsByUser(this)));
            }

            return super.getDomains();
        }

        @Override
        public List<ProductOrder> getOrders() {
            if (super.getOrders() == null) {
                super.setOrders(new LinkedList<>(productOrderDao.getOrdersByUser(this)));
            }

            return super.getOrders();
        }

        @Override
        public List<Complain> getComplains() {
            if (super.getComplains() == null) {
                super.setComplains(new LinkedList<>(complainDao.getComplainsByUser(this)));
            }

            return super.getComplains();
        }
    }

    private final class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new UserProxy();

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
