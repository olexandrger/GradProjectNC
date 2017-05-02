package com.grad.project.nc.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by Alex on 4/24/2017.
 */
@Data
@NoArgsConstructor
public class Product {
    private Long productId;
    private String name;
    private String description;
    private Category status;

    private ProductType productType;
    private List<ProductCharacteristicValue> productCharacteristicValues;
    private List<ProductCharacteristic> productCharacteristics;


}
