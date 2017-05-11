package com.grad.project.nc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

/**
 * Created by Alex on 4/24/2017.
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductCharacteristicValue {
    private Long valueId;
    private Product product;
    private ProductCharacteristic productCharacteristic;
    private Number numberValue;
    private OffsetDateTime dateValue;
    private String stringValue;
}
