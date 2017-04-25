package com.grad.project.nc.persistence.mappers;

import com.grad.project.nc.model.ProductInstance;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductInstanceRowMapper implements RowMapper<ProductInstance>{
    @Override
    public ProductInstance mapRow(ResultSet resultSet, int i) throws SQLException {
        ProductInstance productInstance = new ProductInstance();

        productInstance.setInstanceId(resultSet.getInt("instance_id"));
        productInstance.setProductId(resultSet.getInt("product_id"));
        productInstance.setDomainId(resultSet.getInt("domain_id"));
        productInstance.setStatusId(resultSet.getInt("status_id"));

        return productInstance;
    }
}
