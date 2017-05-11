package com.grad.project.nc.persistence.impl;

import com.grad.project.nc.model.*;
import com.grad.project.nc.model.proxy.ProductProxy;
import com.grad.project.nc.persistence.*;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.*;

/**
 * edited by Pavlo Rospopa
 */
@Repository
public class ProductDaoImpl extends AbstractDao implements ProductDao {

    private static final String PK_COLUMN_NAME = "product_id";

    private final ObjectFactory<ProductProxy> proxyFactory;

    @Autowired
    public ProductDaoImpl(JdbcTemplate jdbcTemplate, ObjectFactory<ProductProxy> proxyFactory) {
        super(jdbcTemplate);
        this.proxyFactory = proxyFactory;
    }

    @Override
    public Product add(Product product) {
        String insertQuery = "INSERT INTO \"product\" (\"product_name\", \"product_description\", \"is_active\", " +
                "\"product_type_id\") VALUES (?, ?, ?, ?)";

        Long productId = executeInsertWithId(insertQuery, PK_COLUMN_NAME, product.getProductName(), product.getProductDescription(),
                product.getIsActive(), product.getProductType().getProductTypeId());

        product.setProductId(productId);

        return product;
    }

    @Override
    public Product find(Long id) {
        String query = "SELECT \"product_id\", \"product_name\", \"product_description\", \"is_active\", " +
                "\"product_type_id\" FROM \"product\" WHERE \"product_id\"=?";
        return findOne(query, new ProductRowMapper(), id);
    }

    @Override
    public Product update(Product product) {
        String updateQuery = "UPDATE \"product\" SET " +
                "\"product_name\"=?, " +
                "\"product_description\" = ?, " +
                "\"is_active\"=?, " +
                "\"product_type_id\"=? " +
                "WHERE \"product_id\"=?";

        executeUpdate(updateQuery, product.getProductName(), product.getProductDescription(), product.getIsActive(),
                product.getProductType().getProductTypeId(), product.getProductId());

        return product;
    }

    @Override
    public void delete(Long id) {
        String deleteQuery = "DELETE FROM \"product\" WHERE \"product_id\" =?";
        executeUpdate(deleteQuery, id);
    }

    @Override
    public List<Product> findAll() {
        String findAllQuery = "SELECT \"product_id\", \"product_name\", \"product_description\", \"is_active\", " +
                "\"product_type_id\" FROM \"product\"";

        return findMultiple(findAllQuery, new ProductRowMapper());
    }

    @Override
    public List<Product> findFirstN(int n) {
        String findAllQuery = "SELECT \"product_id\", \"product_name\", \"product_description\", \"is_active\", " +
                "\"product_type_id\" FROM \"product\"ORDER BY product_id LIMIT " + n;

        return findMultiple(findAllQuery, new ProductRowMapper());
    }

    @Override
    public List<Product> findLastN(int n) {
        String findAllQuery = "SELECT \"product_id\", \"product_name\", \"product_description\", \"is_active\", " +
                "\"product_type_id\" FROM \"product\"ORDER BY product_id OFFSET " + n;

        return findMultiple(findAllQuery, new ProductRowMapper());
    }

    @Override
    public List<Product> findByRegionId(Long regionId) {
        String query = "SELECT p.\"product_id\", p.\"product_name\", p.\"product_description\", " +
                "p.\"is_active\", p.\"product_type_id\" FROM \"product\" p " +
                "JOIN \"product_region_price\" prp " +
                "ON p.\"product_id\" = prp.\"product_id\" " +
                "WHERE prp.\"region_id\" =?";

        return findMultiple(query, new ProductRowMapper(), regionId);
    }

    @Override
    public Product findByName(String productName) {
        String query = "SELECT \"product_id\", \"product_name\", \"product_description\", \"is_active\", " +
                "\"product_type_id\" FROM \"product\" WHERE \"product_name\"=?";

        return findOne(query, new ProductRowMapper(), productName);
    }

    @Override
    public List<Product> findByProductTypeId(Long productTypeId) {
        String query = "SELECT \"product_id\", \"product_name\", \"product_description\", \"is_active\", " +
                "\"product_type_id\" FROM \"product\" WHERE \"product_type_id\"=?";

        return findMultiple(query, new ProductRowMapper(), productTypeId);
    }

    @Override
    public Product findByProductRegionPriceId(Long productRegionPriceId) {
        String query = "SELECT pr.\"product_id\", pr.\"product_name\", pr.\"product_description\", pr.\"is_active\", " +
                "pr.\"product_type_id\" FROM \"product\" pr " +
                "WHERE pr.\"product_id\" = " +
                "(SELECT prp.\"product_id\" " +
                "FROM \"product_region_price\" prp WHERE prp.\"price_id\" = ?)";

        return findOne(query, new ProductRowMapper(), productRegionPriceId);
    }

    private class ProductRowMapper implements RowMapper<Product> {

        @Override
        public Product mapRow(ResultSet rs, int rowNum) throws SQLException {
            ProductProxy product = proxyFactory.getObject();

            product.setProductId(rs.getLong("product_id"));
            product.setProductName(rs.getString("product_name"));
            product.setProductDescription(rs.getString("product_description"));
            product.setIsActive(rs.getBoolean("is_active"));
            product.setProductTypeId(rs.getLong("product_type_id"));

            return product;
        }
    }
}