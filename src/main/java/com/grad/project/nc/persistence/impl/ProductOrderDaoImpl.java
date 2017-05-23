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

        Long responsibleId = productOrder.getResponsible() == null ? null : productOrder.getResponsible().getUserId();

        Long productOrderId = executeInsertWithId(insertQuery, PK_COLUMN_NAME,
                productOrder.getProductInstance().getInstanceId(), productOrder.getUser().getUserId(),
                productOrder.getOrderAim().getCategoryId(), productOrder.getStatus().getCategoryId(),
                responsibleId, productOrder.getOpenDate(), productOrder.getCloseDate());

        productOrder.setProductOrderId(productOrderId);

        return find(productOrderId);
    }

    @Override
    public ProductOrder update(ProductOrder productOrder) {
        String updateQuery = "UPDATE \"product_order\" SET \"product_instance_id\"=?, \"user_id\"=?, " +
                "\"order_aim_id\"=?, \"status_id\"=?, \"responsible_id\"=?, \"open_date\"=?, " +
                "\"close_date\"=? WHERE \"product_order_id\"=?";


        Long responsibleId = productOrder.getResponsible() == null ? null : productOrder.getResponsible().getUserId();

        executeUpdate(updateQuery, productOrder.getProductInstance().getInstanceId(),
                productOrder.getUser().getUserId(), productOrder.getOrderAim().getCategoryId(),
                productOrder.getStatus().getCategoryId(), responsibleId,
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
    public List<ProductOrder> findAll(long size, long offset) {
        String findAllQuery = "SELECT \"product_order_id\", \"product_instance_id\", \"user_id\", " +
                "\"order_aim_id\", \"status_id\", \"responsible_id\", \"open_date\", \"close_date\" " +
                "FROM \"product_order\" ORDER BY close_date DESC NULLS FIRST, open_date DESC";

        return findMultiplePage(findAllQuery, new ProductOrderRowMapper(), size, offset);
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
            "WHERE po.\"product_instance_id\" = ?  ORDER BY close_date DESC NULLS FIRST, open_date DESC";

        return findMultiple(query, new ProductOrderRowMapper(), id);
    }

    @Override
    public List<ProductOrder> findByProductInstanceId(Long id, Long size, Long offset) {
        String query = "SELECT po.\"product_order_id\", po.\"product_instance_id\", po.\"user_id\", " +
                "po.\"order_aim_id\", po.\"status_id\", po.\"responsible_id\", po.\"open_date\", po.\"close_date\" " +
                "FROM \"product_order\" po " +
                "WHERE po.\"product_instance_id\" = ? ORDER BY close_date DESC NULLS FIRST, open_date DESC";

        return findMultiplePage(query, new ProductOrderRowMapper(), size, offset, id);
    }

    @Override
    public List<ProductOrder> findByUserId(Long userId, long size, long offset) {
        String query = "SELECT \"product_order_id\", \"product_instance_id\", \"user_id\", \"order_aim_id\", " +
            "\"status_id\", \"responsible_id\", \"open_date\", \"close_date\" " +
            "FROM \"product_order\" WHERE \"user_id\"=? ORDER BY close_date DESC NULLS FIRST, open_date DESC";

        return findMultiplePage(query, new ProductOrderRowMapper(), size, offset, userId);
    }

    @Override
    public List<ProductOrder> findOpenOrdersByInstanseId(Long instanceId, long size, long offset) {
        String query =
                "SELECT " +
                        "po.product_order_id, " +
                        "po.product_instance_id, " +
                        "po.user_id, " +
                        "po.order_aim_id, " +
                        "po.status_id, " +
                        "po.responsible_id, " +
                        "po.open_date, " +
                        "po.close_date " +
                        "FROM product_order po " +
                        "INNER JOIN category c " +
                        "ON c.category_id=po.status_id " +
                        "WHERE c.category_name " +
                        "NOT IN (\'CANCELLED\', \'COMPLETED\') " +
                        "AND po.product_instance_id = ? " +
                        "ORDER BY close_date DESC " +
                        "NULLS FIRST, " +
                        "open_date DESC";
        return findMultiplePage(query, new ProductOrderRowMapper(), size, offset, instanceId);
    }

    @Override
    public List<ProductOrder> findByAim(String aim, Long size, Long offset) {
        String query =
                "SELECT " +
                        "po.product_order_id, " +
                        "po.product_instance_id, " +
                        "po.user_id, " +
                        "po.order_aim_id, " +
                        "po.status_id, " +
                        "po.responsible_id, " +
                        "po.open_date, " +
                        "po.close_date " +
                        "FROM product_order po " +
                        "INNER JOIN category c " +
                        "ON c.category_id=po.order_aim_id " +
                        "WHERE c.category_name=? " +
                        "ORDER BY close_date DESC " +
                        "NULLS FIRST, " +
                        "open_date DESC";
        return findMultiplePage(query, new ProductOrderRowMapper(), size, offset, aim);
    }

    @Override
    public List<ProductOrder> findByStatus(String status, Long size, Long offset) {
        String query =
                "SELECT " +
                        "po.product_order_id, " +
                        "po.product_instance_id, " +
                        "po.user_id, " +
                        "po.order_aim_id, " +
                        "po.status_id, " +
                        "po.responsible_id, " +
                        "po.open_date, " +
                        "po.close_date " +
                        "FROM product_order po " +
                        "INNER JOIN category c " +
                        "ON c.category_id=po.status_id " +
                        "WHERE c.category_name=? " +
                        "ORDER BY close_date DESC " +
                        "NULLS FIRST, " +
                        "open_date DESC";
        return findMultiplePage(query, new ProductOrderRowMapper(), size, offset, status);
    }

    @Override
    public List<ProductOrder> findByAimAndStatus(String aim, String status, Long size, Long offset) {
        String query =
                "SELECT " +
                        "po.product_order_id, " +
                        "po.product_instance_id, " +
                        "po.user_id, " +
                        "po.order_aim_id, " +
                        "po.status_id, " +
                        "po.responsible_id, " +
                        "po.open_date, " +
                        "po.close_date " +
                        "FROM product_order po " +
                        "INNER JOIN category c ON c.category_id=po.status_id " +
                        "INNER JOIN category c2 on po.order_aim_id = c2.category_id " +
                        "WHERE c.category_name=? and c2.category_name=? " +
                        "ORDER BY close_date DESC " +
                        "NULLS FIRST, " +
                        "open_date DESC";
        return findMultiplePage(query, new ProductOrderRowMapper(), size, offset, status, aim);
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
