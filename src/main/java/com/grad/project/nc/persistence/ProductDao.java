package com.grad.project.nc.persistence;

import com.grad.project.nc.model.Product;
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
public class ProductDao  {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Transactional
    public void insertProduct(Product product) {

        SimpleJdbcInsert insertProductQuery = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("product")
                .usingGeneratedKeyColumns("product_id");

        Map<String, Object> parameters = new HashMap<String, Object>(3);
        parameters.put("product_name", product.getName());
        parameters.put("product_description", product.getDescription());
        parameters.put("product_type_id", product.getProductTypeId());

        Number newId = insertProductQuery.executeAndReturnKey(parameters);
        product.setProductId(newId.longValue());

    }

    @Transactional
    public Product readProductById(int id) {
        final String SELECT_QUERY = "SELECT product_id" +
                ",product_name" +
                ",product_description" +
                ",product_type_id " +
                "FROM product " +
                "WHERE product_id = ?";

        Product product = jdbcTemplate.queryForObject(SELECT_QUERY,
                new Object[]{id}, new ProductRowMapper());

        return product;
    }

    @Transactional
    public void updateProduct(Product product) {
        final String UPDATE_QUERY = "UPDATE product SET product_name = ?" +
                ", product_description = ?" +
                ", product_type_id = ? " +
                "WHERE product_id = ? ";

        jdbcTemplate.update(UPDATE_QUERY, new Object[]{product.getName()
                ,product.getDescription()
                ,product.getProductTypeId()
                ,product.getProductId()});


    }

    @Transactional
    public void deleteProductById(int id) {

        final String DELETE_QUERY = "DELETE FROM product WHERE product_id = ?";

        jdbcTemplate.update(DELETE_QUERY,id);

    }
}
