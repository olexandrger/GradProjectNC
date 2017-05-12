package com.grad.project.nc.service.orders;

import com.grad.project.nc.controller.api.dto.FrontendOrder;
import com.grad.project.nc.service.exceptions.ServiceSecurityException;
import com.grad.project.nc.model.*;
import com.grad.project.nc.persistence.*;
import com.grad.project.nc.service.notifications.EmailService;
import com.grad.project.nc.service.security.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Supplier;

//TODO security of states changing

@Service
@Slf4j
public class OrdersServiceImpl implements OrdersService {

    private UserDao userDao;
    private ProductOrderDao orderDao;
    private ProductRegionPriceDao productRegionPriceDao;
    private DomainDao domainDao;
    private ProductInstanceDao productInstanceDao;
    private CategoryDao categoryDao;
    private ProductDao productDao;
    private UserService userService;
    private EmailService emailService;

    private static final long INSTANCE_STATUS_CREATED = 9L;
    private static final long INSTANCE_STATUS_ACTIVATED = 10L;
    private static final long INSTANCE_STATUS_SUSPENDED = 11L;
    private static final long INSTANCE_STATUS_DEACTIVATED = 12L;

    private static final long ORDER_AIM_CREATE = 13L;
    private static final long ORDER_AIM_SUSPEND = 14L;
    private static final long ORDER_AIM_DEACTIVATE = 15L;
    private static final long ORDER_AIM_RESUME = 25L;
//    private static final long ORDER_AIM_MODIFY = 26L;

    private static final long ORDER_STATUS_CREATED = 1L;
    private static final long ORDER_STATUS_IN_PROGRESS = 2L;
    private static final long ORDER_STATUS_CANCELLED = 3L;
    private static final long ORDER_STATUS_COMPLETED = 4L;

    @Autowired
    public OrdersServiceImpl(UserDao userDao, ProductOrderDao orderDao, ProductRegionPriceDao productRegionPriceDao,
                             DomainDao domainDao, ProductInstanceDao productInstanceDao, CategoryDao categoryDao,
                             ProductDao productDao, UserService userService, EmailService emailService) {
        this.userDao = userDao;
        this.orderDao = orderDao;
        this.productRegionPriceDao = productRegionPriceDao;
        this.domainDao = domainDao;
        this.productInstanceDao = productInstanceDao;
        this.categoryDao = categoryDao;
        this.productDao = productDao;
        this.userService = userService;
        this.emailService = emailService;
    }

    private ProductOrder newOrder(long instanceId, long userId, long aimId) {
        ProductInstance instance = productInstanceDao.find(instanceId);
        Category canceled = categoryDao.find(ORDER_STATUS_CANCELLED);
        Category completed = categoryDao.find(ORDER_STATUS_COMPLETED);
        boolean canCreate = instance.getProductOrders().stream()
                .allMatch((order) -> order.getStatus().equals(canceled) || order.getStatus().equals(completed));

        if (!canCreate) {
            return null;
        } else {

            ProductOrder order = new ProductOrder();

            order.setOpenDate(OffsetDateTime.now());
            order.setOrderAim(categoryDao.find(aimId));
            order.setProductInstance(instance);
            order.setUser(userDao.find(userId));
            order.setStatus(categoryDao.find(ORDER_STATUS_CREATED));

            order = orderDao.add(order);

            emailService.sendNewOrderEmail(order);

            return order;
        }
    }

    @Override
    public ProductOrder newCreationOrder(long productId, long domainId, long userId) {
        Domain domain = domainDao.find(domainId);

        ProductInstance instance = new ProductInstance();

        Category category = categoryDao.find(INSTANCE_STATUS_CREATED);

        instance.setDomain(domain);
        instance.setStatus(category);
        instance.setPrice(productRegionPriceDao.findByRegionIdAndProductId(
                domain.getAddress().getLocation().getRegion().getRegionId(), productId));

        instance = productInstanceDao.add(instance);

        return newOrder(instance.getInstanceId(), userId, ORDER_AIM_CREATE);
    }

    @Override
    public ProductOrder newSuspensionOrder(long instanceId, long userId) {
        ProductOrder order = newOrder(instanceId, userId, ORDER_AIM_SUSPEND);
        if (order != null) {
            completeOrder(order.getProductOrderId());
        }

        return order;
    }

    @Override
    public ProductOrder newContinueOrder(long instanceId, long userId) {
        ProductOrder order = newOrder(instanceId, userId, ORDER_AIM_RESUME);
        if (order != null) {
            completeOrder(order.getProductOrderId());
            log.info("Completing too");
        }

        return order;
    }

