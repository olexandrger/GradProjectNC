package com.grad.project.nc.persistence.mappers;

import com.grad.project.nc.model.ProductCharacteristicValue;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Alex on 4/25/2017.
 */
public class ProductCharacteristicValueRowMapper implements RowMapper<ProductCharacteristicValue> {
    @Override
    public ProductCharacteristicValue mapRow(ResultSet rs, int rowNum) throws SQLException {
        return null;
    }
}
