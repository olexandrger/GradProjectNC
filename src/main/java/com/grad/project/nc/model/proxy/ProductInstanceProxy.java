package com.grad.project.nc.model.proxy;

import com.grad.project.nc.model.*;
import com.grad.project.nc.persistence.CategoryDao;
import com.grad.project.nc.persistence.DomainDao;
import com.grad.project.nc.persistence.ProductOrderDao;
import com.grad.project.nc.persistence.ProductRegionPriceDao;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
public class ProductInstanceProxy extends ProductInstance {
    @Getter @Setter
    private Long priceId;
    @Getter @Setter
    private Long domainId;
    @Getter @Setter
    private Long statusId;

    private boolean priceLoaded;
    private boolean domainLoaded;
    private boolean statusLoaded;
    private boolean productOrdersLoaded;

    private final CategoryDao categoryDao;
    private final ProductRegionPriceDao productRegionPriceDao;
    private final DomainDao domainDao;
    private final ProductOrderDao productOrderDao;

    @Autowired
    public ProductInstanceProxy(CategoryDao categoryDao, ProductRegionPriceDao productRegionPriceDao,
                                DomainDao domainDao, ProductOrderDao productOrderDao) {
        this.categoryDao = categoryDao;
        this.productRegionPriceDao = productRegionPriceDao;
        this.domainDao = domainDao;
        this.productOrderDao = productOrderDao;
    }

    @Override
    public ProductRegionPrice getPrice() {
        if (!priceLoaded) {
            this.setPrice(productRegionPriceDao.find(getPriceId()));
        }

        return super.getPrice();
    }

    @Override
    public void setPrice(ProductRegionPrice price) {
        priceLoaded = true;
        super.setPrice(price);
    }

    @Override
    public Domain getDomain() {
        if (!domainLoaded) {
            this.setDomain(domainDao.find(getDomainId()));
        }

        return super.getDomain();
    }

    @Override
    public void setDomain(Domain domain) {
        domainLoaded = true;
        super.setDomain(domain);
    }

    @Override
    public Category getStatus() {
        if (!statusLoaded) {
            this.setStatus(categoryDao.find(getStatusId()));
        }

        return super.getStatus();
    }

    @Override
    public void setStatus(Category status) {
        statusLoaded = true;
        super.setStatus(status);
    }

    @Override
    public List<ProductOrder> getProductOrders() {
        if (!productOrdersLoaded) {
            this.setProductOrders(productOrderDao.findByProductInstanceId(getInstanceId()));
        }

        return super.getProductOrders();
    }

    @Override
    public void setProductOrders(List<ProductOrder> productOrders) {
        productOrdersLoaded = true;
        super.setProductOrders(productOrders);
    }
}
