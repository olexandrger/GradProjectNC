package com.grad.project.nc.persistence.mappers;

import com.grad.project.nc.model.ProductInstance;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductInstanceRowMapper implements RowMapper<ProductInstance>{
    @Override
    public ProductInstance mapRow(ResultSet resultSet, int i) throws SQLException {
        ProductInstance productInstance = new ProductInstance();

        productInstance.setInstanceId(resultSet.getLong("instance_id"));
        productInstance.setProductId(resultSet.getLong("product_id"));
        productInstance.setDomainId(resultSet.getLong("domain_id"));
        productInstance.setStatusId(resultSet.getLong("status_id"));

        return productInstance;
    }
}
