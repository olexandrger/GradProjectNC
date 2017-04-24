package com.grad.project.nc.model.mapper;

import com.google.common.collect.ImmutableList;
import com.grad.project.nc.model.Role;
import com.grad.project.nc.model.UserOLD;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserOLDRowMapper implements RowMapper<UserOLD> {
    @Override
    public UserOLD mapRow(ResultSet resultSet, int i) throws SQLException {
        UserOLD userOLD = new UserOLD();
        userOLD.setId(resultSet.getLong("id"));
        userOLD.setUsername(resultSet.getString("username"));
        userOLD.setPassword(resultSet.getString("password"));
        userOLD.setAuthorities(ImmutableList.of(Role.USER));
        userOLD.setAccountNonExpired(true);
        userOLD.setAccountNonLocked(true);
        userOLD.setCredentialsNonExpired(true);
        userOLD.setEnabled(true);
        return userOLD;
    }
}
