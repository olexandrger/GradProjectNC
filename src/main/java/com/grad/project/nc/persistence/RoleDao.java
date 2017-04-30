package com.grad.project.nc.persistence;

import com.grad.project.nc.model.Role;
import com.grad.project.nc.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

/**
 * Created by Roman Savuliak on 26.04.2017.
 */
@Repository
public class RoleDao extends AbstractDao<Role> {


    //JdbcTemplate jdbcTemplate;


    @Autowired
    RoleDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public Role add(Role role) {

        KeyHolder keyHolder= executeInsert(connection -> {
            //language=GenericSQL
            String query = "INSERT INTO role (role.role_name) VALUES (?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, role.getRoleName());
            return preparedStatement;
        });

        role.setRoleId(getLongValue(keyHolder, "role_id"));

        return role;
    }

    @Override
    @Transactional
    public Role update(Role role) {
        executeUpdate(connection -> {
            final String UPDATE_QUERY = "UPDATE role SET role_name = ? WHERE role_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_QUERY);
            preparedStatement.setString(1, role.getRoleName());
            preparedStatement.setLong(2, role.getRoleId());
            return preparedStatement;
        });
        return role;
    }

    @Override
    public Role find(long id) {

        return findOne(connection -> {
            final String SELECT_QUERY = "SELECT role_id, role_name FROM role WHERE role_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERY);
            preparedStatement.setLong(1, id);
            return preparedStatement;
        }, new RoleRowMapper());

    }

    @Override
    public Collection<Role> findAll() {
        return findMultiple(connection -> {
            final String SELECT_QUERY = "SELECT role_id, role_name FROM role";
            return connection.prepareStatement(SELECT_QUERY);
        }, new RoleRowMapper());

    }


    @Override
    @Transactional
    public void delete(Role role) {
        executeUpdate(connection -> {
            final String DELETE_QUERY = "DELETE FROM role WHERE role_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_QUERY);
            preparedStatement.setLong(1, role.getRoleId());
            return preparedStatement;
        });
    }

    public Role getRoleByName(String roleName) {
        return findOne(connection -> {
            String query = "SELECT role_id, role_name FROM role WHERE role_name = ?";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, roleName);

            return statement;
        }, new RoleRowMapper());
    }

    Collection<Role> getRolesByUser(User user) {
        return findMultiple(connection -> {
            String query = "SELECT role.role_id, role.role_name FROM role " +
                    "INNER JOIN user_role ON role.role_id = user_role.role_id WHERE user_role.user_id = ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, user.getUserId());

            return statement;
        }, new RoleDao.RoleRowMapper());
    }


    private final class RoleRowMapper implements RowMapper<Role> {
        @Override
        public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
            Role role = new Role();

            role.setRoleId(rs.getLong("role_id"));
            role.setRoleName(rs.getString("role_name"));

            return role;
        }
    }
}
