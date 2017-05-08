package com.grad.project.nc.persistence;

import com.grad.project.nc.model.ProductType;

public interface ProductTypeDao extends CrudDao<ProductType> {

    ProductType findByProductId(Long productId);
}
