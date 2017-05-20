package com.grad.project.nc.service.exceptions;

import com.grad.project.nc.service.orders.OrdersService;

public class InsufficientRightsException extends OrderException {
    public InsufficientRightsException(String message) {
        super(message);
    }
}
