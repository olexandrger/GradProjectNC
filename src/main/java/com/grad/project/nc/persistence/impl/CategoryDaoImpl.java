package com.grad.project.nc.persistence.impl;

import com.grad.project.nc.model.Category;
import com.grad.project.nc.model.proxy.CategoryProxy;
import com.grad.project.nc.persistence.AbstractDao;
import com.grad.project.nc.persistence.CategoryDao;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Pavlo Rospopa
 */
@Repository
public class CategoryDaoImpl extends AbstractDao implements CategoryDao {

    private static final String PK_COLUMN_NAME = "category_id";

    private final ObjectFactory<CategoryProxy> proxyFactory;

    @Autowired
    public CategoryDaoImpl(JdbcTemplate jdbcTemplate, ObjectFactory<CategoryProxy> proxyFactory) {
        super(jdbcTemplate);
        this.proxyFactory = proxyFactory;
    }

    @Override
    public Category add(Category category) {
        String insertQuery = "INSERT INTO \"category\" (\"category_name\", \"category_type_id\") VALUES (?, ?)";
        Long categoryId = executeInsertWithId(insertQuery, PK_COLUMN_NAME, category.getCategoryName(),
                category.getCategoryType().getCategoryTypeId());

        category.setCategoryId(categoryId);

        return category;
    }

    @Override
    public Category update(Category category) {
        String updateQuery = "UPDATE \"category\" SET \"category_name\"=?, \"category_type_id\"=? " +
                "WHERE \"category_id\"=?";
        executeUpdate(updateQuery, category.getCategoryName(), category.getCategoryType().getCategoryTypeId(),
                category.getCategoryId());

        return category;
    }

    @Override
    public Category find(Long id) {
        String query = "SELECT \"category_id\", \"category_name\", \"category_type_id\" FROM \"category\" " +
                "WHERE \"category_id\"=?";
        return findOne(query, new CategoryRowMapper(), id);
    }

    @Override
    public List<Category> findAll() {
        String findAllQuery = "SELECT \"category_id\", \"category_name\", \"category_type_id\" FROM \"category\"";
        return findMultiple(findAllQuery, new CategoryRowMapper());
    }

    @Override
    public List<Category> findByCategoryTypeId(Long categoryTypeId) {
        String query = "SELECT \"category_id\", \"category_name\", \"category_type_id\" FROM \"category\" " +
                "WHERE \"category_type_id\"=?";
        return findMultiple(query, new CategoryRowMapper(), categoryTypeId);
    }

    @Override
    public List<Category> findByCategoryTypeName(String categoryTypeName) {
        String query = "SELECT c.\"category_id\", c.\"category_name\", c.\"category_type_id\" " +
                "FROM \"category\" c " +
                "JOIN \"category_type\" ct " +
                "ON c.\"category_type_id\" = ct.\"category_type_id\" " +
                "WHERE ct.\"category_type_name\" = ?";
        return findMultiple(query, new CategoryRowMapper(), categoryTypeName);
    }

    @Override
    public void delete(Long id) {
        String deleteQuery = "DELETE FROM \"category\" WHERE category_id = ?";
        executeUpdate(deleteQuery, id);
    }

    @Override
    public Category findProductOrderAim(Long productOrderId) {
        String query = "SELECT c.\"category_id\", c.\"category_name\", c.\"category_type_id\" " +
                "FROM \"category\" c " +
                "JOIN \"product_order\" po " +
                "ON po.\"order_aim_id\"=c.\"category_id\" " +
                "AND po.\"product_order_id\"=?";
        return findOne(query, new CategoryRowMapper(), productOrderId);
    }

    @Override
    public Category findProductOrderStatus(Long productOrderId) {
        String query = "SELECT c.\"category_id\", c.\"category_name\", c.\"category_type_id\" " +
                "FROM \"category\" c " +
                "JOIN \"product_order\" po " +
                "ON po.\"status_id\"=c.\"category_id\" " +
                "AND po.\"product_order_id\"=?";
        return findOne(query, new CategoryRowMapper(), productOrderId);
    }

    @Override
    public Category findComplainStatus(Long complainId) {
        String query = "SELECT c.\"category_id\", c.\"category_name\", c.\"category_type_id\" " +
                "FROM \"category\" c " +
                "JOIN \"complain\" co " +
                "ON co.\"status_id\"=c.\"category_id\" " +
                "AND co.\"complain_id\"=?";
        return findOne(query, new CategoryRowMapper(), complainId);
    }

    @Override
    public Category findComplainReason(Long complainId) {
        String query = "SELECT c.\"category_id\", c.\"category_name\", c.\"category_type_id\" " +
                "FROM \"category\" c " +
                "JOIN \"complain\" co " +
                "ON co.\"complain_reason_id\"=c.\"category_id\" " +
                "AND co.\"complain_id\"=?";
        return findOne(query, new CategoryRowMapper(), complainId);
    }

    @Override
    public Category findProductInstanceStatus(Long productInstanceId) {
        String query = "SELECT c.\"category_id\", c.\"category_name\", c.\"category_type_id\" " +
                "FROM \"category\" c " +
                "JOIN \"product_instance\" pi " +
                "ON pi.\"status_id\"=c.\"category_id\" " +
                "AND pi.\"instance_id\"=?";
        return findOne(query, new CategoryRowMapper(), productInstanceId);
    }

    @Override
    public Category findDomainType(Long domainId) {
        String query = "SELECT c.\"category_id\", c.\"category_name\", c.\"category_type_id\" " +
                "FROM \"category\" c " +
                "JOIN \"domain\" d " +
                "ON d.\"domain_type_id\"=c.\"category_id\" " +
                "AND d.\"domain_id\"=?";
        return findOne(query, new CategoryRowMapper(), domainId);
    }

    private class CategoryRowMapper implements RowMapper<Category> {
        @Override
        public Category mapRow(ResultSet resultSet, int i) throws SQLException {
            CategoryProxy category = proxyFactory.getObject();
            category.setCategoryId(resultSet.getLong("category_id"));
            category.setCategoryName(resultSet.getString("category_name"));
            category.setCategoryTypeId(resultSet.getLong("category_type_id"));

            return category;
        }
    }
}