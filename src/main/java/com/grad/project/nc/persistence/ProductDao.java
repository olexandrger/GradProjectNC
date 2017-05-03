package com.grad.project.nc.persistence;

import com.grad.project.nc.model.Product;
import com.grad.project.nc.model.ProductRegionPrice;
import com.grad.project.nc.model.ProductType;
import com.grad.project.nc.model.Region;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductDao extends CrudDao<Product> {
    void deleteProductCharacteristicValues(Product product);

    @Transactional
    void saveProductCharacteristicValues(Product product);

    List<Product> findProductsByRegion(Region region);

    Product findByName(String productName);

    List<Product> findProductsByType(ProductType productType);

    Product getProductByProductRegionPrice(ProductRegionPrice productRegionPrice);
}