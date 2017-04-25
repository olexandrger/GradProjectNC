package com.grad.project.nc.persistence.mappers;

import com.grad.project.nc.model.StatusType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StatusTypeRowMapper implements RowMapper<StatusType> {
    @Override
    public StatusType mapRow(ResultSet resultSet, int i) throws SQLException {
        StatusType statusType = new StatusType();

        statusType.setStatusTypeId(resultSet.getInt("status_type_id"));
        statusType.setStatusTypeName(resultSet.getString("status_type_name"));

        return statusType;
    }
}
