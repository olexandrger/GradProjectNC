package com.grad.project.nc.service.Category;

import com.grad.project.nc.model.Category;
import com.grad.project.nc.model.ProductInstance;
import com.grad.project.nc.persistence.CategoryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * Created by DeniG on 11.05.2017.
 */
@Service
public class CategoryServiceImpl implements CategoryService {

    CategoryDao categoryDao;

    @Autowired
    public CategoryServiceImpl(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }


    @Override
    public Category getByProductInstanceId(Long productInstanceId) {
        return categoryDao.findProductInstanceStatus(productInstanceId);
    }

    @Override
    public Collection<Category> findByCategoryTypeName(String categoryTypeName) {
        return categoryDao.findByCategoryTypeName(categoryTypeName);
    }

    @Override
    public Collection<Category> findByCategoryType(long categoryTypeId) {
        return categoryDao.findByCategoryTypeId(categoryTypeId);
    }
}