    @Override
    public ProductOrder newDeactivationOrder(long instanceId, long userId) {
        return newOrder(instanceId, userId, ORDER_AIM_DEACTIVATE);
    }

    @Override
    public ProductOrder newCreationOrder(long productId, long domainId) {
        return newCreationOrder(productId, domainId, userService.getCurrentUser().getUserId());
    }

    @Override
    public ProductOrder newSuspensionOrder(long instanceId) {
        return newSuspensionOrder(instanceId, userService.getCurrentUser().getUserId());

    }

    @Override
    public ProductOrder newDeactivationOrder(long instanceId) {
        return newDeactivationOrder(instanceId, userService.getCurrentUser().getUserId());
    }

    @Override
    public ProductOrder newContinueOrder(long instanceId) {
        return newContinueOrder(instanceId, userService.getCurrentUser().getUserId());
    }

    @Override
    public void startOrder(long orderId) {
        ProductOrder order = orderDao.find(orderId);

        order.setStatus(categoryDao.find(ORDER_STATUS_IN_PROGRESS));
        order.setResponsible(userService.getCurrentUser());
        orderDao.update(order);
    }

    @Override
    public void cancelOrder(long orderId) {
        ProductOrder order = orderDao.find(orderId);

        order.setStatus(categoryDao.find(ORDER_STATUS_CANCELLED));
        order.setCloseDate(OffsetDateTime.now());

        if (order.getOrderAim().getCategoryId() == ORDER_AIM_CREATE) {
            order.getProductInstance().setStatus(categoryDao.find(INSTANCE_STATUS_DEACTIVATED));
            productInstanceDao.update(order.getProductInstance());
//            productInstanceDao.delete(order.getProductInstance());
        }
        orderDao.update(order);
    }

    @Override
    public void completeOrder(long orderId) {
        ProductOrder order = orderDao.find(orderId);

        order.setStatus(categoryDao.find(ORDER_STATUS_COMPLETED));
        order.setCloseDate(OffsetDateTime.now());

        if (order.getOrderAim().getCategoryId() == ORDER_AIM_CREATE) {
            order.getProductInstance().setStatus(categoryDao.find(INSTANCE_STATUS_ACTIVATED));
        } else if (order.getOrderAim().getCategoryId() == ORDER_AIM_RESUME) {
            order.getProductInstance().setStatus(categoryDao.find(INSTANCE_STATUS_ACTIVATED));
        } else if (order.getOrderAim().getCategoryId() == ORDER_AIM_DEACTIVATE) {
            order.getProductInstance().setStatus(categoryDao.find(INSTANCE_STATUS_DEACTIVATED));
        } else if (order.getOrderAim().getCategoryId() == ORDER_AIM_SUSPEND) {
            order.getProductInstance().setStatus(categoryDao.find(INSTANCE_STATUS_SUSPENDED));
        }

        productInstanceDao.update(order.getProductInstance());
        orderDao.update(order);

        emailService.sendInstanceStatusChangedEmail(order.getProductInstance());
    }

    @Override
    public List<ProductOrder> getUserOrders(long size, long offset) {
        return orderDao.findByUserId(userService.getCurrentUser().getUserId(), size, offset);
    }

    @Override
    public List<ProductOrder> getAllOrders(long size, long offset) {
        return orderDao.findAll(size, offset);
    }

    @Override
    public void userCancelOrder(Long id) {
        cancelOrder(id);
    }

    @Override
    public List<ProductOrder> getOrdersByProductInstance(long id, long size, long offset) {
        return orderDao.findByProductInstanceId(id, size, offset);
    }
    @Override
    public Collection<ProductOrder> getOpenInstanceOrders(long instanceId, long size, long offset) {
        return orderDao.findOpenOrdersByInstanseId(instanceId, size, offset);
    }

    @Override
    public ProductOrder updateOrderInfo(long orderId, long domainId, long productId) {
        ProductOrder order = orderDao.find(orderId);
        Domain domain = domainDao.find(domainId);

        ProductRegionPrice price = productRegionPriceDao.findByRegionIdAndProductId(
                domain.getAddress().getLocation().getRegion().getRegionId(), productId);

        if (price == null) {
            return null;
        } else {
            order.getProductInstance().setDomain(domain);
            order.getProductInstance().setPrice(price);
            productInstanceDao.update(order.getProductInstance());
            orderDao.update(order);
            return order;
        }
    }
}
