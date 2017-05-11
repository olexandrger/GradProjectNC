package com.grad.project.nc.controller.api.dto;

import com.grad.project.nc.model.ProductCharacteristic;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FrontendCharacteristic {
    private Long productCharacteristicId;
    private String characteristicName;
    private String measure;
    private Long dataTypeId;

    public static FrontendCharacteristic fromEntity(ProductCharacteristic characteristic) {
        return FrontendCharacteristic.builder()
                .productCharacteristicId(characteristic.getProductCharacteristicId())
                .characteristicName(characteristic.getCharacteristicName())
                .measure(characteristic.getMeasure())
                .dataTypeId(characteristic.getDataType().getCategoryId())
                .build();
    }
}
