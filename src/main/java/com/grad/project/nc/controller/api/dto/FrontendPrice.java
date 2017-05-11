package com.grad.project.nc.controller.api.dto;

import com.grad.project.nc.model.ProductRegionPrice;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FrontendPrice {
    private Long priceId;
    private double price;
    private Long regionId;

    public static FrontendPrice fromEntity(ProductRegionPrice price) {
        return FrontendPrice.builder()
                .priceId(price.getPriceId())
                .price(price.getPrice())
                .regionId(price.getRegion().getRegionId())
                .build();
    }
}
