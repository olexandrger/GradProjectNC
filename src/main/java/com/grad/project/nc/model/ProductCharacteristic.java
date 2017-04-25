package com.grad.project.nc.model;

/**
 * Created by Alex on 4/24/2017.
 */
public class ProductCharacteristic {
    private Long productCharacteristicId;
    private Long productTypeId;
    private String characteristicName;
    private String measure;
    private Long dataTypeId;

    public Long getProductCharacteristicId() {
        return productCharacteristicId;
    }

    public void setProductCharacteristicId(Long productCharacteristicId) {
        this.productCharacteristicId = productCharacteristicId;
    }

    public Long getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(Long productTypeId) {
        this.productTypeId = productTypeId;
    }

    public String getCharacteristicName() {
        return characteristicName;
    }

    public void setCharacteristicName(String characteristicName) {
        this.characteristicName = characteristicName;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public Long getDataTypeId() {
        return dataTypeId;
    }

    public void setDataTypeId(Long dataTypeId) {
        this.dataTypeId = dataTypeId;
    }
}
