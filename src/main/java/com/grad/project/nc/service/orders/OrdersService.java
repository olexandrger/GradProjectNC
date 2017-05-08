package com.grad.project.nc.service.orders;

import com.grad.project.nc.model.ProductOrder;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public interface OrdersService {

    /**
     * If user's id not specified, current account used
     */
    ProductOrder newCreationOrder(long productId, long domainId);

    ProductOrder newCreationOrder(long productId, long domainId, long userId);

    ProductOrder newSuspensionOrder(long instanceId);

    ProductOrder newSuspensionOrder(long instanceId, long userId);

    ProductOrder newResumeOrder(long instanceId);

    ProductOrder newResumeOrder(long instanceId, long userId);

    ProductOrder newDeactivationOrder(long instanceId);

    ProductOrder newDeactivationOrder(long instanceId, long userId);

    Collection<ProductOrder> getOrdersByProductInstance(Long id, Long size, Long offset);

    ProductOrder updateOrderInfo(long orderId, long domainId, long productId);

    void startOrder(long orderId);

    void cancelOrder(long orderId);

    void completeOrder(long orderId);

    Collection<ProductOrder> getUserOrders(long size, long offset);

    Collection<ProductOrder> getAllOrders(long size, long offset);
}

