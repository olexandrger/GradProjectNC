package com.grad.project.nc.persistence.mappers;

import com.grad.project.nc.model.ProductOrder;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductOrderRowMapper implements RowMapper<ProductOrder> {
    @Override
    public ProductOrder mapRow(ResultSet resultSet, int i) throws SQLException {
        ProductOrder productOrder = new ProductOrder();

        productOrder.setProductOrderId(resultSet.getLong("product_order_id"));
        productOrder.setProductInstanceId(resultSet.getLong("product_instance_id"));
        productOrder.setUserId(resultSet.getLong("user_id"));
        productOrder.setCategoryId(resultSet.getLong("category_id"));
        productOrder.setStatusId(resultSet.getLong("status_id"));
        productOrder.setResponsibleId(resultSet.getLong("responsible_id"));
        productOrder.setOpenDate(resultSet.getTimestamp("open_date").toLocalDateTime());

        if (resultSet.getTimestamp("close_date") != null)
            productOrder.setCloseDate(resultSet.getTimestamp("close_date").toLocalDateTime());

        return productOrder;
    }
}
