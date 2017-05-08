package com.grad.project.nc.model.proxy;

import com.grad.project.nc.model.*;
import com.grad.project.nc.persistence.AddressDao;
import com.grad.project.nc.persistence.CategoryDao;
import com.grad.project.nc.persistence.ProductInstanceDao;
import com.grad.project.nc.persistence.UserDao;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
public class DomainProxy extends Domain {
    @Getter @Setter
    private Long addressId;
    @Getter @Setter
    private Long domainTypeId;

    private boolean addressLoaded;
    private boolean domainTypeLoaded;
    private boolean usersLoaded;
    private boolean productInstancesLoaded;

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

    @Override
    public Address getAddress() {
        if (!addressLoaded) {
            this.setAddress(addressDao.find(getAddressId()));
        }

        return super.getAddress();
    }

    @Override
    public void setAddress(Address address) {
        addressLoaded = true;
        super.setAddress(address);
    }

    @Override
    public Category getDomainType() {
        if (!domainTypeLoaded) {
            this.setDomainType(categoryDao.find(getDomainTypeId()));
        }

        return super.getDomainType();
    }

    @Override
    public void setDomainType(Category domainType) {
        domainTypeLoaded = true;
        super.setDomainType(domainType);
    }

    @Override
    public List<User> getUsers() {
        if (!usersLoaded) {
            this.setUsers(userDao.findByDomainId(getDomainId()));
        }

        return super.getUsers();
    }

    @Override
    public void setUsers(List<User> users) {
        usersLoaded = true;
        super.setUsers(users);
    }

    @Override
    public List<ProductInstance> getProductInstances() {
        if (!productInstancesLoaded) {
            this.setProductInstances(productInstanceDao.findByDomainId(getDomainId()));
        }

        return super.getProductInstances();
    }

    @Override
    public void setProductInstances(List<ProductInstance> productInstances) {
        productInstancesLoaded = true;
        super.setProductInstances(productInstances);
    }
}
