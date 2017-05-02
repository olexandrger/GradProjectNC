package com.grad.project.nc.persistence.impl;

import com.grad.project.nc.model.*;
import com.grad.project.nc.persistence.AbstractDao;
import com.grad.project.nc.persistence.CategoryDao;
import com.grad.project.nc.persistence.CategoryTypeDao;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

/**
 * @author Pavlo Rospopa
 */
@Repository
public class CategoryDaoImpl extends AbstractDao<Category> implements CategoryDao {

    private static final String PK_COLUMN_NAME = "category_id";

    @Autowired
    private CategoryTypeDao categoryTypeDao;

    @Autowired
    public CategoryDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public Category add(Category entity) {
        String insertQuery = "INSERT INTO \"category\" (\"category_name\", \"category_type_id\") VALUES (?, ?)";
        Long categoryId = executeInsertWithId(insertQuery, PK_COLUMN_NAME, entity.getCategoryName(),
                entity.getCategoryType().getCategoryTypeId());

        entity.setCategoryId(categoryId);

        return entity;
    }

    @Override
    public Category update(Category entity) {
        String updateQuery = "UPDATE \"category\" SET \"category_name\"=?, \"category_type_id\"=? " +
                "WHERE \"category_id\"=?";
        jdbcTemplate.update(updateQuery, entity.getCategoryName(), entity.getCategoryType().getCategoryTypeId(),
                entity.getCategoryId());

        return entity;
    }

    @Override
    public Category find(long id) {
        String query = "SELECT \"category_id\", \"category_name\", \"category_type_id\" FROM \"category\" " +
                "WHERE \"category_id\"=?";
        return findOne(query, new CategoryRowMapper(), id);
    }

    @Override
    public Collection<Category> findAll() {
        String findAllQuery = "SELECT \"category_id\", \"category_name\", \"category_type_id\" FROM \"category\"";
        return jdbcTemplate.query(findAllQuery, new CategoryRowMapper());
    }

    @Override
    public Collection<Category> findByCategoryTypeId(Long categoryTypeId) {
        String findAllQuery = "SELECT \"category_id\", \"category_name\", \"category_type_id\" FROM \"category\" " +
                "WHERE \"category_type_id\"=?";
        return jdbcTemplate.query(findAllQuery, new CategoryRowMapper(), categoryTypeId);
    }

    @Override
    public void delete(Category entity) {
        String deleteQuery = "DELETE FROM \"category\" WHERE category_id = ?";
        jdbcTemplate.update(deleteQuery, entity.getCategoryId());
    }

    @Override
    public Category findProductOrderAim(Long productOrderId) {
        String query = "SELECT c.\"category_id\", c.\"category_name\", c.\"category_type_id\" " +
                "FROM \"category\" c" +
                "JOIN \"product_order\" po " +
                "ON po.\"order_aim_id\"=c.\"category_id\" " +
                "AND po.\"product_order_id\"=?";
        return findOne(query, new CategoryRowMapper(), productOrderId);
    }

    @Override
    public Category findProductOrderStatus(Long productOrderId) {
        String query = "SELECT c.\"category_id\", c.\"category_name\", c.\"category_type_id\" " +
                "FROM \"category\" c" +
                "JOIN \"product_order\" po " +
                "ON po.\"status_id\"=c.\"category_id\" " +
                "AND po.\"product_order_id\"=?";
        return findOne(query, new CategoryRowMapper(), productOrderId);
    }

    @Override
    public Category findComplainStatus(Long complainId) {
        String query = "SELECT c.\"category_id\", c.\"category_name\", c.\"category_type_id\" " +
                "FROM \"category\" c" +
                "JOIN \"complain\" co " +
                "ON co.\"status_id\"=c.\"category_id\" " +
                "AND co.\"complain_id\"=?";
        return findOne(query, new CategoryRowMapper(), complainId);
    }

    @Override
    public Category findComplainReason(Long complainId) {
        String query = "SELECT c.\"category_id\", c.\"category_name\", c.\"category_type_id\" " +
                "FROM \"category\" c" +
                "JOIN \"complain\" co " +
                "ON co.\"complain_reason_id\"=c.\"category_id\" " +
                "AND co.\"complain_id\"=?";
        return findOne(query, new CategoryRowMapper(), complainId);
    }

    @Override
    public Category findProductInstanceStatus(Long productInstanceId) {
        String query = "SELECT c.\"category_id\", c.\"category_name\", c.\"category_type_id\" " +
                "FROM \"category\" c" +
                "JOIN \"product_instance\" pi " +
                "ON pi.\"status_id\"=c.\"category_id\" " +
                "AND pi.\"instance_id\"=?";
        return findOne(query, new CategoryRowMapper(), productInstanceId);
    }

    @Override
    public Category findDomainType(Long domainId) {
        String query = "SELECT c.\"category_id\", c.\"category_name\", c.\"category_type_id\" " +
                "FROM \"category\" c" +
                "JOIN \"domain\" d " +
                "ON d.\"domain_type_id\"=c.\"category_id\" " +
                "AND d.\"domain_id\"=?";
        return findOne(query, new CategoryRowMapper(), domainId);
    }

    private class CategoryProxy extends Category {
        private Long categoryTypeId;

        public Long getCategoryTypeId() {
            return categoryTypeId;
        }

        public void setCategoryTypeId(Long categoryTypeId) {
            this.categoryTypeId = categoryTypeId;
        }

        @Override
        public CategoryType getCategoryType() {
            if (super.getCategoryType() == null) {
                super.setCategoryType(categoryTypeDao.find(categoryTypeId));
            }
            return super.getCategoryType();
        }
    }

    private class CategoryRowMapper implements RowMapper<Category> {
        @Override
        public Category mapRow(ResultSet resultSet, int i) throws SQLException {
            CategoryProxy category = new CategoryProxy();
            category.setCategoryId(resultSet.getLong("category_id"));
            category.setCategoryName(resultSet.getString("category_name"));
            category.setCategoryTypeId(resultSet.getLong("category_type_id"));

            return category;
        }
    }
}