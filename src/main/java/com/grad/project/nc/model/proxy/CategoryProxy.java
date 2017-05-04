package com.grad.project.nc.model.proxy;

import com.grad.project.nc.model.Category;
import com.grad.project.nc.model.CategoryType;
import com.grad.project.nc.persistence.CategoryTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class CategoryProxy extends Category {
    private Long categoryTypeId;

    private final CategoryTypeDao categoryTypeDao;

    @Autowired
    public CategoryProxy(CategoryTypeDao categoryTypeDao) {
        this.categoryTypeDao = categoryTypeDao;
    }

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
