package com.grad.project.nc.persistence;

import com.grad.project.nc.model.ProductRegionPrice;

import java.util.List;

public interface ProductRegionPriceDao extends CrudDao<ProductRegionPrice> {

    List<ProductRegionPrice> findByDiscountId(Long discountId);

    void deleteDiscounts(Long productRegionPriceId);

    void persistDiscounts(ProductRegionPrice productRegionPrice);

    void persistBatch(List<ProductRegionPrice> prices);

    void updateBatch(List<ProductRegionPrice> prices);

    void deleteBatch(List<ProductRegionPrice> prices);

    void deleteByProductId(Long productId);

    List<ProductRegionPrice> findByProductId(Long productId);

    List<ProductRegionPrice> findByRegionId(Long regionId);
    List<ProductRegionPrice> findAllWithoutActiveDiscountForRegion(Long regionId);

    ProductRegionPrice find(Long regionId, Long priceId);
}
