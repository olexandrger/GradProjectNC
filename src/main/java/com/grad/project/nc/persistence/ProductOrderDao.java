package com.grad.project.nc.persistence;

import com.grad.project.nc.model.Category;
import com.grad.project.nc.model.ProductInstance;
import com.grad.project.nc.model.ProductOrder;
import com.grad.project.nc.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.Collection;

@Repository
public class ProductOrderDao extends AbstractDao<ProductOrder> {

    ProductOrderRowMapper mapper = new ProductOrderRowMapper();

    @Autowired
    ProductOrderDao(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public ProductOrder add(ProductOrder entity) {
        KeyHolder keyHolder = executeInsert(connection -> {
            String statement =
                    "INSERT INTO product_order (" +
                            "product_instance_id, " +
                            "user_id, " +
                            "order_aim_id, " +
                            "status_id, " +
                            "responsible_id, " +
                            "open_date, " +
                            "close_date) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setLong(1, entity.getProductInstance().getInstanceId());
            preparedStatement.setLong(2, entity.getUser().getUserId());
            preparedStatement.setLong(3, entity.getOrderAim().getCategoryId());
            preparedStatement.setLong(4, entity.getStatus().getCategoryId());
            preparedStatement.setLong(5, entity.getResponsible().getUserId());
            preparedStatement.setTimestamp(6, Timestamp.valueOf(entity.getOpenDate()));
            preparedStatement.setTimestamp(7, Timestamp.valueOf(entity.getOpenDate()));

            return preparedStatement;
        });

        // entity.setOrderAimId(((Number)keyHolder.getKeys().get("product_order_id")).longValue());

        return find(getLongValue(keyHolder, "product_order_id"));
    }

    @Override
    public ProductOrder update(ProductOrder entity) {
        executeUpdate(connection -> {
            String statement =
                    "UPDATE product_order " +
                            "SET " +
                            "product_instance_id=?, " +
                            "user_id=?, " +
                            "order_aim_id=?, " +
                            "status_id=?, " +
                            "responsible_id=?, " +
                            "open_date=?, " +
                            "close_date=? " +
                            "WHERE product_order_id=?";

            PreparedStatement preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setLong(1, entity.getProductInstance().getInstanceId());
            preparedStatement.setLong(2, entity.getUser().getUserId());
            preparedStatement.setLong(3, entity.getOrderAim().getCategoryId());
            preparedStatement.setLong(4, entity.getStatus().getCategoryId());
            preparedStatement.setLong(5, entity.getResponsible().getUserId());
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
            String statement =
                    "SELECT " +
                            "product_order_id, " +
                            "open_date, " +
                            "close_date " +
                            "FROM product_order " +
                            "WHERE product_order_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setLong(1, id);

            return preparedStatement;
        }, new ProductOrderRowMapper());
    }

    @Override
    public Collection<ProductOrder> findAll() {
        return findMultiple(connection -> {
            String statement =
                    "SELECT " +
                            "product_order_id, " +
                            "open_date, " +
                            "close_date " +
                            "FROM product_order";
            return connection.prepareStatement(statement);
        }, new ProductOrderRowMapper());
    }

    @Override
    public void delete(ProductOrder entity) {
        executeUpdate(connection -> {
            String statement =
                    "DELETE" +
                            " FROM product_order " +
                            "WHERE product_order_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(statement);

            preparedStatement.setLong(1, entity.getProductOrderId());

            return preparedStatement;
        });
    }

    Collection<ProductOrder> getOrdersByUser(User userProxy) {
        return findMultiple(connection -> {
            String statement =
                    "SELECT " +
                            "product_order_id, " +
                            "open_date, " +
                            "close_date " +
                            "FROM product_order " +
                            "INNER JOIN \"user\" " +
                            "ON \"user\".user_id = product_order.user_id";
            return connection.prepareStatement(statement);
        }, new ProductOrderRowMapper());
    }

    private class ProductOrderProxy extends ProductOrder {

        @Autowired
        private ProductInstanceDao productInstanceDao;
        @Autowired
        private UserDao userDao;
        @Autowired
        private CategoryDao categoryDao;


        @Override
        public ProductInstance getProductInstance() {
            if(super.getProductInstance() == null){
                super.setProductInstance(productInstanceDao.findByProductOrder(this));
            }
            return super.getProductInstance();
        }

        @Override
        public User getUser() {
            if(super.getUser() == null){
                super.setUser(userDao.findUserByProductOrder(this));
            }
            return super.getUser();
        }

        @Override
        public Category getOrderAim() {
            if(super.getOrderAim() == null){
                super.setOrderAim(categoryDao.findAimByProductOrder(this));
            }
            return super.getOrderAim();
        }

        @Override
        public Category getStatus() {
            if(super.getStatus() == null){
                super.setStatus(categoryDao.findOrderStatusByProductOrder(this));
            }
            return super.getStatus();
        }

        @Override
        public User getResponsible() {
            if(super.getResponsible() == null){
                super.setResponsible(userDao.findResponsibleByProductOrder(this));
            }
            return super.getResponsible();
        }
    }

    private class ProductOrderRowMapper implements RowMapper<ProductOrder> {
        @Override
        public ProductOrder mapRow(ResultSet resultSet, int i) throws SQLException {
            ProductOrder productOrder = new ProductOrderProxy();

            productOrder.setProductOrderId(resultSet.getLong("product_order_id"));
//            productOrder.setProductInstanceId(resultSet.getLong("product_instance_id"));
//            productOrder.setUserId(resultSet.getLong("user_id"));
//            productOrder.setOrderAimId(resultSet.getLong("order_aim_id"));
//            productOrder.setStatusId(resultSet.getLong("status_id"));
//            productOrder.setResponsibleId(resultSet.getLong("responsible_id"));
            productOrder.setOpenDate(resultSet.getTimestamp("open_date").toLocalDateTime());

            if (resultSet.getTimestamp("close_date") != null)
                productOrder.setCloseDate(resultSet.getTimestamp("close_date").toLocalDateTime());

            return productOrder;
        }
    }

}
