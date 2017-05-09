package com.grad.project.nc.controller.api.dto;

import com.grad.project.nc.model.ProductCharacteristicValue;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class FrontendCharacteristicValue {
    private FrontendCharacteristic productCharacteristic;
    private Number numberValue;
    private OffsetDateTime dateValue;
    private String stringValue;

    public static FrontendCharacteristicValue fromEntity(ProductCharacteristicValue value) {
        return FrontendCharacteristicValue.builder()
                .productCharacteristic(FrontendCharacteristic.fromEntity(value.getProductCharacteristic()))
                .dateValue(value.getDateValue())
                .stringValue(value.getStringValue())
                .numberValue(value.getNumberValue())
                .build();
    }
}
