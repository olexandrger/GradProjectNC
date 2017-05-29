package com.grad.project.nc.controller.api.dto.catalog;

import com.grad.project.nc.model.Discount;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class FrontendCatalogDiscount {
    private Long discountId;
    private String discountTitle;
    private double discount;

    public static FrontendCatalogDiscount fromEntity(Discount discount) {
        return FrontendCatalogDiscount.builder()
                .discountId(discount.getDiscountId())
                .discountTitle(discount.getDiscountTitle())
                .discount(discount.getDiscount())
                .build();
    }
}
