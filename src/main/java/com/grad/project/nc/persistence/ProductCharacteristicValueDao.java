package com.grad.project.nc.persistence;

import com.grad.project.nc.model.ProductCharacteristicValue;
import com.grad.project.nc.model.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

/**
 * Created by Alex on 4/25/2017.
 */
@Repository
public class ProductCharacteristicValueDao  {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ProductCharacteristicValueDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }


    @Transactional
    public ProductCharacteristicValue add(ProductCharacteristicValue entity) {
        String QUERY = "INSERT INTO product_characteristic_value(product_id,product_characteristic_id,value_id) VALUES (?,?,?) ";
        jdbcTemplate.update(QUERY, new Object[]{entity.getProductId(),entity.getProductCharacteristicId(),entity.getValueId()});

        return entity;
    }


    @Transactional
    public ProductCharacteristicValue update(ProductCharacteristicValue entity) {

        String QUERY = "UPDATE product_characteristic_value SET value_id = ? WHERE (product_id = ? AND product_characteristic_id = ?)";
        jdbcTemplate.update(QUERY, new Object[]{entity.getValueId(),entity.getProductId(),entity.getProductCharacteristicId()});

        return entity;
    }



    @Transactional
    public ProductCharacteristicValue find(Long productId, Long productCharacteristicId) {

        final String QUERY = "SELECT product_id" +
                ",product_characteristic_id" +
                ",value_id " +
                "FROM product_characteristic_value" +
                " WHERE (product_id = ? AND product_characteristic_id = ? )";

        ProductCharacteristicValue productCharacteristicValue = jdbcTemplate.queryForObject(QUERY
                ,new Object[]{productId,productCharacteristicId}
                ,new ProductCharacteristicValueRowMapper());

        return  productCharacteristicValue;

    }


    public Collection<ProductCharacteristicValue> findAll() {
        final String QUERY = "SELECT product_id" +
                ",product_characteristic_id" +
                ",value_id " +
                "FROM product_characteristic_value" ;
        return jdbcTemplate.query(QUERY, new ProductCharacteristicValueRowMapper());
    }


    public void delete(ProductCharacteristicValue entity) {

        final String DELETE_QUERY = "DELETE FROM product_characteristic_value WHERE (product_id = ? AND product_characteristic_id = ? )";
        jdbcTemplate.update(DELETE_QUERY,entity.getProductId(),entity.getProductCharacteristicId());

    }

    public static final class ProductCharacteristicValueRowMapper implements RowMapper<ProductCharacteristicValue> {
        @Override
        public ProductCharacteristicValue mapRow(ResultSet rs, int rowNum) throws SQLException {
            ProductCharacteristicValue productCharacteristicValue = new ProductCharacteristicValue();

            productCharacteristicValue.setProductId(rs.getLong("product_id"));
            productCharacteristicValue.setProductCharacteristicId(rs.getLong("product_characteristic_id"));
            productCharacteristicValue.setValueId(rs.getLong("value_id"));
            return productCharacteristicValue;
        }
    }
}
