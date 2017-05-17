package com.grad.project.nc.controller.api.dto.catalog;

import com.grad.project.nc.controller.api.dto.FrontendCharacteristic;
import com.grad.project.nc.model.ProductCharacteristicValue;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@Builder
public class FrontendCatalogCharacteristicValue {
    private Long valueId;
    private FrontendCharacteristic productCharacteristic;
    private Number numberValue;
    private OffsetDateTime dateValue;
    private String stringValue;

    public static FrontendCatalogCharacteristicValue fromEntity(ProductCharacteristicValue value) {
        return FrontendCatalogCharacteristicValue.builder()
                .valueId(value.getValueId())
                .productCharacteristic(FrontendCharacteristic.fromEntity(value.getProductCharacteristic()))
                .dateValue(value.getDateValue())
                .stringValue(value.getStringValue())
                .numberValue(value.getNumberValue())
                .build();
    }
}
