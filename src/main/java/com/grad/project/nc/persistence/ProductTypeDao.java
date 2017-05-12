package com.grad.project.nc.persistence;

import com.grad.project.nc.model.ProductType;

import java.util.List;

public interface ProductTypeDao extends CrudDao<ProductType> {

    ProductType findByProductId(Long productId);

    List<ProductType> findLastN(int n);

    List<ProductType> findFirstN(int n);


}
