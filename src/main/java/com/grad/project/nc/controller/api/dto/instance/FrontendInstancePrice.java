package com.grad.project.nc.controller.api.dto.instance;

import com.grad.project.nc.controller.api.dto.FrontendRegion;
import com.grad.project.nc.controller.api.dto.catalog.FrontendCatalogDiscount;
import com.grad.project.nc.model.Product;
import com.grad.project.nc.model.ProductRegionPrice;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FrontendInstancePrice {
    private Long priceId;
    private FrontendRegion region;
    private double price;

    private FrontendCatalogDiscount discount;

    public static FrontendInstancePrice fromEntity(ProductRegionPrice price) {




        return FrontendInstancePrice.builder()
                .priceId(price.getPriceId())
                .region(FrontendRegion.fromEntity(price.getRegion()))
                .price(price.getPrice())
                .build();
    }

    public ProductRegionPrice toModel(Long productId) {
        return ProductRegionPrice.builder()
                .priceId(getPriceId())
                .product(new Product(productId))
                .region(getRegion().toModel())
                .price(getPrice())
                .build();
    }

}
