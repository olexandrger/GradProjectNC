package com.grad.project.nc.persistence;

import com.grad.project.nc.model.ProductRegionPrice;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ProductRegionPriceDao extends CrudDao<ProductRegionPrice> {

    List<ProductRegionPrice> findByDiscountId(Long discountId);

    void deleteDiscounts(Long productRegionPriceId);

    @Transactional
    void persistDiscounts(ProductRegionPrice productRegionPrice);

    @Transactional
    void persistBatch(Long productId, List<ProductRegionPrice> prices);

    void deleteByProductId(Long productId);

    List<ProductRegionPrice> findByProductId(Long productId);

    List<ProductRegionPrice> findByRegionId(Long regionId);
}
