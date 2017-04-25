package com.grad.project.nc.persistence;

import com.grad.project.nc.model.Product;

import com.grad.project.nc.model.ProductType;
import com.grad.project.nc.persistence.mappers.ProductTypeRowMapper;
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
public class ProductTypeDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Transactional
    public void insertProductType(ProductType productType) {

        SimpleJdbcInsert insertProductQuery = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("product_type")
                .usingGeneratedKeyColumns("product_type_id");

        Map<String, Object> parameters = new HashMap<String, Object>(2);
        parameters.put("product_type_name", productType.getProductTypeName());
        parameters.put("product_type_description", productType.getProductTypeDescription());

        Number newId = insertProductQuery.executeAndReturnKey(parameters);
        productType.setProductTypeId(newId.longValue());

    }

    @Transactional
    public ProductType readProductTypeById(int id) {
        final String SELECT_QUERY = "SELECT product_type_id" +
                ",product_type_name" +
                ",product_type_description " +
                "FROM product_type " +
                "WHERE product_type_id = ?";

        ProductType productType = jdbcTemplate.queryForObject(SELECT_QUERY,
                new Object[]{id}, new ProductTypeRowMapper());

        return productType;
    }

    @Transactional
    public void updateProductType(ProductType productType) {
        final String UPDATE_QUERY = "UPDATE product_type SET product_type_name = ?" +
                ", product_type_description = ?" +
                "WHERE product_type_id = ? ";

        jdbcTemplate.update(UPDATE_QUERY, new Object[]{productType.getProductTypeName()
                ,productType.getProductTypeDescription()
                ,productType.getProductTypeId()});


    }

    @Transactional
    public void deleteProductTypeById(int id) {

        final String DELETE_QUERY = "DELETE FROM product_type WHERE product_type_id = ?";

        jdbcTemplate.update(DELETE_QUERY,id);

    }
}
