package com.grad.project.nc.persistence;

import com.grad.project.nc.model.Discount;
import com.grad.project.nc.model.Product;
import com.grad.project.nc.model.ProductRegionPrice;

import java.util.List;

public interface ProductRegionPriceDao extends CrudDao<ProductRegionPrice> {
    List<ProductRegionPrice> getProductRegionPricesByDiscount(Discount discount);

    void deleteAllDiscounts(ProductRegionPrice productRegionPrice);

    void saveAllDiscounts(ProductRegionPrice productRegionPrice);

    void addBatch(List<ProductRegionPrice> prices);

    List<ProductRegionPrice> getPricesByProduct(Product product);
}
