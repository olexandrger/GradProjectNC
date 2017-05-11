package com.grad.project.nc.service.product.type;

import com.grad.project.nc.model.Category;
import com.grad.project.nc.model.ProductType;
import com.grad.project.nc.service.CrudService;

import java.util.List;

public interface ProductTypeService extends CrudService<ProductType> {

    List<Category> findProductCharacteristicDataTypes();
}
