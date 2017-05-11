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
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductCharacteristicValueDaoImpl extends AbstractDao
        implements ProductCharacteristicValueDao {

    private static final String PK_COLUMN_NAME = "value_id";

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

        Long valueId = executeInsertWithId(insertQuery, PK_COLUMN_NAME, value.getProduct().getProductId(),
                value.getProductCharacteristic().getProductCharacteristicId(),
                value.getNumberValue(), value.getDateValue(), value.getStringValue());

        value.setValueId(valueId);

        return value;
    }

    @Override
    public ProductCharacteristicValue update(ProductCharacteristicValue value) {
        String updateQuery = "UPDATE \"product_characteristic_value\" SET \"product_id\"=?, " +
                "\"product_characteristic_id\"=?, \"number_value\"=?, " +
                "\"date_value\"=?, \"string_value\"=? WHERE \"value_id=?\"";

        executeUpdate(updateQuery, value.getProduct().getProductId(),
                value.getProductCharacteristic().getProductCharacteristicId(),
                value.getNumberValue(), value.getDateValue(),
                value.getStringValue(), value.getValueId()
        );

        return value;
    }

    @Override
    public ProductCharacteristicValue find(Long id) {
        String query = "SELECT \"value_id\", \"product_id\", \"product_characteristic_id\", \"number_value\", " +
                "\"date_value\", \"string_value\" FROM \"product_characteristic_value\" WHERE \"value_id\"=?";

        return findOne(query, new ProductCharacteristicValueRowMapper(), id);
    }

    @Override
    public ProductCharacteristicValue find(Long productId, Long productCharacteristicId) {
        String query = "SELECT \"value_id\", \"product_id\", \"product_characteristic_id\", \"number_value\", " +
                "\"date_value\", \"string_value\" FROM \"product_characteristic_value\" " +
                "WHERE (\"product_id\"=? AND \"product_characteristic_id\"=?)";

        return findOne(query, new ProductCharacteristicValueRowMapper(),
                productId, productCharacteristicId);
    }

    @Override
    public List<ProductCharacteristicValue> findAll() {
        String findAllQuery = "SELECT \"value_id\", \"product_id\", \"product_characteristic_id\", " +
                "\"number_value\", \"date_value\", \"string_value\" " +
                "FROM \"product_characteristic_value\"";
        return findMultiple(findAllQuery, new ProductCharacteristicValueRowMapper());
    }

    @Override
    public void delete(Long id) {
        String deleteQuery = "DELETE FROM \"product_characteristic_value\" " +
                "WHERE \"value_id\"=?";

        executeUpdate(deleteQuery, id);
    }

    @Override
    public void delete(Long productId, Long productCharacteristicId) {
        String deleteQuery = "DELETE FROM \"product_characteristic_value\" " +
                "WHERE (\"product_id\"=? AND \"product_characteristic_id\"=?)";

        executeUpdate(deleteQuery, productId, productCharacteristicId);
    }

    @Override
    @Transactional
    public void deleteBatch(List<ProductCharacteristicValue> values) {
        String deleteQuery = "DELETE FROM \"product_characteristic_value\" " +
                "WHERE \"value_id\"=?";

        List<Object[]> batchArgs = new ArrayList<>();
        for (ProductCharacteristicValue value : values) {
            batchArgs.add(new Object[]{value.getValueId()});
        }
        batchUpdate(deleteQuery, batchArgs);
    }

    @Override
    @Transactional
    public void updateBatch(List<ProductCharacteristicValue> values) {
        String updateQuery = "UPDATE \"product_characteristic_value\" SET \"product_id\"=?, " +
                "\"product_characteristic_id\"=?, \"number_value\"=?, " +
                "\"date_value\"=?, \"string_value\"=? WHERE \"value_id\"=?";

        List<Object[]> batchArgs = new ArrayList<>();
        for (ProductCharacteristicValue value : values) {
            batchArgs.add(new Object[]{value.getProduct().getProductId(),
                    value.getProductCharacteristic().getProductCharacteristicId(),
                    value.getNumberValue(), value.getDateValue(), value.getStringValue(), value.getValueId()});
        }
        batchUpdate(updateQuery, batchArgs);
    }

    @Override
    public List<ProductCharacteristicValue> findByProductId(Long productId) {
        final String query = "SELECT \"value_id\", \"product_id\", \"product_characteristic_id\", " +
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
    @Transactional
    public void persistBatch(List<ProductCharacteristicValue> values) {
        String insertQuery = "INSERT INTO \"product_characteristic_value\" (\"product_id\", " +
                "\"product_characteristic_id\", \"number_value\", \"date_value\", \"string_value\") " +
                "VALUES (?, ?, ?, ?, ?)";

        List<Object[]> batchArgs = new ArrayList<>();
        for (ProductCharacteristicValue value : values) {
            batchArgs.add(new Object[]{value.getProduct().getProductId(),
                    value.getProductCharacteristic().getProductCharacteristicId(),
                    value.getNumberValue(), value.getDateValue(), value.getStringValue()});
        }
        batchUpdate(insertQuery, batchArgs);
    }

    private class ProductCharacteristicValueRowMapper implements RowMapper<ProductCharacteristicValue> {

        @Override
        public ProductCharacteristicValue mapRow(ResultSet rs, int rowNum) throws SQLException {
            ProductCharacteristicValueProxy value = proxyFactory.getObject();

            value.setValueId(rs.getLong("value_id"));
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
