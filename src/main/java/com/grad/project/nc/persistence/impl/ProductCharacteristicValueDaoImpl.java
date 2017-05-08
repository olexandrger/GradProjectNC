package com.grad.project.nc.persistence.impl;

import com.grad.project.nc.model.ProductCharacteristicValue;
import com.grad.project.nc.model.proxy.ProductCharacteristicValueProxy;
import com.grad.project.nc.persistence.AbstractDao;
import com.grad.project.nc.persistence.ProductCharacteristicValueDao;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductCharacteristicValueDaoImpl extends AbstractDao
        implements ProductCharacteristicValueDao {

    private final ObjectFactory<ProductCharacteristicValueProxy> proxyFactory;

    @Autowired
    public ProductCharacteristicValueDaoImpl(JdbcTemplate jdbcTemplate,
                                             ObjectFactory<ProductCharacteristicValueProxy> proxyFactory) {
        super(jdbcTemplate);
        this.proxyFactory = proxyFactory;
    }

    @Override
    public ProductCharacteristicValue add(ProductCharacteristicValue value) {
        String insertQuery = "INSERT INTO \"product_characteristic_value\" (\"product_id\", " +
                "\"product_characteristic_id\", \"number_value\", \"date_value\", \"string_value\") " +
                "VALUES (?,?,?,?,?)";

        executeUpdate(insertQuery, value.getProduct().getProductId(),
                value.getProductCharacteristic().getProductCharacteristicId(),
                value.getNumberValue(), value.getDateValue(), value.getStringValue());

        return value;
    }

    @Override
    public ProductCharacteristicValue update(ProductCharacteristicValue value) {
        String updateQuery = "UPDATE \"product_characteristic_value\" SET \"number_value\"=?, " +
                "\"date_value\"=?, \"string_value\"=? WHERE \"product_id\"=? AND \"product_characteristic_id\"=?";

        executeUpdate(updateQuery, value.getNumberValue(), value.getDateValue(),
                value.getStringValue(), value.getProduct().getProductId(),
                value.getProductCharacteristic().getProductCharacteristicId());

        return value;
    }

    @Override
    public ProductCharacteristicValue find(Long productId, Long productCharacteristicId) {
        String query = "SELECT \"product_id\", \"product_characteristic_id\", \"number_value\", " +
                "\"date_value\", \"string_value\" FROM \"product_characteristic_value\" " +
                "WHERE (\"product_id\"=? AND \"product_characteristic_id\"=?)";

        return findOne(query, new ProductCharacteristicValueRowMapper(),
                productId, productCharacteristicId);
    }

    @Override
    public List<ProductCharacteristicValue> findAll() {
        String findAllQuery = "SELECT \"product_id\" \"product_characteristic_id\", " +
                "\"number_value\", \"date_value\", \"string_value\" " +
                "FROM \"product_characteristic_value\"";
        return findMultiple(findAllQuery, new ProductCharacteristicValueRowMapper());
    }

    @Override
    public void delete(Long productId, Long productCharacteristicId) {
        String deleteQuery = "DELETE FROM \"product_characteristic_value\" " +
                "WHERE (\"product_id\"=? AND \"product_characteristic_id\"=?)";

        executeUpdate(deleteQuery, productId, productCharacteristicId);
    }

    @Override
    public List<ProductCharacteristicValue> findByProductId(Long productId) {
        final String query = "SELECT \"product_id\", \"product_characteristic_id\", " +
                "\"number_value\", \"date_value\", \"string_value\" " +
                "FROM \"product_characteristic_value\" WHERE \"product_id\"=?";
        return findMultiple(query, new ProductCharacteristicValueRowMapper(), productId);
    }

    @Override
    public void deleteByProductId(Long productId) {
        String deleteQuery = "DELETE FROM \"product_characteristic_value\" WHERE \"product_id\"=?";
        executeUpdate(deleteQuery, productId);
    }

    @Override
    public void persistBatch(Long productId, List<ProductCharacteristicValue> values) {
        String insertQuery = "INSERT INTO \"product_characteristic_value\" (\"product_id\", " +
                "\"product_characteristic_id\", \"number_value\", \"date_value\", \"string_value\") " +
                "VALUES (?, ?, ?, ?, ?)";

        List<Object[]> batchArgs = new ArrayList<>();
        for (ProductCharacteristicValue value : values) {
            batchArgs.add(new Object[]{productId,
                    value.getProductCharacteristic().getProductCharacteristicId(),
                    value.getNumberValue(), value.getDateValue(), value.getStringValue()});
        }
        batchUpdate(insertQuery, batchArgs);
    }

    private class ProductCharacteristicValueRowMapper implements RowMapper<ProductCharacteristicValue> {

        @Override
        public ProductCharacteristicValue mapRow(ResultSet rs, int rowNum) throws SQLException {
            ProductCharacteristicValueProxy value = proxyFactory.getObject();

            value.setProductId(rs.getLong("product_id"));
            value.setProductCharacteristicId(rs.getLong("product_characteristic_id"));
            value.setNumberValue(rs.getDouble("number_value"));
            if (rs.getTimestamp("date_value") != null) {
                value.setDateValue(OffsetDateTime
                        .ofInstant(rs.getTimestamp("date_value").toInstant(), ZoneId.systemDefault()));
            }
            value.setStringValue(rs.getString("string_value"));
            return value;
        }
    }
}
