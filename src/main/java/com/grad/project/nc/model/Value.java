package com.grad.project.nc.model;

import java.util.Calendar;

/**
 * Created by Alex on 4/24/2017.
 */
public class Value {
    private int valueId;
    private int productCharacteristicId;
    private int numberValue;
    private Calendar dateValue;
    private String stringValue;

    public int getValueId() {
        return valueId;
    }

    public void setValueId(int valueId) {
        this.valueId = valueId;
    }

    public int getProductCharacteristicId() {
        return productCharacteristicId;
    }

    public void setProductCharacteristicId(int productCharacteristicId) {
        this.productCharacteristicId = productCharacteristicId;
    }

    public int getNumberValue() {
        return numberValue;
    }

    public void setNumberValue(int numberValue) {
        this.numberValue = numberValue;
    }

    public Calendar getDateValue() {
        return dateValue;
    }

    public void setDateValue(Calendar dateValue) {
        this.dateValue = dateValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }
}
