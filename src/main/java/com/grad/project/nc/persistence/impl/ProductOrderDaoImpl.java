package com.grad.project.nc.persistence.impl;

import com.grad.project.nc.model.ProductOrder;
import com.grad.project.nc.model.proxy.ProductOrderProxy;
import com.grad.project.nc.persistence.AbstractDao;
import com.grad.project.nc.persistence.ProductOrderDao;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;

@Repository
public class ProductOrderDaoImpl extends AbstractDao
        implements ProductOrderDao {

    private static final String PK_COLUMN_NAME = "product_order_id";

    private final ObjectFactory<ProductOrderProxy> proxyFactory;

    @Autowired
    public ProductOrderDaoImpl(JdbcTemplate jdbcTemplate, ObjectFactory<ProductOrderProxy> proxyFactory) {
        super(jdbcTemplate);
        this.proxyFactory = proxyFactory;
    }

    @Override
    public ProductOrder add(ProductOrder productOrder) {
        String insertQuery = "INSERT INTO \"product_order\" (\"product_instance_id\", \"user_id\", " +
                "\"order_aim_id\", \"status_id\", \"responsible_id\", \"open_date\", \"close_date\") " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        Long productOrderId = executeInsertWithId(insertQuery, PK_COLUMN_NAME,
                productOrder.getProductInstance().getInstanceId(), productOrder.getUser().getUserId(),
                productOrder.getOrderAim().getCategoryId(), productOrder.getStatus().getCategoryId(),
                productOrder.getResponsible().getUserId(), productOrder.getOpenDate(), productOrder.getCloseDate());

        productOrder.setProductOrderId(productOrderId);

        return productOrder;
    }

    @Override
    public ProductOrder update(ProductOrder productOrder) {
        String updateQuery = "UPDATE \"product_order\" SET \"product_instance_id\"=?, \"user_id\"=?, " +
                "\"order_aim_id\"=?, \"status_id\"=?, \"responsible_id\"=?, \"open_date\"=?, " +
                "\"close_date\"=? WHERE \"product_order_id\"=?";

        executeUpdate(updateQuery, productOrder.getProductInstance().getInstanceId(),
                productOrder.getUser().getUserId(), productOrder.getOrderAim().getCategoryId(),
                productOrder.getStatus().getCategoryId(), productOrder.getResponsible().getUserId(),
                productOrder.getOpenDate(), productOrder.getCloseDate(), productOrder.getProductOrderId());

        return productOrder;
    }

    @Override
    public ProductOrder find(Long id) {
        String findOneQuery = "SELECT \"product_order_id\", \"product_instance_id\", \"user_id\", \"order_aim_id\", " +
                "\"status_id\", \"responsible_id\", \"open_date\", \"close_date\" " +
                "FROM \"product_order\" WHERE \"product_order_id\"=?";

        return findOne(findOneQuery, new ProductOrderRowMapper(), id);
    }

    @Override
    public List<ProductOrder> findAll() {
        String findAllQuery = "SELECT \"product_order_id\", \"product_instance_id\", \"user_id\", " +
                "\"order_aim_id\", \"status_id\", \"responsible_id\", \"open_date\", \"close_date\" " +
                "FROM \"product_order\"";

        return findMultiple(findAllQuery, new ProductOrderRowMapper());
    }

    @Override
    public void delete(Long id) {
        String deleteQuery = "DELETE FROM \"product_order\" " +
                "WHERE \"product_order_id\"=?";

        executeUpdate(deleteQuery, id);
    }

    @Override
    public List<ProductOrder> findByUserId(Long userId) {
        String query = "SELECT \"product_order_id\", \"product_instance_id\", \"user_id\", \"order_aim_id\", " +
                "\"status_id\", \"responsible_id\", \"open_date\", \"close_date\" " +
                "FROM \"product_order\" WHERE \"user_id\"=?";

        return findMultiple(query, new ProductOrderRowMapper(), userId);
    }

    @Override
    public List<ProductOrder> findByProductName(String productName) {
        String query = "SELECT po.\"product_order_id\", po.\"product_instance_id\", po.\"user_id\", " +
                "po.\"order_aim_id\", po.\"status_id\", po.\"responsible_id\", po.\"open_date\", po.\"close_date\" " +
                "FROM \"product_order\" po " +
                "INNER JOIN \"product_instance\" pi " +
                "ON po.\"product_instance_id\" = pi.\"instance_id\" " +
                "INNER JOIN \"product_region_price\" prp " +
                "ON pi.\"price_id\" = prp.\"price_id\" " +
                "INNER JOIN \"product\" p " +
                "ON prp.\"product_id\" = p.\"product_id\" " +
                "WHERE p.\"product_name\" = ?";

        return findMultiple(query, new ProductOrderRowMapper(), productName);
    }

    @Override
    public List<ProductOrder> findByUserLastName(String lastName) {
        String query = "SELECT po.\"product_order_id\", po.\"product_instance_id\", po.\"user_id\", " +
                "po.\"order_aim_id\", po.\"status_id\", po.\"responsible_id\", po.\"open_date\", po.\"close_date\" " +
                "FROM \"product_order\" po " +
                "INNER JOIN \"user\" u " +
                "ON po.\"user_id\" = u.\"user_id\" " +
                "WHERE u.\"last_name\" = ?";

        return findMultiple(query, new ProductOrderRowMapper(), lastName);
    }

    @Override
    public List<ProductOrder> findByUserPhoneNumber(String phoneNumber) {
        String query = "SELECT po.\"product_order_id\", po.\"product_instance_id\", po.\"user_id\", " +
                "po.\"order_aim_id\", po.\"status_id\", po.\"responsible_id\", po.\"open_date\", po.\"close_date\" " +
                "FROM \"product_order\" po " +
                "INNER JOIN \"user\" u " +
                "ON po.\"user_id\" = u.\"user_id\" " +
                "WHERE u.\"phone_number\" = ?";

        return findMultiple(query, new ProductOrderRowMapper(), phoneNumber);
    }

    @Override
    public List<ProductOrder> findByProductInstanceId(Long id) {
        String query = "SELECT po.\"product_order_id\", po.\"product_instance_id\", po.\"user_id\", " +
            "po.\"order_aim_id\", po.\"status_id\", po.\"responsible_id\", po.\"open_date\", po.\"close_date\" " +
            "FROM \"product_order\" po " +
            "WHERE po.\"product_instance_id\" = ?";

        return findMultiple(query, new ProductOrderRowMapper(), id);
    }

    private class ProductOrderRowMapper implements RowMapper<ProductOrder> {

        @Override
        public ProductOrder mapRow(ResultSet resultSet, int i) throws SQLException {
            ProductOrderProxy productOrder = proxyFactory.getObject();

            productOrder.setProductOrderId(resultSet.getLong("product_order_id"));
            productOrder.setProductInstanceId(resultSet.getLong("product_instance_id"));
            productOrder.setUserId(resultSet.getLong("user_id"));
            productOrder.setOrderAimId(resultSet.getLong("order_aim_id"));
            productOrder.setStatusId(resultSet.getLong("status_id"));
            productOrder.setResponsibleId(resultSet.getLong("responsible_id"));
            productOrder.setOpenDate(OffsetDateTime
                    .ofInstant(resultSet.getTimestamp("open_date").toInstant(), ZoneId.systemDefault()));

            if (resultSet.getTimestamp("close_date") != null) {
                productOrder.setCloseDate(OffsetDateTime
                        .ofInstant(resultSet.getTimestamp("close_date").toInstant(), ZoneId.systemDefault()));
            }

            return productOrder;
        }
    }
}
