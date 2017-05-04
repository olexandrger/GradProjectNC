package com.grad.project.nc.model.proxy;

import com.grad.project.nc.model.Discount;
import com.grad.project.nc.model.Product;
import com.grad.project.nc.model.ProductRegionPrice;
import com.grad.project.nc.model.Region;
import com.grad.project.nc.persistence.DiscountDao;
import com.grad.project.nc.persistence.ProductDao;
import com.grad.project.nc.persistence.RegionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
@Scope("prototype")
public class ProductRegionPriceProxy extends ProductRegionPrice {

    private DiscountDao discountDao;
    private ProductDao productDao;
    private RegionDao regionDao;

    private Long productId;
    private Long regionId;

    @Autowired
    public ProductRegionPriceProxy(DiscountDao discountDao, ProductDao productDao, RegionDao regionDao) {
        this.discountDao = discountDao;
        this.productDao = productDao;
        this.regionDao = regionDao;
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
    public Collection<Discount> getDiscounts() {
        if (super.getDiscounts() == null) {
            super.setDiscounts(discountDao.findByProductRegionPrice(getPriceId()));
        }
        return super.getDiscounts();
    }
}