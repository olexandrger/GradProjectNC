package com.grad.project.nc.persistence;

import com.grad.project.nc.model.Category;

import java.util.List;

public interface CategoryDao extends CrudDao<Category> {
    List<Category> findByCategoryTypeId(Long categoryTypeId);

    List<Category> findByCategoryTypeName(String categoryTypeName);

    Category findProductOrderAim(Long productOrderId);

    Category findProductOrderStatus(Long productOrderId);

    Category findComplainStatus(Long complainId);

    Category findComplainReason(Long complainId);

    Category findProductInstanceStatus(Long productInstanceId);

    Category findDomainType(Long domainId);

    Category findCategoryByName(String categoryName);
}
