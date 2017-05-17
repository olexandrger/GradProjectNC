package com.grad.project.nc.controller.api.dto;

import com.grad.project.nc.model.Product;
import com.grad.project.nc.model.ProductRegionPrice;
import com.grad.project.nc.model.Region;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FrontendPrice {
    private Long priceId;
    private Long regionId;
    private double price;

    public static FrontendPrice fromEntity(ProductRegionPrice price) {
        return FrontendPrice.builder()
                .priceId(price.getPriceId())
                .regionId(price.getRegion().getRegionId())
                .price(price.getPrice())
                .build();
    }

    public ProductRegionPrice toModel(Long productId) {
        return ProductRegionPrice.builder()
                .priceId(getPriceId())
                .product(new Product(productId))
                .region(new Region(getRegionId()))
                .price(getPrice())
                .build();
    }
}
