package com.grad.project.nc.controller.api.dto;

import com.grad.project.nc.model.ProductCharacteristic;
import com.grad.project.nc.model.ProductType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FrontendCharacteristic {
    private Long productCharacteristicId;
    private String characteristicName;
    private String measure;
    private FrontendCategory dataType;

    public static FrontendCharacteristic fromEntity(ProductCharacteristic characteristic) {
        return FrontendCharacteristic.builder()
                .productCharacteristicId(characteristic.getProductCharacteristicId())
                .characteristicName(characteristic.getCharacteristicName())
                .measure(characteristic.getMeasure())
                .dataType(FrontendCategory.fromEntity(characteristic.getDataType()))
                .build();
    }

    public ProductCharacteristic toModel(Long productTypeId) {
        return ProductCharacteristic.builder()
                .productCharacteristicId(getProductCharacteristicId())
                .productType(new ProductType(productTypeId))
                .characteristicName(getCharacteristicName())
                .measure(getMeasure())
                .dataType(getDataType().toModel())
                .build();
    }
}
