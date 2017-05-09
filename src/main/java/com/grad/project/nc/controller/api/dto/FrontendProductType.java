package com.grad.project.nc.controller.api.dto;

import com.grad.project.nc.model.ProductType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FrontendProductType {
    private Long productTypeId;
    private String productTypeName;
    private String productTypeDescription;
    private Boolean isActive;

    public static FrontendProductType fromEntity(ProductType productType) {
        return FrontendProductType.builder()
                .productTypeId(productType.getProductTypeId())
                .productTypeName(productType.getProductTypeName())
                .productTypeDescription(productType.getProductTypeDescription())
                .isActive(productType.getIsActive())
                .build();
    }
}
