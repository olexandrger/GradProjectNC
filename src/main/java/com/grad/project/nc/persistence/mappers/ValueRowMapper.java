package com.grad.project.nc.persistence.mappers;

import com.grad.project.nc.model.Value;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Alex on 4/25/2017.
 */
public class ValueRowMapper implements RowMapper<Value> {
    @Override
    public Value mapRow(ResultSet rs, int rowNum) throws SQLException {
        Value value = new Value();

        value.setValueId(rs.getLong("value_id"));
        value.setProductCharacteristicId(rs.getLong("product_characteristic_id"));
        value.setNumberValue(rs.getLong("number_value"));
        value.setDateValue(rs.getTimestamp("date_value").toLocalDateTime());
        value.setStringValue(rs.getString("string_value"));

        return value;
    }
}
