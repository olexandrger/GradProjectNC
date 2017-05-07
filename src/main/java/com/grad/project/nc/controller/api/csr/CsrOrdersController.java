package com.grad.project.nc.controller.api.csr;

import com.grad.project.nc.model.Product;
import com.grad.project.nc.model.ProductOrder;
import com.grad.project.nc.service.orders.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

@RestController
@RequestMapping("/api/csr/orders")
public class CsrOrdersController {

    private OrdersService ordersService;

    @Autowired
    public CsrOrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    private Map<String, Object> newOrder(BiFunction<Long, Long, ProductOrder> function, Map<String, String> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            long instanceId = Long.parseLong(params.get("instanceId"));
            long userId = Long.parseLong("userId");
            ProductOrder order = function.apply(instanceId, userId);
            result.put("status", "success");
            result.put("message", "Order created");
            result.put("id", order.getProductOrderId());
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            result.put("status", "error");
            result.put("message", "Can not parse identifiers");
        }

        return result;
    }

    @RequestMapping(value = "/new/create", method = RequestMethod.POST)
    public Map<String, Object> create(@RequestBody Map<String, String> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            long productId = Long.parseLong(params.get("productId"));
            long domainId = Long.parseLong(params.get("domainId"));
            long userId = Long.parseLong("userId");
            ProductOrder order = ordersService.newCreationOrder(productId, domainId, userId);
            result.put("status", "success");
            result.put("message", "Order created");
            result.put("id", order.getProductOrderId());
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            result.put("status", "error");
            result.put("message", "Can not parse identifiers");
        }

        return result;
    }

    @RequestMapping(value = "/new/suspend", method = RequestMethod.POST)
    public Map<String, Object> suspend(@RequestBody Map<String, String> params) {
        return newOrder(ordersService::newSuspensionOrder, params);
    }

    @RequestMapping(value = "/new/activate", method = RequestMethod.POST)
    public Map<String, Object> activate(@RequestBody Map<String, String> params) {
        return newOrder(ordersService::newActivationOrder, params);
    }

    @RequestMapping(value = "/new/deactivate", method = RequestMethod.POST)
    public Map<String, Object> deactivate(@RequestBody Map<String, String> params) {
        return newOrder(ordersService::newDeactivationOrder, params);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Map<String, Object> update(@RequestBody Map<String, String> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            long orderId = Long.parseLong(params.get("orderId"));
            long domainId = Long.parseLong(params.get("domainId"));
            long productId = Long.parseLong("productId");
            ProductOrder order = ordersService.updateOrderInfo(orderId, domainId, productId);

            if (order == null) {
                result.put("status", "error");
                result.put("message", "You cannot create order with such parameters");
            } else {
                result.put("status", "success");
                result.put("message", "Order edited");
                result.put("id", order.getProductOrderId());
            }
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            result.put("status", "error");
            result.put("message", "Can not parse identifiers");
        }

        return result;
    }

    @RequestMapping(value = "/{orderId}/start",  method = RequestMethod.POST)
    public Map<String, Object> start(@PathVariable Long orderId) {
        Map<String, Object> result = new HashMap<>();

        ordersService.startOrder(orderId);

        result.put("status", "success");
        result.put("message", "Order in progress");
        return result;
    }

    @RequestMapping(value = "/{orderId}/cancel", method = RequestMethod.POST)
    public Map<String, Object> cancel(@PathVariable Long orderId) {
        Map<String, Object> result = new HashMap<>();

        ordersService.cancelOrder(orderId);

        result.put("status", "success");
        result.put("message", "Order canceled");
        return result;
    }

    @RequestMapping(value = "/{orderId}/complete", method = RequestMethod.POST)
    public Map<String, Object> complete(@PathVariable Long orderId) {
        Map<String, Object> result = new HashMap<>();

        ordersService.completeOrder(orderId);

        result.put("status", "success");
        result.put("message", "Order completed");
        return result;
    }

    @RequestMapping(value = "/get/all/size/{size}/offset/{offset}")
    public Collection<ProductOrder> getOrders(@PathVariable Long size, @PathVariable Long offset) {
        return ordersService.getAllOrders(size, offset);
    }
}
