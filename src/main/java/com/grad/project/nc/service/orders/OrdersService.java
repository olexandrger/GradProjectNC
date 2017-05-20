package com.grad.project.nc.service.orders;

import com.grad.project.nc.model.ProductOrder;
import com.grad.project.nc.service.exceptions.IllegalOrderOperationException;
import com.grad.project.nc.service.exceptions.OrderCreationException;
import com.grad.project.nc.service.exceptions.OrderException;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public interface OrdersService {

    /**
     * If user's id not specified, current account used
     */
    ProductOrder newCreationOrder(long productId, long domainId) throws OrderException;

    ProductOrder newCreationOrder(long productId, long domainId, long userId) throws OrderException;

    ProductOrder newSuspensionOrder(long instanceId) throws OrderException;

    ProductOrder newSuspensionOrder(long instanceId, long userId) throws OrderException;

    ProductOrder newContinueOrder(long instanceId) throws OrderException;

    ProductOrder newContinueOrder(long instanceId, long userId) throws OrderException;

    ProductOrder newDeactivationOrder(long instanceId) throws OrderException;

    ProductOrder newDeactivationOrder(long instanceId, long userId) throws OrderException;

    ProductOrder updateOrderInfo(long orderId, long domainId, long productId) throws OrderException;

    void startOrder(long orderId);

    void cancelOrder(long orderId);

    void completeOrder(long orderId);

    Collection<ProductOrder> getUserOrders(long size, long offset);

    Collection<ProductOrder> getOpenInstanceOrders(long instanceId, long size, long offset);

    Collection<ProductOrder> getAllOrders(long size, long offset);

    Collection<ProductOrder> getOrdersByProductInstance(long id, long size, long offset);
}

