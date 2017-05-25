package com.grad.project.nc.service.product.type;

import com.grad.project.nc.model.Category;
import com.grad.project.nc.model.Product;
import com.grad.project.nc.model.ProductType;
import com.grad.project.nc.service.CrudService;
import com.grad.project.nc.support.pagination.Page;

import java.util.List;

public interface ProductTypeService extends CrudService<ProductType> {

    List<ProductType> findActive();

    List<ProductType> findLastN(int n);

    List<ProductType> findByNameContaining(String productTypeName);

    Page<ProductType> findPaginated(int page, int amount);

    List<Category> findProductCharacteristicDataTypes();
}
