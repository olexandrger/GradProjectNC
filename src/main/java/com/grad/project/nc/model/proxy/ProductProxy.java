package com.grad.project.nc.model.proxy;

import com.grad.project.nc.model.Product;
import com.grad.project.nc.model.ProductCharacteristicValue;
import com.grad.project.nc.model.ProductType;
import com.grad.project.nc.persistence.ProductCharacteristicValueDao;
import com.grad.project.nc.persistence.ProductTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
public class ProductProxy extends Product {

    private final ProductTypeDao productTypeDao;
    private final ProductCharacteristicValueDao productCharacteristicValueDao;

    private Long productTypeId;

    @Autowired
    public ProductProxy(ProductCharacteristicValueDao productCharacteristicValueDao, ProductTypeDao productTypeDao) {
        this.productCharacteristicValueDao = productCharacteristicValueDao;
        this.productTypeDao = productTypeDao;
    }

    public Long getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(Long productTypeId) {
        this.productTypeId = productTypeId;
    }

    @Override
    public ProductType getProductType() {
        if (super.getProductType() == null) {
            super.setProductType(productTypeDao.find(productTypeId));
        }
        return super.getProductType();
    }

    @Override
    public List<ProductCharacteristicValue> getProductCharacteristicValues() {
        if (super.getProductCharacteristicValues() == null) {
            super.setProductCharacteristicValues(productCharacteristicValueDao.findByProductId(getProductId()));
        }

        return super.getProductCharacteristicValues();
    }
}
