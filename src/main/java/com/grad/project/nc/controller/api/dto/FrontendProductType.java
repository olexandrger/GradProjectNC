package com.grad.project.nc.controller.api.dto;

import com.grad.project.nc.model.ProductType;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class FrontendProductType {
    private Long productTypeId;
    private String productTypeName;
    private String productTypeDescription;
    private Boolean isActive;
    private List<FrontendCharacteristic> productCharacteristics;

    public static FrontendProductType fromEntity(ProductType productType) {
        return FrontendProductType.builder()
                .productTypeId(productType.getProductTypeId())
                .productTypeName(productType.getProductTypeName())
                .productTypeDescription(productType.getProductTypeDescription())
                .isActive(productType.getIsActive())
                .productCharacteristics(productType.getProductCharacteristics()
                        .stream()
                        .map(FrontendCharacteristic::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }

    public ProductType toModel() {
        return ProductType.builder()
                .productTypeId(getProductTypeId())
                .productTypeName(getProductTypeName())
                .productTypeDescription(getProductTypeDescription())
                .isActive(getIsActive())
                .productCharacteristics(getProductCharacteristics()
                        .stream()
                        .map(fc -> fc.toModel(getProductTypeId()))
                        .collect(Collectors.toList()))
                .build();

    }
}
