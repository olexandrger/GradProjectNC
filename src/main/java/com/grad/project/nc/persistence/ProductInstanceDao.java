package com.grad.project.nc.persistence;

import com.grad.project.nc.model.ProductInstance;

import java.util.List;

public interface ProductInstanceDao extends CrudDao<ProductInstance> {
    ProductInstance findByProductOrderId(Long productOrderId);

    ProductInstance findByComplainId(Long complainId);

    List<ProductInstance> findByDomainId(Long domainId);

    List<ProductInstance> findByProductRegionPriceId(Long productRegionPriceId);
}
