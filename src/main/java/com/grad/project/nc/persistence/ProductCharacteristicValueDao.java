package com.grad.project.nc.persistence;

import com.grad.project.nc.model.ProductCharacteristic;
import com.grad.project.nc.model.ProductCharacteristicValue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Repository
public class ProductCharacteristicValueDao   {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ProductCharacteristicValueDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }



    @Transactional
    public ProductCharacteristicValue add(ProductCharacteristicValue entity) {
        String QUERY = "INSERT INTO product_characteristic_value(product_id,product_characteristic_id, number_value , date_value , string_value  ) VALUES (?,?,?,?,?) ";
        jdbcTemplate.update(QUERY, new Object[]{entity.getProductId(),entity.getProductCharacteristicId(),entity.getNumberValue(),entity.getDateValue(),entity.getStringValue()});

        return entity;
    }





    @Transactional
    public ProductCharacteristicValue update(ProductCharacteristicValue entity) {

        String QUERY = "UPDATE product_characteristic_value SET number_value = ? , date_value = ?, string_value = ? WHERE (product_id = ? AND product_characteristic_id = ?)";
        jdbcTemplate.update(QUERY, new Object[]{entity.getNumberValue(),entity.getDateValue(),entity.getStringValue(),entity.getProductId(),entity.getProductCharacteristicId()});

        return entity;
    }



    @Transactional
    public ProductCharacteristicValue find(Long productId, Long productCharacteristicId) {

        final String QUERY = "SELECT product_id" +
                ",product_characteristic_id" +
                ", number_value , date_value , string_value " +
                "FROM product_characteristic_value" +
                " WHERE (product_id = ? AND product_characteristic_id = ? )";

        ProductCharacteristicValue productCharacteristicValue = null;
        try {
            productCharacteristicValue = jdbcTemplate.queryForObject(QUERY
                    , new Object[]{productId, productCharacteristicId}
                    , new ProductCharacteristicValueRowMapper());
        } catch (EmptyResultDataAccessException ex){

        }

        return  productCharacteristicValue;

    }



    @Transactional
    public Collection<ProductCharacteristicValue> findAll() {
        final String QUERY = "SELECT product_id" +
                ",product_characteristic_id" +
                ", number_value , date_value , string_value " +
                "FROM product_characteristic_value" ;
        return jdbcTemplate.query(QUERY, new ProductCharacteristicValueRowMapper());
    }


    public void delete(ProductCharacteristicValue entity) {

        final String DELETE_QUERY = "DELETE FROM product_characteristic_value WHERE (product_id = ? AND product_characteristic_id = ? )";
        jdbcTemplate.update(DELETE_QUERY,entity.getProductId(),entity.getProductCharacteristicId());

    }

    public List<ProductCharacteristicValue> findByProductId(Long id){
        final String QUERY = "SELECT product_id" +
                ",product_characteristic_id" +
                ", number_value , date_value , string_value " +
                "FROM product_characteristic_value WHERE product_id = ?" ;
        return jdbcTemplate.query(QUERY,new Object[]{id}, new ProductCharacteristicValueRowMapper());

    }

    private static final class ProductCharacteristicValueRowMapper implements RowMapper<ProductCharacteristicValue> {
        @Override
        public ProductCharacteristicValue mapRow(ResultSet rs, int rowNum) throws SQLException {
            ProductCharacteristicValue productCharacteristicValue = new ProductCharacteristicValue();

            productCharacteristicValue.setProductId(rs.getLong("product_id"));
            productCharacteristicValue.setProductCharacteristicId(rs.getLong("product_characteristic_id"));


            productCharacteristicValue.setNumberValue(rs.getDouble("number_value"));
            if (rs.getTimestamp("date_value") != null) {
                productCharacteristicValue.setDateValue(rs.getTimestamp("date_value").toLocalDateTime());
            }
            productCharacteristicValue.setStringValue(rs.getString("string_value"));
            return productCharacteristicValue;
        }
    }
}
