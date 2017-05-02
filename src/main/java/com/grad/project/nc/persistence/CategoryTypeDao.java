package com.grad.project.nc.persistence;

import com.grad.project.nc.model.CategoryType;

public interface CategoryTypeDao extends CrudDao<CategoryType> {
    CategoryType findCategoryTypeByCategoryId(Long categoryId);
}
