package com.grad.project.nc.model.proxy;

import com.grad.project.nc.model.Category;
import com.grad.project.nc.model.CategoryType;
import com.grad.project.nc.persistence.CategoryTypeDao;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class CategoryProxy extends Category {
    @Getter
    @Setter
    private Long categoryTypeId;

    private boolean categoryTypeLoaded;

    private final CategoryTypeDao categoryTypeDao;

    @Autowired
    public CategoryProxy(CategoryTypeDao categoryTypeDao) {
        this.categoryTypeDao = categoryTypeDao;
    }

    @Override
    public CategoryType getCategoryType() {
        if (!categoryTypeLoaded) {
            this.setCategoryType(categoryTypeDao.find(getCategoryTypeId()));
        }
        return super.getCategoryType();
    }

    @Override
    public void setCategoryType(CategoryType categoryType) {
        categoryTypeLoaded = true;
        super.setCategoryType(categoryType);
    }
}
