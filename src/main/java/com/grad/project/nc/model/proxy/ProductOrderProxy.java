package com.grad.project.nc.model.proxy;

import com.grad.project.nc.model.Category;
import com.grad.project.nc.model.ProductInstance;
import com.grad.project.nc.model.ProductOrder;
import com.grad.project.nc.model.User;
import com.grad.project.nc.persistence.CategoryDao;
import com.grad.project.nc.persistence.ProductInstanceDao;
import com.grad.project.nc.persistence.UserDao;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ProductOrderProxy extends ProductOrder {
    @Getter @Setter
    private Long productInstanceId;
    @Getter @Setter
    private Long userId;
    @Getter @Setter
    private Long orderAimId;
    @Getter @Setter
    private Long statusId;
    @Getter @Setter
    private Long responsibleId;

    private boolean productInstanceLoaded;
    private boolean userLoaded;
    private boolean orderAimLoaded;
    private boolean statusLoaded;
    private boolean responsibleLoaded;

    private final ProductInstanceDao productInstanceDao;
    private final UserDao userDao;
    private final CategoryDao categoryDao;

    @Autowired
    public ProductOrderProxy(CategoryDao categoryDao, UserDao userDao, ProductInstanceDao productInstanceDao) {
        this.categoryDao = categoryDao;
        this.userDao = userDao;
        this.productInstanceDao = productInstanceDao;
    }

    @Override
    public ProductInstance getProductInstance() {
        if (!productInstanceLoaded) {
            this.setProductInstance(productInstanceDao.find(getProductInstanceId()));
        }
        return super.getProductInstance();
    }

    @Override
    public void setProductInstance(ProductInstance productInstance) {
        productInstanceLoaded = true;
        super.setProductInstance(productInstance);
    }

    @Override
    public User getUser() {
        if (!userLoaded) {
            this.setUser(userDao.find(getUserId()));
        }
        return super.getUser();
    }

    @Override
    public void setUser(User user) {
        userLoaded = true;
        super.setUser(user);
    }

    @Override
    public Category getOrderAim() {
        if (!orderAimLoaded) {
            this.setOrderAim(categoryDao.find(getOrderAimId()));
        }
        return super.getOrderAim();
    }

    @Override
    public void setOrderAim(Category orderAim) {
        orderAimLoaded = true;
        super.setOrderAim(orderAim);
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
    public User getResponsible() {
        if (!responsibleLoaded) {
            this.setResponsible(userDao.find(getResponsibleId()));
        }
        return super.getResponsible();
    }

    @Override
    public void setResponsible(User responsible) {
        responsibleLoaded = true;
        super.setResponsible(responsible);
    }
}
