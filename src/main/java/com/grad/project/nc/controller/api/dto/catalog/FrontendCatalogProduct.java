package com.grad.project.nc.controller.api.dto.catalog;

import com.grad.project.nc.model.Product;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;


@Data
@Builder
public class FrontendCatalogProduct {
    private Long productId;
    private String productName;
    private String productDescription;
    private Boolean isActive;
    private Long productTypeId;

    private List<FrontendCatalogCharacteristicValue> productCharacteristicValues;
    private List<FrontendCatalogPrice> prices;

    public static FrontendCatalogProduct fromEntity(Product product) {
        return FrontendCatalogProduct.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .productDescription(product.getProductDescription())
                .isActive(product.getIsActive())
                .productTypeId(product.getProductType().getProductTypeId())
                .productCharacteristicValues(product.getProductCharacteristicValues()
                        .stream()
                        .map(FrontendCatalogCharacteristicValue::fromEntity)
                        .collect(Collectors.toList()))
                .prices(product.getPrices()
                        .stream()
                        .map(FrontendCatalogPrice::fromEntity)
                        .collect(Collectors.toList()))
                .build();

    }
}
