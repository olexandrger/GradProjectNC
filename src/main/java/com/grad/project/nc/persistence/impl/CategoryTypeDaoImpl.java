package com.grad.project.nc.persistence.impl;

import com.grad.project.nc.model.CategoryType;
import com.grad.project.nc.persistence.AbstractDao;
import com.grad.project.nc.persistence.CategoryTypeDao;
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
public class CategoryTypeDaoImpl extends AbstractDao<CategoryType> implements CategoryTypeDao {
    private static final String PK_COLUMN_NAME = "category_type_id";

    @Autowired
    CategoryTypeDaoImpl(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    @Override
    public CategoryType add(CategoryType entity) {
        String insertQuery = "INSERT INTO \"category_type\" (\"category_type_name\") VALUES (?)";
        Long categoryTypeId = executeInsertWithId(insertQuery, PK_COLUMN_NAME, entity.getCategoryTypeName());

        entity.setCategoryTypeId(categoryTypeId);

        return entity;
    }

    @Override
    public CategoryType update(CategoryType entity) {
        String updateQuery = "UPDATE \"category_type\" SET \"category_type_name\"=? WHERE \"category_type_id\"=?";
        executeUpdate(updateQuery,  entity.getCategoryTypeName(), entity.getCategoryTypeId());

        return entity;
    }

    @Override
    public CategoryType find(Long id) {
        String query = "SELECT \"category_type_id\", \"category_type_name\" " +
                "FROM \"category_type\" " +
                "WHERE \"category_type_id\"=?";
        return findOne(query, new CategoryTypeRowMapper(), id);
    }

    @Override
    public List<CategoryType> findAll() {
        String findAllQuery = "SELECT \"category_type_id\", \"category_type_name\" FROM \"category_type\"";
        return query(findAllQuery, new CategoryTypeRowMapper());
    }

    @Override
    public void delete(CategoryType entity) {
        String deleteQuery = "DELETE FROM \"category_type\" WHERE category_type_id=?";
        executeUpdate(deleteQuery, entity.getCategoryTypeId());
    }

    @Override
    public CategoryType findCategoryTypeByCategoryId(Long categoryId) {
        String query = "SELECT ct.\"category_type_id\", ct.\"category_type_name\" " +
                "FROM \"category_type\" ct" +
                "JOIN \"category\" c " +
                "ON c.\"category_type_id\"=ct.\"category_type_id\" " +
                "AND c.\"category_id\"=?";
        return findOne(query, new CategoryTypeRowMapper(), categoryId);
    }

    private class CategoryTypeRowMapper implements RowMapper<CategoryType> {
        @Override
        public CategoryType mapRow(ResultSet resultSet, int i) throws SQLException {
            CategoryType categoryType = new CategoryType();

            categoryType.setCategoryTypeId(resultSet.getLong("category_type_id"));
            categoryType.setCategoryTypeName(resultSet.getString("category_type_name"));

            return categoryType;
        }
    }
}