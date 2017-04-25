package com.grad.project.nc.persistence.mappers;

import com.grad.project.nc.model.ProductType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * Created by Alex on 4/25/2017.
 */
public class ProductTypeRowMapper implements RowMapper<ProductType> {
    @Override
    public ProductType mapRow(ResultSet rs, int rowNum) throws SQLException {
        ProductType productType = new ProductType();

        productType.setProductTypeId(rs.getLong("product_type_id"));
        productType.setProductTypeName(rs.getString("product_type_name"));
        productType.setProductTypeDescription(rs.getString("product_type_description"));

        return productType;
    }
}
