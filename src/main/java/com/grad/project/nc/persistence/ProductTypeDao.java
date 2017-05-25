package com.grad.project.nc.persistence;

import com.grad.project.nc.model.ProductType;

import java.util.List;

public interface ProductTypeDao extends CrudDao<ProductType> {

    List<ProductType> findByActiveStatus(boolean isActive);

    List<ProductType> findPaginated(int page, int amount);

    ProductType findByProductId(Long productId);

    List<ProductType> findByNameContaining(String productTypeName);

    List<ProductType> findLastN(int n);

    List<ProductType> findFirstN(int n);

    int countTotalProducts();
}
