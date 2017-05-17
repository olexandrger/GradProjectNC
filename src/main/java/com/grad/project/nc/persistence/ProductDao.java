package com.grad.project.nc.persistence;

import com.grad.project.nc.model.Product;

import java.util.List;

public interface ProductDao extends CrudDao<Product> {
    List<Product> findByRegionId(Long regionId);

    List<Product> findActiveByRegionId(Long regionId);

    Product findByName(String productName);

    List<Product> findFirstN(int n);

    List<Product> findLastN(int n);

    List<Product> findByProductTypeId(Long productTypeId);

    Product findByProductRegionPriceId(Long productRegionPriceId);
}