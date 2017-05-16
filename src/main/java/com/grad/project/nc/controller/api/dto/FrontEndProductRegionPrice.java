package com.grad.project.nc.controller.api.dto;

import com.grad.project.nc.model.ProductRegionPrice;
import lombok.Builder;
import lombok.Data;

/**
 * Created by Alex on 5/16/2017.
 */
@Data
@Builder
public class FrontEndProductRegionPrice {
    private Long priceId;
    private FrontendProduct product;
    private FrontendRegion region;
    private double price;

    public static FrontEndProductRegionPrice fromEntity(ProductRegionPrice productRegionPrice){
        return builder().priceId(productRegionPrice.getPriceId())
                .product(FrontendProduct.fromEntity(productRegionPrice.getProduct()))
                .region(FrontendRegion.fromEntity(productRegionPrice.getRegion()))
                .price(productRegionPrice.getPrice())
                .build();
    }
}
