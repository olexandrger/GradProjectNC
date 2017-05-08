package com.grad.project.nc.model.proxy;

import com.grad.project.nc.model.Category;
import com.grad.project.nc.model.Domain;
import com.grad.project.nc.model.ProductInstance;
import com.grad.project.nc.model.ProductRegionPrice;
import com.grad.project.nc.persistence.CategoryDao;
import com.grad.project.nc.persistence.DomainDao;
import com.grad.project.nc.persistence.ProductRegionPriceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ProductInstanceProxy extends ProductInstance {
    private Long priceId;
    private Long domainId;
    private Long statusId;

    private final CategoryDao categoryDao;
    private final ProductRegionPriceDao productRegionPriceDao;
    private final DomainDao domainDao;

    @Autowired
    public ProductInstanceProxy(CategoryDao categoryDao, ProductRegionPriceDao productRegionPriceDao, DomainDao domainDao) {
        this.categoryDao = categoryDao;
        this.productRegionPriceDao = productRegionPriceDao;
        this.domainDao = domainDao;
    }

    public Long getPriceId() {
        return priceId;
    }

    public void setPriceId(Long priceId) {
        this.priceId = priceId;
    }

    public Long getDomainId() {
        return domainId;
    }

    public void setDomainId(Long domainId) {
        this.domainId = domainId;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    @Override
    public ProductRegionPrice getPrice() {
        if (super.getPrice() == null) {
            super.setPrice(productRegionPriceDao.find(getPriceId()));
        }

        return super.getPrice();
    }

    @Override
    public Domain getDomain() {
        if (super.getDomain() == null) {
            super.setDomain(domainDao.find(getDomainId()));
        }

        return super.getDomain();
    }

    @Override
    public Category getStatus() {
        if (super.getStatus() == null) {
            super.setStatus(categoryDao.find(getStatusId()));
        }

        return super.getStatus();
    }
}
