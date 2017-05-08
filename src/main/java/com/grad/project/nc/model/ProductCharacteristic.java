package com.grad.project.nc.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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
