package com.grad.project.nc.model.proxy;

import com.grad.project.nc.model.*;
import com.grad.project.nc.persistence.DiscountDao;
import com.grad.project.nc.persistence.ProductDao;
import com.grad.project.nc.persistence.ProductInstanceDao;
import com.grad.project.nc.persistence.RegionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
public class ProductRegionPriceProxy extends ProductRegionPrice {

    private final DiscountDao discountDao;
    private final ProductDao productDao;
    private final RegionDao regionDao;
    private final ProductInstanceDao productInstanceDao;

    private Long productId;
    private Long regionId;

    @Autowired
    public ProductRegionPriceProxy(DiscountDao discountDao, ProductDao productDao,
                                   RegionDao regionDao, ProductInstanceDao productInstanceDao) {
        this.discountDao = discountDao;
        this.productDao = productDao;
        this.regionDao = regionDao;
        this.productInstanceDao = productInstanceDao;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getRegionId() {
        return regionId;
    }

    public void setRegionId(Long regionId) {
        this.regionId = regionId;
    }

    @Override
    public Product getProduct() {
        if (super.getProduct() == null) {
            super.setProduct(productDao.find(getProductId()));
        }

        return super.getProduct();
    }

    @Override
    public Region getRegion() {
        if (super.getRegion() == null) {
            super.setRegion(regionDao.find(getRegionId()));
        }

        return super.getRegion();
    }

    @Override
    public List<Discount> getDiscounts() {
        if (super.getDiscounts() == null) {
            super.setDiscounts(discountDao.findByProductRegionPriceId(getPriceId()));
        }

        return super.getDiscounts();
    }

    @Override
    public List<ProductInstance> getProductInstances() {
        if (super.getProductInstances() == null) {
            super.setProductInstances(productInstanceDao.findByProductRegionPriceId(getPriceId()));
        }

        return super.getProductInstances();
    }
}