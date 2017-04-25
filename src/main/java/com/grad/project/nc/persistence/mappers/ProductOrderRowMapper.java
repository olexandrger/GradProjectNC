package com.grad.project.nc.persistence.mappers;

import com.grad.project.nc.model.ProductOrder;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductOrderRowMapper implements RowMapper<ProductOrder> {
    @Override
    public ProductOrder mapRow(ResultSet resultSet, int i) throws SQLException {
        ProductOrder productOrder = new ProductOrder();

        productOrder.setProductOrderId(resultSet.getInt("product_order_id"));
        productOrder.setProductInstanceId(resultSet.getInt("product_instance_id"));
        productOrder.setUserId(resultSet.getInt("user_id"));
        productOrder.setCategoryId(resultSet.getInt("category_id"));
        productOrder.setStatusID(resultSet.getInt("status_id"));
        productOrder.setResponsibleId(resultSet.getInt("responsible_id"));
        productOrder.setOpenDate(resultSet.getTimestamp("open_date").toLocalDateTime());
        productOrder.setCloseDate(resultSet.getTimestamp("close_date").toLocalDateTime());

        return productOrder;
    }
}
