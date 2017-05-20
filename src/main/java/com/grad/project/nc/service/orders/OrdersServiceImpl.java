package com.grad.project.nc.service.orders;

import com.grad.project.nc.model.*;
import com.grad.project.nc.persistence.*;
import com.grad.project.nc.service.exceptions.IllegalOrderOperationException;
import com.grad.project.nc.service.exceptions.InsufficientRightsException;
import com.grad.project.nc.service.exceptions.OrderCreationException;
import com.grad.project.nc.service.exceptions.OrderException;
import com.grad.project.nc.service.notifications.EmailService;
import com.grad.project.nc.service.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;

@Service
public class OrdersServiceImpl implements OrdersService {

    private List<GrantedAuthority> CSR_AUTHORITIES = Arrays.asList(new SimpleGrantedAuthority("ROLE_CSR"));

    private UserDao userDao;
    private ProductOrderDao orderDao;
    private ProductRegionPriceDao productRegionPriceDao;
    private DomainDao domainDao;
    private ProductInstanceDao productInstanceDao;
    private CategoryDao categoryDao;
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
    private static final long ORDER_AIM_MODIFY = 26L;

    private static final long ORDER_STATUS_CREATED = 1L;
    private static final long ORDER_STATUS_IN_PROGRESS = 2L;
    private static final long ORDER_STATUS_CANCELLED = 3L;
    private static final long ORDER_STATUS_COMPLETED = 4L;

    @Autowired
    public OrdersServiceImpl(UserDao userDao, ProductOrderDao orderDao, ProductRegionPriceDao productRegionPriceDao,
                             DomainDao domainDao, ProductInstanceDao productInstanceDao, CategoryDao categoryDao,
                             UserService userService, EmailService emailService) {
        this.userDao = userDao;
        this.orderDao = orderDao;
        this.productRegionPriceDao = productRegionPriceDao;
        this.domainDao = domainDao;
        this.productInstanceDao = productInstanceDao;
        this.categoryDao = categoryDao;

        this.userService = userService;
        this.emailService = emailService;
    }

    private boolean isAllowedToCreateOrder(User user, Domain domain) {

        return domain.getUsers().stream().anyMatch(u -> user.getUserId().equals(u.getUserId())) ||
                !Collections.disjoint(user.getAuthorities(), CSR_AUTHORITIES);
    }

    private ProductOrder newOrder(long instanceId, long aimId, long userId) throws OrderException {
        ProductInstance instance = productInstanceDao.find(instanceId);

        if (!isAllowedToCreateOrder(userService.getCurrentUser(), instance.getDomain())) {
            throw new InsufficientRightsException("User must have CSR authority to create orders at this domain");
        }
        Category cancelled = categoryDao.find(ORDER_STATUS_CANCELLED);
        Category completed = categoryDao.find(ORDER_STATUS_COMPLETED);

        boolean canCreate = instance.getProductOrders().stream()
                .allMatch((order) -> order.getStatus().equals(cancelled) || order.getStatus().equals(completed));

        if (!canCreate) {
            throw new OrderCreationException("You can not create more than one active order per instance");
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
    public ProductOrder newCreationOrder(long productId, long domainId, long userId) throws OrderException {
        Domain domain = domainDao.find(domainId);

        ProductRegionPrice price = productRegionPriceDao.find(
                domain.getAddress().getLocation().getRegion().getRegionId(), productId);

        if (price == null) {
            throw new OrderCreationException("No price for this product in selected region");
        }

        ProductInstance instance = new ProductInstance();

        Category category = categoryDao.find(INSTANCE_STATUS_CREATED);

        instance.setStatus(category);
        instance.setDomain(domain);
        instance.setPrice(price);

        instance = productInstanceDao.add(instance);

        try {
            return newOrder(instance.getInstanceId(), ORDER_AIM_CREATE, userId);
        } catch (OrderException e) {
            productInstanceDao.delete(instance.getInstanceId());
            throw e;
        }
    }

    @Override
    public ProductOrder newSuspensionOrder(long instanceId, long userId) throws OrderException {
        ProductOrder order = newOrder(instanceId, ORDER_AIM_SUSPEND, userId);

        completeOrder(order.getProductOrderId());

        return order;
    }

    @Override
    public ProductOrder newContinueOrder(long instanceId, long userId) throws OrderException {
        ProductOrder order = newOrder(instanceId, ORDER_AIM_RESUME, userId);

        completeOrder(order.getProductOrderId());

        return order;
    }

    @Override
    public ProductOrder newDeactivationOrder(long instanceId, long userId) throws OrderException {
        return newOrder(instanceId, ORDER_AIM_DEACTIVATE, userId);
    }

    @Override
    public ProductOrder newCreationOrder(long productId, long domainId) throws OrderException {
        return newCreationOrder(productId, domainId, userService.getCurrentUser().getUserId());
    }

    @Override
    public ProductOrder newSuspensionOrder(long instanceId) throws OrderException {
        return newSuspensionOrder(instanceId, userService.getCurrentUser().getUserId());
    }

    @Override
    public ProductOrder newDeactivationOrder(long instanceId) throws OrderException {
        return newDeactivationOrder(instanceId, userService.getCurrentUser().getUserId());
    }

    @Override
    public ProductOrder newContinueOrder(long instanceId) throws OrderException {
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
    public List<ProductOrder> getOrdersByProductInstance(long id, long size, long offset) {
        return orderDao.findByProductInstanceId(id, size, offset);
    }
    @Override
    public Collection<ProductOrder> getOpenInstanceOrders(long instanceId, long size, long offset) {
        return orderDao.findOpenOrdersByInstanseId(instanceId, size, offset);
    }

    @Override
    public ProductOrder updateOrderInfo(long orderId, long domainId, long productId) throws IllegalOrderOperationException {
        ProductOrder order = orderDao.find(orderId);
        Domain domain = domainDao.find(domainId);

        ProductRegionPrice price = productRegionPriceDao.find(
                domain.getAddress().getLocation().getRegion().getRegionId(), productId);

        if (price == null) {
            throw new IllegalOrderOperationException("No prices for this product at selected region");
        } else {
            order.getProductInstance().setDomain(domain);
            order.getProductInstance().setPrice(price);
            productInstanceDao.update(order.getProductInstance());
            orderDao.update(order);
            return order;
        }
    }
}
