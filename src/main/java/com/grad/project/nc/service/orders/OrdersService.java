package com.grad.project.nc.service.orders;

import com.grad.project.nc.model.Product;
import com.grad.project.nc.model.ProductOrder;
import com.grad.project.nc.service.exceptions.*;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public interface OrdersService {

    /**
     * If user's id not specified, current account used
     */
    ProductOrder newCreationOrder(long productId, long domainId) throws InsufficientRightsException, OrderCreationException;

    ProductOrder newCreationOrder(long productId, long domainId, long userId) throws InsufficientRightsException, OrderCreationException;

    ProductOrder newSuspensionOrder(long instanceId) throws InsufficientRightsException, OrderCreationException;

    ProductOrder newSuspensionOrder(long instanceId, long userId) throws InsufficientRightsException, OrderCreationException;

    ProductOrder newContinueOrder(long instanceId) throws InsufficientRightsException, OrderCreationException;

    ProductOrder newContinueOrder(long instanceId, long userId) throws InsufficientRightsException, OrderCreationException;

    ProductOrder newDeactivationOrder(long instanceId) throws InsufficientRightsException, OrderCreationException;

    ProductOrder newDeactivationOrder(long instanceId, long userId) throws InsufficientRightsException, OrderCreationException;

    ProductOrder updateOrderInfo(long orderId, long domainId, long productId) throws IllegalOrderOperationException;

    void startOrder(long orderId) throws IllegalOrderOperationException, InsufficientRightsException;

    void cancelOrder(long orderId) throws IllegalOrderOperationException, InsufficientRightsException;

    void completeOrder(long orderId) throws IllegalOrderOperationException, InsufficientRightsException;

    void setResponsible(long orderId, long responsibleid) throws IncorrectRoleException, IncorrectOrderStateException;

    Collection<ProductOrder> getUserOrders(long size, long offset);

    Collection<ProductOrder> getOpenInstanceOrders(long instanceId, long size, long offset);

    Collection<ProductOrder> getAllOrders(long size, long offset);

    Collection<ProductOrder> getOrdersByProductInstance(long id, long size, long offset);

    ProductOrder find(Long id);

    List<ProductOrder> getByAim(String aim, Long size, Long offset);

    List<ProductOrder> getByStatus(String status, Long size, Long offset);

    List<ProductOrder> getByAimAndStatus(String aim, String status, Long size, Long offset);
}

