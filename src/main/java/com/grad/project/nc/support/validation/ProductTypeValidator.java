package com.grad.project.nc.support.validation;

import com.grad.project.nc.controller.api.dto.FrontendCharacteristic;
import com.grad.project.nc.controller.api.dto.FrontendProductType;
import com.grad.project.nc.support.validation.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductTypeValidator implements Validator<FrontendProductType> {

    private static final String PRODUCT_TYPE_NAME_MES = "Product type name should contain " +
            "at least 3 characters";
    private static final String CHARACTERISTIC_NAME_MEASURE_MES ="Product characteristic " +
            "cannot have empty name or measure";

    @Override
    public void validate(FrontendProductType frontendProductType) {
        if (frontendProductType.getProductTypeName().trim().length() < 3) {
            throw new ValidationException(PRODUCT_TYPE_NAME_MES);
        }

        validateCharacteristics(frontendProductType.getProductCharacteristics());
    }

    private void validateCharacteristics(List<FrontendCharacteristic> characteristicList) {
        for (FrontendCharacteristic c : characteristicList) {
            if (c.getCharacteristicName().trim().isEmpty() || c.getMeasure().trim().isEmpty()) {
                throw new ValidationException(CHARACTERISTIC_NAME_MEASURE_MES);
            }
        }
    }
}
