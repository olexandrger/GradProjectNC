package com.grad.project.nc.persistence.impl;

import com.grad.project.nc.model.ProductCharacteristic;
import com.grad.project.nc.model.proxy.ProductCharacteristicProxy;
import com.grad.project.nc.persistence.AbstractDao;
import com.grad.project.nc.persistence.ProductCharacteristicDao;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@Repository
public class ProductCharacteristicDaoImpl extends AbstractDao implements ProductCharacteristicDao {

    private static final String PK_COLUMN_NAME = "product_characteristic_id";

    private final ObjectFactory<ProductCharacteristicProxy> proxyFactory;

    @Autowired
    public ProductCharacteristicDaoImpl(JdbcTemplate jdbcTemplate,
                                        ObjectFactory<ProductCharacteristicProxy> proxyFactory) {
        super(jdbcTemplate);
        this.proxyFactory = proxyFactory;
    }

    @Override
    public ProductCharacteristic add(ProductCharacteristic productCharacteristic) {
        String insertQuery = "INSERT INTO \"product_characteristic\" (\"product_type_id\", \"characteristic_name\", " +
                "\"measure\", \"data_type_id\") VALUES(?, ?, ?, ?)";

        Long productCharacteristicId = executeInsertWithId(insertQuery, PK_COLUMN_NAME,
                productCharacteristic.getProductType().getProductTypeId(),
                productCharacteristic.getCharacteristicName(), productCharacteristic.getMeasure(),
                productCharacteristic.getDataType().getCategoryId());

        productCharacteristic.setProductCharacteristicId(productCharacteristicId);

        return productCharacteristic;
    }

    @Override
    public ProductCharacteristic find(Long id) {
        String findOneQuery = "SELECT \"product_characteristic_id\", \"product_type_id\", \"characteristic_name\", " +
                "\"measure\", \"data_type_id\" FROM \"product_characteristic\" " +
                "WHERE \"product_characteristic_id\" = ?";

        return findOne(findOneQuery, new ProductCharacteristicRowMapper(), id);
    }

    @Override
    public ProductCharacteristic update(ProductCharacteristic productCharacteristic) {
        String updateQuery = "UPDATE \"product_characteristic\" SET \"product_type_id\"=?, " +
                "\"characteristic_name\"=?, \"measure\"=?, \"data_type_id\"=? " +
                "WHERE \"product_characteristic_id\" = ?";

        executeUpdate(updateQuery, productCharacteristic.getProductType().getProductTypeId(),
                productCharacteristic.getCharacteristicName(), productCharacteristic.getMeasure(),
                productCharacteristic.getDataType().getCategoryId(),
                productCharacteristic.getProductCharacteristicId());

        return productCharacteristic;
    }

    @Override
    public void delete(Long id) {
        String deleteQuery = "DELETE FROM \"product_characteristic\" WHERE \"product_characteristic_id\" = ?";

        executeUpdate(deleteQuery, id);
    }

    @Override
    public List<ProductCharacteristic> findAll() {
        String findAllQuery = "SELECT \"product_characteristic_id\", \"product_type_id\", \"characteristic_name\", " +
                "\"measure\", \"data_type_id\" FROM \"product_characteristic\"";

        return findMultiple(findAllQuery, new ProductCharacteristicRowMapper());
    }

    @Override
    public List<ProductCharacteristic> findByProductTypeId(Long productTypeId) {
        String query = "SELECT \"product_characteristic_id\", \"product_type_id\", \"characteristic_name\", " +
                "\"measure\", \"data_type_id\" FROM \"product_characteristic\" " +
                "WHERE \"product_type_id\" = ?";

        return findMultiple(query, new ProductCharacteristicRowMapper(), productTypeId);
    }

    @Override
    public List<ProductCharacteristic> findByDataTypeId(Long dataTypeId) {
        String query = "SELECT \"product_characteristic_id\", \"product_type_id\", \"characteristic_name\", " +
                "\"measure\", \"data_type_id\" FROM \"product_characteristic\" " +
                "WHERE \"data_type_id\" = ?";

        return findMultiple(query, new ProductCharacteristicRowMapper(), dataTypeId);
    }

    @Override
    @Transactional
    public void updateBatch(List<ProductCharacteristic> productCharacteristics) {
        String updateQuery = "UPDATE \"product_characteristic\" SET \"characteristic_name\"=?, \"measure\"=?, " +
                "\"data_type_id\"=? WHERE \"product_characteristic_id\" = ?";

        List<Object[]> batchArgs = new ArrayList<>();
        for (ProductCharacteristic characteristic : productCharacteristics) {
            batchArgs.add(new Object[]{characteristic.getCharacteristicName(),
                    characteristic.getMeasure(), characteristic.getDataType().getCategoryId(),
                    characteristic.getProductCharacteristicId()});
        }
        batchUpdate(updateQuery, batchArgs);
    }

    @Override
    @Transactional
    public void deleteBatch(List<ProductCharacteristic> productCharacteristics) {
        String deleteQuery = "DELETE FROM \"product_characteristic\" WHERE \"product_characteristic_id\" = ?";

        List<Object[]> batchArgs = new ArrayList<>();
        for (ProductCharacteristic characteristic : productCharacteristics) {
            batchArgs.add(new Object[]{characteristic.getProductCharacteristicId()});
        }
        batchUpdate(deleteQuery, batchArgs);
    }

    @Override
    @Transactional
    public void persistBatch(List<ProductCharacteristic> productCharacteristics) {
        String insertQuery = "INSERT INTO \"product_characteristic\" (\"product_type_id\", " +
                "\"characteristic_name\", \"measure\", \"data_type_id\") " +
                "VALUES (?, ?, ?, ?)";

        List<Object[]> batchArgs = new ArrayList<>();
        for (ProductCharacteristic characteristic : productCharacteristics) {
            batchArgs.add(new Object[]{characteristic.getProductType().getProductTypeId(),
                    characteristic.getCharacteristicName(), characteristic.getMeasure(),
                    characteristic.getDataType().getCategoryId()});
        }
        batchUpdate(insertQuery, batchArgs);
    }

    @Override
    public void deleteByProductTypeId(Long productTypeId) {
        String deleteQuery = "DELETE FROM \"product_characteristic\" WHERE \"product_type_id\" = ?";

        executeUpdate(deleteQuery, productTypeId);
    }

    private class ProductCharacteristicRowMapper implements RowMapper<ProductCharacteristic> {
        @Override
        public ProductCharacteristic mapRow(ResultSet rs, int rowNum) throws SQLException {
            ProductCharacteristicProxy productCharacteristic = proxyFactory.getObject();

            productCharacteristic.setProductCharacteristicId(rs.getLong("product_characteristic_id"));
            productCharacteristic.setProductTypeId(rs.getLong("product_type_id"));
            productCharacteristic.setCharacteristicName(rs.getString("characteristic_name"));
            productCharacteristic.setMeasure(rs.getString("measure"));
            productCharacteristic.setDataTypeId(rs.getLong("data_type_id"));

            return productCharacteristic;
        }
    }
}
