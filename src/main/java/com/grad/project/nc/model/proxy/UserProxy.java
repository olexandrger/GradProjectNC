package com.grad.project.nc.model.proxy;

import com.grad.project.nc.model.*;
import com.grad.project.nc.persistence.ComplainDao;
import com.grad.project.nc.persistence.DomainDao;
import com.grad.project.nc.persistence.ProductOrderDao;
import com.grad.project.nc.persistence.RoleDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
public class UserProxy extends User {

    private final RoleDao roleDao;
    private final DomainDao domainDao;
    private final ProductOrderDao productOrderDao;
    private final ComplainDao complainDao;

    @Autowired
    public UserProxy(RoleDao roleDao, DomainDao domainDao,
                     ProductOrderDao productOrderDao, ComplainDao complainDao) {
        this.roleDao = roleDao;
        this.domainDao = domainDao;
        this.productOrderDao = productOrderDao;
        this.complainDao = complainDao;
    }

    @Override
    public List<Role> getRoles() {
        if (super.getRoles() == null) {
            super.setRoles(roleDao.findUserRolesById(getUserId()));
        }

        return super.getRoles();
    }

    @Override
    public List<Domain> getDomains() {
        if (super.getDomains() == null) {
            super.setDomains(domainDao.findByUserId(getUserId()));
        }

        return super.getDomains();
    }

    @Override
    public List<ProductOrder> getProductOrders() {
        if (super.getProductOrders() == null) {
            super.setProductOrders(productOrderDao.findByUserId(getUserId()));
        }

        return super.getProductOrders();
    }

    @Override
    public List<Complain> getComplains() {
        if (super.getComplains() == null) {
            super.setComplains(complainDao.findByUserId(getUserId()));
        }

        return super.getComplains();
    }
}
