package com.grad.project.nc.persistence.impl;

import com.grad.project.nc.model.ProductInstance;
import com.grad.project.nc.model.proxy.ProductInstanceProxy;
import com.grad.project.nc.persistence.AbstractDao;
import com.grad.project.nc.persistence.ProductInstanceDao;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ProductInstanceDaoImpl extends AbstractDao implements ProductInstanceDao {

    private static final String PK_COLUMN_NAME = "instance_id";

    private final ObjectFactory<ProductInstanceProxy> proxyFactory;

    @Autowired
    public ProductInstanceDaoImpl(JdbcTemplate jdbcTemplate, ObjectFactory<ProductInstanceProxy> proxyFactory) {
        super(jdbcTemplate);
        this.proxyFactory = proxyFactory;
    }

    @Override
    public ProductInstance add(ProductInstance instance) {
        String insertQuery = "INSERT INTO \"product_instance\" (\"price_id\", \"domain_id\", \"status_id\") " +
                "VALUES (?, ?, ?)";

        Long instanceId = executeInsertWithId(insertQuery, PK_COLUMN_NAME, instance.getPrice().getPriceId(),
                instance.getDomain().getDomainId(), instance.getStatus().getCategoryId());

        instance.setInstanceId(instanceId);

        return instance;
    }

    @Override
    public ProductInstance update(ProductInstance instance) {
        String updateQuery = "UPDATE \"product_instance\" SET \"price_id\"=?, \"domain_id\"=?, " +
                "\"status_id\"=? WHERE \"instance_id\"=?";

        executeUpdate(updateQuery, instance.getPrice().getPriceId(), instance.getDomain().getDomainId(),
                instance.getStatus().getCategoryId(), instance.getInstanceId());

        return instance;
    }

    @Override
    public ProductInstance find(Long id) {
        String findOneQuery = "SELECT \"instance_id\", \"price_id\", \"domain_id\", \"status_id\" " +
                "FROM \"product_instance\" WHERE \"instance_id\"=?";

        return findOne(findOneQuery, new ProductInstanceRowMapper(), id);
    }

    @Override
    public List<ProductInstance> findAll() {
        String findAllQuery = "SELECT \"instance_id\", \"price_id\", \"domain_id\", \"status_id\" " +
                "FROM \"product_instance\"";

        return findMultiple(findAllQuery, new ProductInstanceRowMapper());
    }

    @Override
    public List<ProductInstance> findAll(long size, long offset) {
        String findAllQuery = "SELECT \"instance_id\", \"price_id\", \"domain_id\", \"status_id\" " +
                "FROM \"product_instance\" ORDER BY \"instance_id\" DESC ";

        return findMultiplePage(findAllQuery, new ProductInstanceRowMapper(), size, offset);
    }



    @Override
    public List<ProductInstance> findByUserId(Long id, Long size, Long offset) {
        String findAllQuery = "SELECT \"instance_id\", \"price_id\", \"domain_id\", \"status_id\" " +
                "FROM \"product_instance\" WHERE domain_id IN (SELECT domain_id FROM user_domain WHERE user_id = " + id + ")";

        return findMultiplePage(findAllQuery, new ProductInstanceRowMapper(), size, offset);
    }

    @Override
    public List<ProductInstance> findByStatus( Long size, Long offset, Long statusId) {
        String findAllQuery = "SELECT \"instance_id\", \"price_id\", \"domain_id\", \"status_id\" " +
                "FROM \"product_instance\" WHERE \"status_id\" = ? ORDER BY \"instance_id\" DESC";

        return findMultiplePage(findAllQuery, new ProductInstanceRowMapper(), size, offset, statusId);
    }

    @Override
    public void delete(Long id) {
        String deleteQuery = "DELETE FROM \"product_instance\" WHERE \"instance_id\"=?";

        executeUpdate(deleteQuery, id);
    }

    @Override
    public ProductInstance findByProductOrderId(Long productOrderId) {
        String query = "SELECT i.\"instance_id\", i.\"price_id\", i.\"domain_id\", i.\"status_id\" " +
                "FROM \"product_instance\" i " +
                "JOIN \"product_order\" po " +
                "ON po.\"product_instance_id\"=i.\"instance_id\" AND po.\"product_order_id\"=?";

        return findOne(query, new ProductInstanceRowMapper(), productOrderId);
    }

    @Override
    public ProductInstance findByComplainId(Long complainId) {
        String query = "SELECT i.\"instance_id\", i.\"price_id\", i.\"domain_id\", i.\"status_id\" " +
                "FROM \"product_instance\" i " +
                "JOIN \"complain\" c " +
                "ON c.\"product_instance_id\"=i.\"instance_id\" AND c.\"complain_id\"=?";

        return findOne(query, new ProductInstanceRowMapper(), complainId);
    }

    @Override
    public List<ProductInstance> findByDomainId(Long domainId) {
        String query = "SELECT \"instance_id\", \"price_id\", \"domain_id\", \"status_id\" " +
                "FROM \"product_instance\" WHERE \"domain_id\"=?";

        return findMultiple(query, new ProductInstanceRowMapper(), domainId);
    }

    @Override
    public List<ProductInstance> findByProductRegionPriceId(Long productRegionPriceId) {
        String query = "SELECT \"instance_id\", \"price_id\", \"domain_id\", \"status_id\" " +
                "FROM \"product_instance\" WHERE \"price_id\"=?";

        return findMultiple(query, new ProductInstanceRowMapper(), productRegionPriceId);
    }

    private class ProductInstanceRowMapper implements RowMapper<ProductInstance> {
        @Override
        public ProductInstance mapRow(ResultSet resultSet, int i) throws SQLException {
            ProductInstanceProxy productInstance = proxyFactory.getObject();

            productInstance.setInstanceId(resultSet.getLong("instance_id"));
            productInstance.setPriceId(resultSet.getLong("price_id"));
            productInstance.setDomainId(resultSet.getLong("domain_id"));
            productInstance.setStatusId(resultSet.getLong("status_id"));

            return productInstance;
        }
    }
}
