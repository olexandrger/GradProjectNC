package com.grad.project.nc.model;

/**
 * Created by Alex on 4/24/2017.
 */
public class ProductCharacteristicValue {
    private Long productId;
    private Long productCharacteristicId;
    private Long valueId;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getProductCharacteristicId() {
        return productCharacteristicId;
    }

    public void setProductCharacteristicId(Long productCharacteristicId) {
        this.productCharacteristicId = productCharacteristicId;
    }

    public Long getValueId() {
        return valueId;
    }

    public void setValueId(Long valueId) {
        this.valueId = valueId;
    }
}
