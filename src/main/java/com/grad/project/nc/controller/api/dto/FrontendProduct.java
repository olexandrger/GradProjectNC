package com.grad.project.nc.controller.api.dto;

import com.grad.project.nc.model.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class FrontendProduct {
    private Long productId;
    private String productName;
    private String productDescription;
    private Boolean isActive;
    private Long productTypeId;

    private List<FrontendCharacteristicValue> productCharacteristicValues;
    private List<FrontendPrice> prices;

    public static FrontendProduct fromEntity(Product product) {
        return FrontendProduct.builder()
                .productId(product.getProductId())
                .productName(product.getProductName())
                .productDescription(product.getProductDescription())
                .isActive(product.getIsActive())
                .productTypeId(product.getProductType().getProductTypeId())
                .productCharacteristicValues(product.getProductCharacteristicValues()
                        .stream()
                        .map(FrontendCharacteristicValue::fromEntity)
                        .collect(Collectors.toList()))
                .prices(product.getPrices()
                        .stream()
                        .map(FrontendPrice::fromEntity)
                        .collect(Collectors.toList()))
                .build();

    }

    public Product toModel() {
        return Product.builder()
                .productId(getProductId())
                .productName(getProductName())
                .productDescription(getProductDescription())
                .isActive(getIsActive())
                .productType(ProductType.builder().productTypeId(getProductTypeId()).build())
                .productCharacteristicValues(getProductCharacteristicValues()
                        .stream()
                        .map(fv -> ProductCharacteristicValue.builder()
                                .valueId(fv.getValueId())
                                .product(new Product(getProductId()))
                                .productCharacteristic(new ProductCharacteristic(fv.getProductCharacteristicId()))
                                .stringValue(fv.getStringValue())
                                .numberValue(fv.getNumberValue())
                                .dateValue(fv.getDateValue())
                                .build())
                        .collect(Collectors.toList()))
                .prices(getPrices()
                        .stream()
                        .map(fp -> ProductRegionPrice.builder()
                                .priceId(fp.getPriceId())
                                .product(new Product(getProductId()))
                                .region(new Region(fp.getRegionId()))
                                .price(fp.getPrice())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}