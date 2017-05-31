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
            "at least 3 characters and cannot be longer than 60 characters";
    private static final String PRODUCT_TYPE_MES = "Product should belong to specific product type";
    private static final String PRODUCT_PRICE_MES = "Product regional price must be a number greater than zero";
    private static final String PRODUCT_PRICE_REGION_MES = "Product price must be specified " +
            "for a particular region";
    private static final String PRODUCT_CHARACTERISTIC_VALUE_MES = "Product characteristic " +
            "value cannot be empty";

    @Override
    public void validate(FrontendProduct frontendProduct) {
        if (frontendProduct.getProductName().trim().length() < 3
                || frontendProduct.getProductName().length() > 60) {
            throw new ValidationException(PRODUCT_NAME_MES);
        } else if (frontendProduct.getProductTypeId() == null) {
            throw new ValidationException(PRODUCT_TYPE_MES);
        }

        validatePrices(frontendProduct.getPrices());
        validateProductCharacteristicValues(frontendProduct.getProductCharacteristicValues());
    }

    private void validatePrices(List<FrontendPrice> prices) {
        prices.forEach(p -> {
            if (p.getPrice() <= 0) {
                throw new ValidationException(PRODUCT_PRICE_MES);
            } else if (p.getRegionId() == null) {
                throw new ValidationException(PRODUCT_PRICE_REGION_MES);
            }
        });
    }

    private void validateProductCharacteristicValues(List<FrontendCharacteristicValue> values) {
        values.forEach(v -> {
            if (v.getDateValue() == null
                    && v.getNumberValue() == null
                    && (v.getStringValue() == null || v.getStringValue().trim().isEmpty())) {
                throw new ValidationException(PRODUCT_CHARACTERISTIC_VALUE_MES);
            }
        });
    }
}
