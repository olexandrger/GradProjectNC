package com.grad.project.nc.model;

/**
 * Created by Alex on 4/24/2017.
 */
public class ProductCharacteristicValue {
    private int productId;
    private int productCharacteristicId;
    private int valueId;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getProductCharacteristicId() {
        return productCharacteristicId;
    }

    public void setProductCharacteristicId(int productCharacteristicId) {
        this.productCharacteristicId = productCharacteristicId;
    }

    public int getValueId() {
        return valueId;
    }

    public void setValueId(int valueId) {
        this.valueId = valueId;
    }
}
