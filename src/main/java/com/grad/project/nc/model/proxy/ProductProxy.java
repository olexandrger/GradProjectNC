package com.grad.project.nc.model.proxy;

import com.grad.project.nc.model.*;
import com.grad.project.nc.persistence.ProductCharacteristicDao;
import com.grad.project.nc.persistence.ProductCharacteristicValueDao;
import com.grad.project.nc.persistence.ProductRegionPriceDao;
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
    private final ProductCharacteristicDao productCharacteristicDao;
    private final ProductRegionPriceDao productRegionPriceDao;

    private Long productTypeId;

    @Autowired
    public ProductProxy(ProductCharacteristicValueDao productCharacteristicValueDao, ProductTypeDao productTypeDao,
                        ProductCharacteristicDao productCharacteristicDao, ProductRegionPriceDao productRegionPriceDao) {
        this.productCharacteristicValueDao = productCharacteristicValueDao;
        this.productTypeDao = productTypeDao;
        this.productCharacteristicDao = productCharacteristicDao;
        this.productRegionPriceDao = productRegionPriceDao;
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

    @Override
    public List<ProductCharacteristic> getProductCharacteristics() {
        if (super.getProductCharacteristics() == null) {
            super.setProductCharacteristics(productCharacteristicDao.findByProductTypeId(getProductTypeId()));
        }

        return super.getProductCharacteristics();
    }

    @Override
    public List<ProductRegionPrice> getPrices() {
        if (super.getPrices() == null) {
            super.setPrices(productRegionPriceDao.getPricesByProductId(getProductId()));
        }

        return super.getPrices();
    }
}
