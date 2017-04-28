package com.grad.project.nc.persistence;

import com.grad.project.nc.model.ProductOrder;
import com.grad.project.nc.model.User;
import com.grad.project.nc.persistence.mappers.ProductOrderRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Collection;

@Repository
public class ProductOrderDao extends AbstractDao<ProductOrder> {

    @Autowired
    ProductOrderDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public ProductOrder add(ProductOrder entity) {
        KeyHolder keyHolder = executeInsert(connection -> {
            String statement = "INSERT INTO product_order (product_instance_id, user_id, category_id, " +
                    "status_id, responsible_id, open_date, close_date) VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setLong(1, entity.getProductInstanceId());
            preparedStatement.setLong(2, entity.getUserId());
            preparedStatement.setLong(3, entity.getCategoryId());
            preparedStatement.setLong(4, entity.getStatusId());
            preparedStatement.setLong(5, entity.getResponsibleId());
            preparedStatement.setTimestamp(6, Timestamp.valueOf(entity.getOpenDate()));
            preparedStatement.setTimestamp(7, Timestamp.valueOf(entity.getOpenDate()));

            return preparedStatement;
        });

        entity.setCategoryId(((Number)keyHolder.getKeys().get("product_order_id")).longValue());

        return entity;
    }

    @Override
    public ProductOrder update(ProductOrder entity) {
        executeUpdate(connection -> {
            String statement = "UPDATE product_order SET product_instance_id=?, user_id=?, category_id=?, " +
                    "status_id=?, responsible_id=?, open_date=?, close_date=? WHERE product_order_id=?";

            PreparedStatement preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setLong(1, entity.getProductInstanceId());
            preparedStatement.setLong(2, entity.getUserId());
            preparedStatement.setLong(3, entity.getCategoryId());
            preparedStatement.setLong(4, entity.getStatusId());
            preparedStatement.setLong(5, entity.getResponsibleId());
            preparedStatement.setTimestamp(6, Timestamp.valueOf(entity.getOpenDate()));
            preparedStatement.setTimestamp(7, Timestamp.valueOf(entity.getOpenDate()));
            preparedStatement.setLong(8, entity.getProductOrderId());

            return preparedStatement;
        });

        return entity;
    }

    @Override
    public ProductOrder find(long id) {
        return findOne(connection -> {
            String statement = "SELECT product_order_id, product_instance_id, user_id, category_id, " +
                    "status_id, responsible_id, open_date, close_date FROM product_order WHERE product_order_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setLong(1, id);

            return preparedStatement;
        }, new ProductOrderRowMapper());
    }

    @Override
    public Collection<ProductOrder> findAll() {
        return findMultiple(connection -> {
            String statement = "SELECT product_order_id, product_instance_id, user_id, category_id, " +
                "status_id, responsible_id, open_date, close_date FROM product_order";

            return connection.prepareStatement(statement);
        }, new ProductOrderRowMapper());
    }

    @Override
    public void delete(ProductOrder entity) {
        executeUpdate(connection -> {
            String statement = "DELETE FROM product_order WHERE product_order_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setLong(1, entity.getProductOrderId());

            return preparedStatement;
        });
    }

    Collection<ProductOrder> getOrdersByUser(User userProxy) {return findMultiple(connection -> {
        String statement = "SELECT product_order_id, product_instance_id, product_order.user_id, category_id, " +
                "status_id, responsible_id, open_date, close_date FROM product_order " +
                "INNER JOIN \"user\" ON \"user\".user_id = product_order.user_id";

            return connection.prepareStatement(statement);
        }, new ProductOrderRowMapper());
    }
}
