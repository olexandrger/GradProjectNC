package com.grad.project.nc.model;

/**
 * Created by Alex on 4/24/2017.
 */
public class ProductCharacteristic {
    private int productCharacteristicId;
    private int productTypeId;
    private String characteristicName;
    private String measure;
    private int dataTypeId;

    public int getProductCharacteristicId() {
        return productCharacteristicId;
    }

    public void setProductCharacteristicId(int productCharacteristicId) {
        this.productCharacteristicId = productCharacteristicId;
    }

    public int getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(int productTypeId) {
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

    public int getDataTypeId() {
        return dataTypeId;
    }

    public void setDataTypeId(int dataTypeId) {
        this.dataTypeId = dataTypeId;
    }
}
