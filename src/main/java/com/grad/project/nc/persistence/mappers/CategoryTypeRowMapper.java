package com.grad.project.nc.persistence.mappers;

import com.grad.project.nc.model.CategoryType;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoryTypeRowMapper implements RowMapper<CategoryType> {
    @Override
    public CategoryType mapRow(ResultSet resultSet, int i) throws SQLException {
        CategoryType categoryType = new CategoryType();

        categoryType.setCategoryTypeId(resultSet.getInt("category_type_id"));
        categoryType.setCategoryTypeName(resultSet.getString("category_type_name"));

        return categoryType;
    }
}
