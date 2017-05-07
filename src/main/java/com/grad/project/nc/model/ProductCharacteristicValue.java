package com.grad.project.nc.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

/**
 * Created by Alex on 4/24/2017.
 */

@Data
@NoArgsConstructor
public class ProductCharacteristicValue {
    private Product product;
    private ProductCharacteristic productCharacteristic;
    private Number numberValue;
    private OffsetDateTime dateValue;
    private String stringValue;

    public ProductCharacteristicValue(ProductCharacteristic productCharacteristic, String stringValue) {
        this.productCharacteristic = productCharacteristic;
        this.stringValue = stringValue;
    }
}
