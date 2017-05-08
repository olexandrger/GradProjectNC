package com.grad.project.nc.model.proxy;

import com.grad.project.nc.model.*;
import com.grad.project.nc.persistence.AddressDao;
import com.grad.project.nc.persistence.CategoryDao;
import com.grad.project.nc.persistence.ProductInstanceDao;
import com.grad.project.nc.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
public class DomainProxy extends Domain {
    private Long addressId;
    private Long domainTypeId;

    private final AddressDao addressDao;
    private final CategoryDao categoryDao;
    private final UserDao userDao;
    private final ProductInstanceDao productInstanceDao;

    @Autowired
    public DomainProxy(AddressDao addressDao, CategoryDao categoryDao,
                       UserDao userDao, ProductInstanceDao productInstanceDao) {
        this.addressDao = addressDao;
        this.categoryDao = categoryDao;
        this.userDao = userDao;
        this.productInstanceDao = productInstanceDao;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Long getDomainTypeId() {
        return domainTypeId;
    }

    public void setDomainTypeId(Long domainTypeId) {
        this.domainTypeId = domainTypeId;
    }

    @Override
    public Address getAddress() {
        if (super.getAddress() == null) {
            super.setAddress(addressDao.find(getAddressId()));
        }

        return super.getAddress();
    }

    @Override
    public Category getDomainType() {
        if (super.getDomainType() == null) {
            super.setDomainType(categoryDao.find(getDomainTypeId()));
        }

        return super.getDomainType();
    }

    @Override
    public List<User> getUsers() {
        if (super.getUsers() == null) {
            super.setUsers(userDao.findByDomainId(getDomainId()));
        }

        return super.getUsers();
    }

    @Override
    public List<ProductInstance> getProductInstances() {
        if (super.getProductInstances() == null) {
            super.setProductInstances(productInstanceDao.findByDomainId(getDomainId()));
        }

        return super.getProductInstances();
    }
}
