package com.grad.project.nc.controller.api.dto.catalog;

import com.grad.project.nc.model.Discount;
import com.grad.project.nc.model.Product;
import com.grad.project.nc.model.ProductRegionPrice;
import com.grad.project.nc.model.Region;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FrontendCatalogPrice {
    private Long priceId;
    private Long regionId;
    private double price;

    private FrontendCatalogDiscount discount;

    public static FrontendCatalogPrice fromEntity(ProductRegionPrice price) {
        FrontendCatalogDiscount discount = price.getDiscounts().get(0) != null ?
                FrontendCatalogDiscount.fromEntity(price.getDiscounts().get(0)) : null;

        return FrontendCatalogPrice.builder()
                .priceId(price.getPriceId())
                .regionId(price.getRegion().getRegionId())
                .price(price.getPrice())
                .discount(discount)
                .build();
    }

//    public ProductRegionPrice toModel(Long productId) {
//        return ProductRegionPrice.builder()
//                .priceId(getPriceId())
//                .product(new Product(productId))
//                .region(new Region(getRegionId()))
//                .price(getPrice())
//                .build();
//    }
}
