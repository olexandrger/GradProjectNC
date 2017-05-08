package com.grad.project.nc.service.product;

import com.grad.project.nc.model.Product;
import com.grad.project.nc.model.ProductType;
import com.grad.project.nc.service.CrudService;

import java.util.Collection;
import java.util.List;

public interface ProductService extends CrudService<Product> {

    Collection<Product> getProductsByProductType(ProductType productType);
}
