package com.grad.project.nc.persistence;

import com.grad.project.nc.model.Product;
import com.grad.project.nc.model.ProductCharacteristic;
import com.grad.project.nc.persistence.mappers.ProductCharacteristicRowMapper;
import com.grad.project.nc.persistence.mappers.ProductRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alex on 4/25/2017.
 */
@Repository
public class ProductCharacteristicDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Transactional
    public void insertProductCharacteristic(ProductCharacteristic productCharacteristic) {

        SimpleJdbcInsert insertProductQuery = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("product_characteristic")
                .usingGeneratedKeyColumns("product_characteristic_id");

        Map<String, Object> parameters = new HashMap<String, Object>(4);
        parameters.put("product_type_id", productCharacteristic.getProductTypeId());
        parameters.put("characteristic_name", productCharacteristic.getCharacteristicName());
        parameters.put("measure", productCharacteristic.getMeasure());
        parameters.put("data_type_id", productCharacteristic.getDataTypeId());

        Number newId = insertProductQuery.executeAndReturnKey(parameters);
        productCharacteristic.setProductCharacteristicId(newId.longValue());

    }

    @Transactional
    public ProductCharacteristic readProductCharacteristicById(int id) {
        final String SELECT_QUERY = "SELECT product_characteristic_id" +
                ",product_type_id" +
                ",characteristic_name" +
                ",measure" +
                ",data_type_id" +
                " FROM product_characteristic " +
                "WHERE product_characteristic_id = ?";

        ProductCharacteristic productCharacteristic = jdbcTemplate.queryForObject(SELECT_QUERY,
                new Object[]{id}, new ProductCharacteristicRowMapper());

        return productCharacteristic;
    }

    @Transactional
    public void updateProductCharacteristic(ProductCharacteristic productCharacteristic) {
        final String UPDATE_QUERY = "UPDATE product_characteristic SET product_type_id = ?" +
                ", characteristic_name = ?" +
                ", measure = ?" +
                ", data_type_id = ? " +
                "WHERE product_characteristic_id = ? ";

        jdbcTemplate.update(UPDATE_QUERY, new Object[]{productCharacteristic.getProductTypeId()
                ,productCharacteristic.getCharacteristicName()
                ,productCharacteristic.getMeasure()
                ,productCharacteristic.getDataTypeId()
                ,productCharacteristic.getProductCharacteristicId()});


    }

    @Transactional
    public void deleteProductCharacteristicById(int id) {

        final String DELETE_QUERY = "DELETE FROM product_characteristic WHERE product_characteristic_id = ?";

        jdbcTemplate.update(DELETE_QUERY,id);

    }
}
