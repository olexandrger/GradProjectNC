package com.grad.project.nc.persistence;

import com.grad.project.nc.model.Role;
import com.grad.project.nc.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Roman Savuliak on 26.04.2017.
 */
@Repository
public class RoleDao extends AbstractDao<Role>{

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    RoleDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    @Transactional
    public Role add(Role role) {
        SimpleJdbcInsert insertRoleQuery = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("role")
                .usingGeneratedKeyColumns("role_id");

        Map<String, Object> parameters = new HashMap<String, Object>(1);
        parameters.put("role_name", role.getRoleName());

        Number newId = insertRoleQuery.executeAndReturnKey(parameters);
        role.setRoleId(newId.longValue());

        return role;
    }

    @Override
    @Transactional
    public Role update(Role role) {
        final String UPDATE_QUERY = "UPDATE role SET role_name = ?" + "WHERE role_id = ?";
        jdbcTemplate.update(UPDATE_QUERY, new Object[]{role.getRoleName(), role.getRoleId()});
        return role;
    }

    @Override
    @Transactional
    public Role find(long id) {
        final String SELECT_QUERY = "SELECT role_id, role_name FROM role WHERE role_id = ?";
        Role role = null;
        try{
            role =jdbcTemplate.queryForObject(SELECT_QUERY, new Object[]{id}, new RoleRowMapper());
        } catch (EmptyResultDataAccessException ex){

        }
        return role;
    }

    @Override
    @Transactional
    public Collection<Role> findAll() {
        final String SELECT_QUERY = "SELECT role_id, role_name FROM role";
        return jdbcTemplate.query(SELECT_QUERY, new RoleRowMapper());
    }


    @Override
    @Transactional
    public void delete(Role role) {
        final String DELETE_QUERY = "DELETE FROM role WHERE role_id = ?";
        jdbcTemplate.update(DELETE_QUERY, role.getRoleId());
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

    final class RoleRowMapper implements RowMapper<Role> {
        @Override
        public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
            Role role = new Role();

            role.setRoleId(rs.getLong("role_id"));
            role.setRoleName(rs.getString("role_name"));

            return role;
        }
    }
}
