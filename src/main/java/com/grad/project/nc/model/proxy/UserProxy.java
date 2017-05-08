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

    private boolean rolesLoaded;
    private boolean domainsLoaded;
    private boolean productOrdersLoaded;
    private boolean complainsLoaded;

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
        if (!rolesLoaded) {
            this.setRoles(roleDao.findUserRolesById(getUserId()));
        }

        return super.getRoles();
    }

    @Override
    public void setRoles(List<Role> roles) {
        rolesLoaded = true;
        super.setRoles(roles);
    }

    @Override
    public List<Domain> getDomains() {
        if (!domainsLoaded) {
            this.setDomains(domainDao.findByUserId(getUserId()));
        }

        return super.getDomains();
    }

    @Override
    public void setDomains(List<Domain> domains) {
        domainsLoaded = true;
        super.setDomains(domains);
    }

    @Override
    public List<ProductOrder> getProductOrders() {
        if (!productOrdersLoaded) {
            this.setProductOrders(productOrderDao.findByUserId(getUserId()));
        }

        return super.getProductOrders();
    }

    @Override
    public void setProductOrders(List<ProductOrder> productOrders) {
        productOrdersLoaded = true;
        super.setProductOrders(productOrders);
    }

    @Override
    public List<Complain> getComplains() {
        if (!complainsLoaded) {
            this.setComplains(complainDao.findByUserId(getUserId()));
        }

        return super.getComplains();
    }

    @Override
    public void setComplains(List<Complain> complains) {
        complainsLoaded = true;
        super.setComplains(complains);
    }
}
