package com.grad.project.nc.controller.api.dto;

import com.grad.project.nc.model.Discount;
import com.grad.project.nc.model.ProductRegionPrice;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Alex on 5/16/2017.
 */
@Data
@Builder
public class FrontendDiscount {
    private Long discountId;
    private String discountTitle;
    private double discount;
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private List<FrontEndProductRegionPrice> productRegionPrices;


    public static FrontendDiscount fromEntity(Discount discount){
        return builder().discountId(discount.getDiscountId())
                .discountTitle(discount.getDiscountTitle())
                .discount(discount.getDiscount())
                .startDate(discount.getStartDate())
                .endDate(discount.getEndDate()).productRegionPrices(discount.getProductRegionPrices().stream()
                        .map(FrontEndProductRegionPrice::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}
