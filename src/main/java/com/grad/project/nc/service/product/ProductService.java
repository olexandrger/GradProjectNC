package com.grad.project.nc.service.product;

import com.grad.project.nc.model.Product;
import com.grad.project.nc.service.CrudService;
import com.grad.project.nc.support.pagination.Page;

import java.util.List;

public interface ProductService extends CrudService<Product> {

    Page<Product> findPaginated(int page, int amount);

    List<Product> findLastN(int n);

    List<Product> findByProductTypeId(Long productTypeId);

    List<Product> findActiveProductsByRegionId(Long regionId);
}
