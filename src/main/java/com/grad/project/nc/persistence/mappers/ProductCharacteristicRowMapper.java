package com.grad.project.nc.persistence.mappers;

import com.grad.project.nc.model.ProductCharacteristic;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Alex on 4/25/2017.
 */
public class ProductCharacteristicRowMapper implements RowMapper<ProductCharacteristic> {
    @Override
    public ProductCharacteristic mapRow(ResultSet rs, int rowNum) throws SQLException {
        ProductCharacteristic productCharacteristic = new ProductCharacteristic();

        productCharacteristic.setProductCharacteristicId(rs.getLong("product_characteristic_id"));
        productCharacteristic.setProductTypeId(rs.getLong("product_type_id"));
        productCharacteristic.setCharacteristicName(rs.getString("characteristic_name"));
        productCharacteristic.setMeasure(rs.getString("measure"));
        productCharacteristic.setDataTypeId(rs.getLong("data_type_id"));


        return productCharacteristic;
    }
}
