package com.grad.project.nc.controller.api.dto;

import com.grad.project.nc.controller.api.ProductsController;
import com.grad.project.nc.model.ProductCharacteristic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
