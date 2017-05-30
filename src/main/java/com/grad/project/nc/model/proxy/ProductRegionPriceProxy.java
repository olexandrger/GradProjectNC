package com.grad.project.nc.model.proxy;

import com.grad.project.nc.model.*;
import com.grad.project.nc.persistence.DiscountDao;
import com.grad.project.nc.persistence.ProductDao;
import com.grad.project.nc.persistence.ProductInstanceDao;
import com.grad.project.nc.persistence.RegionDao;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Scope("prototype")
public class ProductRegionPriceProxy extends ProductRegionPrice {

    private final DiscountDao discountDao;
    private final ProductDao productDao;
    private final RegionDao regionDao;
    private final ProductInstanceDao productInstanceDao;

    @Getter @Setter
    private Long productId;
    @Getter @Setter
    private Long regionId;

    private boolean productLoaded;
    private boolean regionLoaded;
    private boolean discountsLoaded;
    private boolean productInstancesLoaded;

    @Autowired
    public ProductRegionPriceProxy(DiscountDao discountDao, ProductDao productDao,
                                   RegionDao regionDao, ProductInstanceDao productInstanceDao) {
        this.discountDao = discountDao;
        this.productDao = productDao;
        this.regionDao = regionDao;
        this.productInstanceDao = productInstanceDao;
    }

    @Override
    public Product getProduct() {
        if (!productLoaded) {
            this.setProduct(productDao.find(getProductId()));
        }

        return super.getProduct();
    }

    @Override
    public void setProduct(Product product) {
        productLoaded = true;
        super.setProduct(product);
    }

    @Override
    public Region getRegion() {
        if (!regionLoaded) {
            this.setRegion(regionDao.find(getRegionId()));
        }

        return super.getRegion();
    }

    @Override
    public void setRegion(Region region) {
        regionLoaded = true;
        super.setRegion(region);
    }

    @Override
    public List<Discount> getDiscounts() {
        if (!discountsLoaded) {
            this.setDiscounts(discountDao.findByProductRegionPriceId(getPriceId()));
        }

        return super.getDiscounts();
    }

    @Override
    public void setDiscounts(List<Discount> discounts) {
        discountsLoaded = true;
        super.setDiscounts(discounts);
    }

    @Override
    public List<ProductInstance> getProductInstances() {
        if (!productInstancesLoaded) {
            this.setProductInstances(productInstanceDao.findByProductRegionPriceId(getPriceId()));
        }

        return super.getProductInstances();
    }

    @Override
    public void setProductInstances(List<ProductInstance> productInstances) {
        productInstancesLoaded = true;
        super.setProductInstances(productInstances);
    }
}