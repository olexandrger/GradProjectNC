package com.grad.project.nc.persistence.mappers;

import com.grad.project.nc.model.Category;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryRowMapper implements RowMapper<Category> {
    @Override
    public Category mapRow(ResultSet resultSet, int i) throws SQLException {
        Category category = new Category();

        category.setCategoryId(resultSet.getLong("category_id"));
        category.setCategoryTypeId(resultSet.getLong("category_type_id"));
        category.setCategoryName(resultSet.getString("category_name"));

        return category;
    }
}
