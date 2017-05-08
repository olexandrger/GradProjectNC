package com.grad.project.nc.model.proxy;

import com.grad.project.nc.model.Category;
import com.grad.project.nc.model.Complain;
import com.grad.project.nc.model.ProductInstance;
import com.grad.project.nc.model.User;
import com.grad.project.nc.persistence.CategoryDao;
import com.grad.project.nc.persistence.ProductInstanceDao;
import com.grad.project.nc.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ComplainProxy extends Complain {
    private Long userId;
    private Long productInstanceId;
    private Long statusId;
    private Long responsibleId;
    private Long complainReasonId;

    private final UserDao userDao;
    private final ProductInstanceDao productInstanceDao;
    private final CategoryDao categoryDao;

    @Autowired
    public ComplainProxy(UserDao userDao, ProductInstanceDao productInstanceDao, CategoryDao categoryDao) {
        this.userDao = userDao;
        this.productInstanceDao = productInstanceDao;
        this.categoryDao = categoryDao;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductInstanceId() {
        return productInstanceId;
    }

    public void setProductInstanceId(Long productInstanceId) {
        this.productInstanceId = productInstanceId;
    }

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public Long getResponsibleId() {
        return responsibleId;
    }

    public void setResponsibleId(Long responsibleId) {
        this.responsibleId = responsibleId;
    }

    public Long getComplainReasonId() {
        return complainReasonId;
    }

    public void setComplainReasonId(Long complainReasonId) {
        this.complainReasonId = complainReasonId;
    }

    @Override
    public User getUser() {
        if (super.getUser() == null) {
            super.setUser(userDao.find(getUserId()));
        }

        return super.getUser();
    }

    @Override
    public ProductInstance getProductInstance() {
        if (super.getProductInstance() == null) {
            super.setProductInstance(productInstanceDao.find(getProductInstanceId()));
        }

        return super.getProductInstance();
    }

    @Override
    public Category getStatus() {
        if (super.getStatus() == null) {
            super.setStatus(categoryDao.find(getStatusId()));
        }

        return super.getStatus();
    }

    @Override
    public User getResponsible() {
        if (super.getResponsible() == null) {
            super.setResponsible(userDao.find(getResponsibleId()));
        }

        return super.getResponsible();
    }

    @Override
    public Category getComplainReason() {
        if (super.getComplainReason() == null) {
            super.setComplainReason(categoryDao.find(getComplainReasonId()));
        }

        return super.getComplainReason();
    }
}