package com.grad.project.nc.persistence;

import com.grad.project.nc.model.Product;
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


@Repository
public class ProductDao implements CrudDao<Product> {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ProductDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    @Override
    public Product add(Product product) {

        SimpleJdbcInsert insertProductQuery = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("product")
                .usingGeneratedKeyColumns("product_id");

        Map<String, Object> parameters = new HashMap<String, Object>(3);
        parameters.put("product_name", product.getName());
        parameters.put("product_description", product.getDescription());
        parameters.put("product_type_id", product.getProductTypeId());

        Number newId = insertProductQuery.executeAndReturnKey(parameters);
        product.setProductId(newId.longValue());
        return product;

    }

    @Transactional
    @Override
    public Product find(long id)  {
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
    @Override
    public Product update(Product product) {
        final String UPDATE_QUERY = "UPDATE product SET product_name = ?" +
                ", product_description = ?" +
                ", product_type_id = ? " +
                "WHERE product_id = ? ";

        jdbcTemplate.update(UPDATE_QUERY, product.getName()
                ,product.getDescription()
                ,product.getProductTypeId()
                ,product.getProductId());


        return product;
    }

    @Transactional
    @Override
    public void delete(Product entity){

        final String DELETE_QUERY = "DELETE FROM product WHERE product_id = ?";

        jdbcTemplate.update(DELETE_QUERY,entity.getProductId());

    }


    @Override
    public Collection<Product> findAll() {
        final String SELECT_QUERY = "SELECT product_id" +
                ",product_name" +
                ",product_description" +
                ",product_type_id " +
                "FROM product ";

        return jdbcTemplate.query(SELECT_QUERY, new ProductRowMapper());
    }

    public static final class ProductRowMapper implements RowMapper<Product> {

        @Override
        public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
            Product product = new Product();

            product.setProductId(rs.getLong("product_id"));
            product.setName(rs.getString("product_name"));
            product.setDescription(rs.getString("product_description"));
            product.setProductTypeId(rs.getLong("product_type_id"));

            return product;
        }
    }


}
