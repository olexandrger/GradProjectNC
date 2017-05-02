package com.grad.project.nc.persistence;

import com.grad.project.nc.model.Category;

import java.util.Collection;

public interface CategoryDao extends CrudDao<Category> {
    Collection<Category> findByCategoryTypeId(Long categoryTypeId);

    Category findProductOrderAim(Long productOrderId);

    Category findProductOrderStatus(Long productOrderId);

    Category findComplainStatus(Long complainId);

    Category findComplainReason(Long complainId);

    Category findProductInstanceStatus(Long productInstanceId);

    Category findDomainType(Long domainId);
}
