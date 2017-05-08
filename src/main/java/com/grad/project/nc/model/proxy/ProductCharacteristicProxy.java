package com.grad.project.nc.model.proxy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.grad.project.nc.model.Category;
import com.grad.project.nc.model.ProductCharacteristic;
import com.grad.project.nc.model.ProductType;
import com.grad.project.nc.persistence.CategoryDao;
import com.grad.project.nc.persistence.ProductTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ProductCharacteristicProxy extends ProductCharacteristic {
    private Long productTypeId;
    private Long dataTypeId;

    private final ProductTypeDao productTypeDao;
    private final CategoryDao categoryDao;

    @Autowired
    public ProductCharacteristicProxy(ProductTypeDao productTypeDao, CategoryDao categoryDao) {
        this.productTypeDao = productTypeDao;
        this.categoryDao = categoryDao;
    }

    public Long getProductTypeId() {
        return productTypeId;
    }

    public void setProductTypeId(Long productTypeId) {
        this.productTypeId = productTypeId;
    }

    public Long getDataTypeId() {
        return dataTypeId;
    }

    public void setDataTypeId(Long dataTypeId) {
        this.dataTypeId = dataTypeId;
    }

    @Override
    @JsonIgnore
    public ProductType getProductType() {
        if (super.getProductType() == null) {
            super.setProductType(productTypeDao.find(getProductTypeId()));
        }

        return super.getProductType();
    }

    @Override
    public Category getDataType() {
        if (super.getDataType() == null) {
            super.setDataType(categoryDao.find(getDataTypeId()));
        }

        return super.getDataType();
    }
}
