package com.grad.project.nc.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductCharacteristic {
    private Long productCharacteristicId;
    private Long productTypeId;
    private String characteristicName;
    private String measure;
//    private Long dataTypeId;
    private DataType dataType;
}
