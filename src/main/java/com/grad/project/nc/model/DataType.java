package com.grad.project.nc.model;

import lombok.Data;

import java.util.List;


@Data
public class DataType {
    private Long dataTypeId;
    private String dataType;

    private List<ProductCharacteristic> productCharacteristics;
}
