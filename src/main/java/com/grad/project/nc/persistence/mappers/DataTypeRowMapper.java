package com.grad.project.nc.persistence.mappers;

import com.grad.project.nc.model.DataType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Alex on 4/25/2017.
 */
public class DataTypeRowMapper implements RowMapper<DataType> {
    @Override
    public DataType mapRow(ResultSet rs, int rowNum) throws SQLException {
        DataType dataType = new DataType();

        dataType.setDataTypeId(rs.getInt("data_type_id"));
        dataType.setDataType(rs.getString("data_type"));

        return dataType;
    }
}
