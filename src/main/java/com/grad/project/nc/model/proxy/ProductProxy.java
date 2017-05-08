package com.grad.project.nc.model.proxy;

import com.grad.project.nc.model.*;
import com.grad.project.nc.persistence.ProductCharacteristicDao;
import com.grad.project.nc.persistence.ProductCharacteristicValueDao;
import com.grad.project.nc.persistence.ProductRegionPriceDao;
import com.grad.project.nc.persistence.ProductTypeDao;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
public class ProductProxy extends Product {

    @Getter @Setter
    private Long productTypeId;

    private boolean productTypeLoaded;
    private boolean productCharacteristicValuesLoaded;
    private boolean productCharacteristicsLoaded;
    private boolean pricesLoaded;

    private final ProductTypeDao productTypeDao;
    private final ProductCharacteristicValueDao productCharacteristicValueDao;
    private final ProductCharacteristicDao productCharacteristicDao;
    private final ProductRegionPriceDao productRegionPriceDao;

    @Autowired
    public ProductProxy(ProductCharacteristicValueDao productCharacteristicValueDao, ProductTypeDao productTypeDao,
                        ProductCharacteristicDao productCharacteristicDao, ProductRegionPriceDao productRegionPriceDao) {
        this.productCharacteristicValueDao = productCharacteristicValueDao;
        this.productTypeDao = productTypeDao;
        this.productCharacteristicDao = productCharacteristicDao;
        this.productRegionPriceDao = productRegionPriceDao;
    }

    @Override
    public ProductType getProductType() {
        if (!productTypeLoaded) {
            this.setProductType(productTypeDao.find(productTypeId));
        }
        return super.getProductType();
    }

    @Override
    public void setProductType(ProductType productType) {
        productTypeLoaded = true;
        super.setProductType(productType);
    }

    @Override
    public List<ProductCharacteristicValue> getProductCharacteristicValues() {
        if (!productCharacteristicValuesLoaded) {
            this.setProductCharacteristicValues(productCharacteristicValueDao.findByProductId(getProductId()));
        }

        return super.getProductCharacteristicValues();
    }

    @Override
    public void setProductCharacteristicValues(List<ProductCharacteristicValue> productCharacteristicValues) {
        productCharacteristicValuesLoaded = true;
        super.setProductCharacteristicValues(productCharacteristicValues);
    }

    @Override
    public List<ProductCharacteristic> getProductCharacteristics() {
        if (!productCharacteristicsLoaded) {
            this.setProductCharacteristics(productCharacteristicDao.findByProductTypeId(getProductTypeId()));
        }

        return super.getProductCharacteristics();
    }

    @Override
    public void setProductCharacteristics(List<ProductCharacteristic> productCharacteristics) {
        productCharacteristicsLoaded = true;
        super.setProductCharacteristics(productCharacteristics);
    }

    @Override
    public List<ProductRegionPrice> getPrices() {
        if (!pricesLoaded) {
            this.setPrices(productRegionPriceDao.findByProductId(getProductId()));
        }

        return super.getPrices();
    }

    @Override
    public void setPrices(List<ProductRegionPrice> prices) {
        pricesLoaded = true;
        super.setPrices(prices);
    }
}
