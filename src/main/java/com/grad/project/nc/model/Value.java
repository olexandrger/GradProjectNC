package com.grad.project.nc.model;

import java.time.LocalDateTime;


/**
 * Created by Alex on 4/24/2017.
 */
public class Value {
    private Long valueId;
    private Long productCharacteristicId;
    private Long numberValue;
    private LocalDateTime dateValue;
    private String stringValue;

    public Long getValueId() {
        return valueId;
    }

    public void setValueId(Long valueId) {
        this.valueId = valueId;
    }

    public Long getProductCharacteristicId() {
        return productCharacteristicId;
    }

    public void setProductCharacteristicId(Long productCharacteristicId) {
        this.productCharacteristicId = productCharacteristicId;
    }

    public Long getNumberValue() {
        return numberValue;
    }

    public void setNumberValue(Long numberValue) {
        this.numberValue = numberValue;
    }

    public LocalDateTime getDateValue() {
        return dateValue;
    }

    public void setDateValue(LocalDateTime dateValue) {
        this.dateValue = dateValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }
}
