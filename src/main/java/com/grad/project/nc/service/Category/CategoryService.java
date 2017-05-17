package com.grad.project.nc.service.Category;

import com.grad.project.nc.model.Category;
import com.grad.project.nc.model.ProductInstance;

import java.util.Collection;

/**
 * Created by DeniG on 11.05.2017.
 */
public interface CategoryService {
    public Category getByProductInstanceId(Long productInstanceId);
    public Collection<Category> findByCategoryTypeName(String categoryTypeName);
}
