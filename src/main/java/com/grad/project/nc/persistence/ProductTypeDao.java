package com.grad.project.nc.persistence;

import com.grad.project.nc.model.ProductType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alex on 4/25/2017.
 */
@Repository
public class ProductTypeDao implements CrudDao<ProductType> {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Transactional
    @Override
    public ProductType add(ProductType productType)  {

        SimpleJdbcInsert insertProductQuery = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("product_type")
                .usingGeneratedKeyColumns("product_type_id");

        Map<String, Object> parameters = new HashMap<String, Object>(2);
        parameters.put("product_type_name", productType.getProductTypeName());
        parameters.put("product_type_description", productType.getProductTypeDescription());

        Number newId = insertProductQuery.executeAndReturnKey(parameters);
        productType.setProductTypeId(newId.longValue());

        return productType;

    }

    @Transactional
    @Override
    public ProductType find(long id) {
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
    @Override
    public ProductType update(ProductType productType)  {
        final String UPDATE_QUERY = "UPDATE product_type SET product_type_name = ?" +
                ", product_type_description = ?" +
                "WHERE product_type_id = ? ";

        jdbcTemplate.update(UPDATE_QUERY, new Object[]{productType.getProductTypeName()
                ,productType.getProductTypeDescription()
                ,productType.getProductTypeId()});

        return productType;


    }

    @Transactional
    @Override
    public void delete(ProductType entity)  {

        final String DELETE_QUERY = "DELETE FROM product_type WHERE product_type_id = ?";

        jdbcTemplate.update(DELETE_QUERY,entity.getProductTypeId());

    }



    @Override
    public Collection<ProductType> findAll() {
        final String SELECT_QUERY = "SELECT product_type_id" +
                ",product_type_name" +
                ",product_type_description " +
                "FROM product_type ";
        return jdbcTemplate.query(SELECT_QUERY,new ProductTypeRowMapper());
    }

    public static final class ProductTypeRowMapper implements RowMapper<ProductType> {
        @Override
        public ProductType mapRow(ResultSet rs, int rowNum) throws SQLException {
            ProductType productType = new ProductType();

            productType.setProductTypeId(rs.getLong("product_type_id"));
            productType.setProductTypeName(rs.getString("product_type_name"));
            productType.setProductTypeDescription(rs.getString("product_type_description"));

            return productType;
        }
    }


}
