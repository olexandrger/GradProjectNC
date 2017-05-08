package com.grad.project.nc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCharacteristic {
    private Long productCharacteristicId;
    private ProductType productType;
    private String characteristicName;
    private String measure;
    private Category dataType;

    public ProductCharacteristic(Long productCharacteristicId) {
        this.productCharacteristicId = productCharacteristicId;
    }
}
