package com.grad.project.nc.support.validation;

import com.grad.project.nc.controller.api.dto.FrontendCharacteristicValue;
import com.grad.project.nc.controller.api.dto.FrontendPrice;
import com.grad.project.nc.controller.api.dto.FrontendProduct;
import com.grad.project.nc.support.validation.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductValidator implements Validator<FrontendProduct> {

    private static final String PRODUCT_NAME_MES = "Product name should contain " +
            "at least 3 characters";
    private static final String PRODUCT_PRICE_MES = "Product regional price value should be " +
            "greater than zero";
    private static final String PRODUCT_CHARACTERISTIC_EMPTY_VALUE_MES = "Product characteristic " +
            "values cannot be empty. Values of number type can hold only digits";

    @Override
    public void validate(FrontendProduct frontendProduct) {
        if (frontendProduct.getProductName().trim().length() < 3) {
            throw new ValidationException(PRODUCT_NAME_MES);
        }

        validatePrices(frontendProduct.getPrices());
        validateProductCharacteristicValues(frontendProduct.getProductCharacteristicValues());
    }

    private void validatePrices(List<FrontendPrice> prices) {
        for (FrontendPrice price : prices) {
            if (price.getPrice() <= 0) {
                throw new ValidationException(PRODUCT_PRICE_MES);
            }
        }
    }

    private void validateProductCharacteristicValues(List<FrontendCharacteristicValue> values) {
        for (FrontendCharacteristicValue value : values) {
            if (value.getDateValue() == null
                    && (value.getNumberValue() == null || value.getNumberValue().doubleValue() == 0)
                    && (value.getStringValue() == null || value.getStringValue().trim().isEmpty())) {
                throw new ValidationException(PRODUCT_CHARACTERISTIC_EMPTY_VALUE_MES);
            }
        }
    }
}
