package com.grad.project.nc.persistence.mappers;

import com.grad.project.nc.model.Status;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StatusRowMapper implements RowMapper<Status> {
    @Override
    public Status mapRow(ResultSet resultSet, int i) throws SQLException {
        Status status = new Status();

        status.setStatusId(resultSet.getInt("status_id"));
        status.setStatusTypeId(resultSet.getInt("status_type_id"));
        status.setStatusName(resultSet.getString("status_name"));

        return status;
    }
}
