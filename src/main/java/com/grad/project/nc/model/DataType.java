package com.grad.project.nc.model;

import lombok.Data;

import java.util.List;

/**
 * Created by Alex on 4/24/2017.
 */
@Data
public class DataType {
    private Long dataTypeId;
    private String dataType;

    private List<ProductCharacteristic> productCharacteristics;


    public Long getDataTypeId() {
        return dataTypeId;
    }

    public void setDataTypeId(Long dataTypeId) {
        this.dataTypeId = dataTypeId;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}
