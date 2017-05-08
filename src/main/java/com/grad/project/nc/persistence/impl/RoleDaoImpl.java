package com.grad.project.nc.persistence.impl;

import com.grad.project.nc.model.Role;
import com.grad.project.nc.persistence.AbstractDao;
import com.grad.project.nc.persistence.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class RoleDaoImpl extends AbstractDao implements RoleDao {

    private static final String PK_COLUMN_NAME = "role_id";

    @Autowired
    public RoleDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public Role add(Role role) {
        String insertQuery = "INSERT INTO \"role\" (\"role_name\") VALUES (?)";

        Long roleId = executeInsertWithId(insertQuery, PK_COLUMN_NAME, role.getRoleName());

        role.setRoleId(roleId);

        return role;
    }

    @Override
    public Role update(Role role) {
        String updateQuery = "UPDATE \"role\" SET \"role_name\" = ? WHERE \"role_id\" = ?";

        executeUpdate(updateQuery, role.getRoleName(), role.getRoleId());
        return role;
    }

    @Override
    public Role find(Long id) {
        String findOneQuery = "SELECT \"role_id\", \"role_name\" FROM \"role\" WHERE \"role_id\" = ?";

        return findOne(findOneQuery, new RoleRowMapper(), id);
    }

    @Override
    public List<Role> findAll() {
        String findAllQuery = "SELECT \"role_id\", \"role_name\" FROM \"role\"";

        return findMultiple(findAllQuery, new RoleRowMapper());
    }

    @Override
    public void delete(Long id) {
        String deleteQuery = "DELETE FROM \"role\" WHERE \"role_id\" = ?";
        executeUpdate(deleteQuery, id);
    }

    @Override
    public Role findByName(String roleName) {
        String query = "SELECT \"role_id\", \"role_name\" FROM \"role\" WHERE \"role_name\" = ?";

        return findOne(query, new RoleRowMapper(), roleName);
    }

    @Override
    public List<Role> findUserRolesById(Long userId) {
        String query = "SELECT r.\"role_id\", r.\"role_name\" " +
                "FROM \"role\" r " +
                "INNER JOIN \"user_role\" ur " +
                "ON r.\"role_id\" = ur.\"role_id\" " +
                "WHERE ur.\"user_id\" = ?";

        return findMultiple(query, new RoleRowMapper(), userId);
    }

    private class RoleRowMapper implements RowMapper<Role> {

        @Override
        public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
            Role role = new Role();

            role.setRoleId(rs.getLong("role_id"));
            role.setRoleName(rs.getString("role_name"));

            return role;
        }
    }
}
