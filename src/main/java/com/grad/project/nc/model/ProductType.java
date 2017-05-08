package com.grad.project.nc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by Alex on 4/24/2017.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductType {
    private Long productTypeId;
    private String productTypeName;
    private String productTypeDescription;
    private Boolean isActive;

    private List<ProductCharacteristic> productCharacteristics;

    public ProductType(Long productTypeId) {
        this.productTypeId = productTypeId;
    }
}
