package com.grad.project.nc.service.product;

import com.grad.project.nc.model.Product;
import com.grad.project.nc.service.CrudService;
import com.grad.project.nc.support.pagination.Page;

import java.util.List;

public interface ProductService extends CrudService<Product> {

    Product findCatalogProduct(Long id);

    Page<Product> findPaginated(int page, int amount);

    Page<Product> findActiveByProductTypeIdAndRegionIdPaginated(Long productTypeId, Long regionId,
                                                                int page, int amount);

    List<Product> findLastN(int n);

    List<Product> findByNameContaining(String productName);

    List<Product> findByNameContaining(String productName, Long productTypeId, Long regionId);

    List<Product> findByProductTypeId(Long productTypeId);

    List<Product> findCatalogProductsByRegionId(Long regionId);
}
