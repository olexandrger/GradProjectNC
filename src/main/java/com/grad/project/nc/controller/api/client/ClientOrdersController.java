package com.grad.project.nc.controller.api.client;

import com.grad.project.nc.controller.api.csr.CsrOrdersController;
import com.grad.project.nc.controller.api.dto.FrontendOrder;
import com.grad.project.nc.model.ProductOrder;
import com.grad.project.nc.service.exceptions.OrderException;
import com.grad.project.nc.service.orders.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/client/orders")
public class ClientOrdersController {

    private OrdersService ordersService;

    @Autowired
    public ClientOrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    private Map<String, Object> newOrder(OrderCreationFunction function, Map<String, String> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            long instanceId = Long.parseLong(params.get("instanceId"));
            ProductOrder order = function.apply(instanceId);
            if (order == null) {
                result.put("status", "error");
                result.put("message", "Can not create order");
            } else {
                result.put("status", "success");
                result.put("message", "Order created");
                result.put("id", order.getProductOrderId());
            }
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            result.put("status", "error");
            result.put("message", "Can not parse identifiers");
        } catch (OrderException e) {
            e.printStackTrace();

            result.put("status", "error");
            result.put("message", "Can not create order");
        }

        return result;
    }

    @RequestMapping(value = "/new/create", method = RequestMethod.POST)
    public Map<String, Object> create(@RequestBody Map<String, String> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            long productId = Long.parseLong(params.get("productId"));
            long domainId = Long.parseLong(params.get("domainId"));
            ProductOrder order = ordersService.newCreationOrder(productId, domainId);
            result.put("status", "success");
            result.put("message", "Order created");
            result.put("id", order.getProductOrderId());
            result.put("instanceId", order.getProductInstance().getInstanceId());
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            result.put("status", "error");
            result.put("message", "Can not parse identifiers");
        } catch (OrderException e) {
            log.error("Can not create order", e);

            result.put("status", "error");
            result.put("message", "Can not create order");
        }

        return result;
    }

    @RequestMapping(value = "/new/suspend", method = RequestMethod.POST)
    public Map<String, Object> suspend(@RequestBody Map<String, String> params) {
        return newOrder(ordersService::newSuspensionOrder, params);
    }

    @RequestMapping(value = "/new/continue", method = RequestMethod.POST)
    public Map<String, Object> activate(@RequestBody Map<String, String> params) {
        return newOrder(ordersService::newContinueOrder, params);
    }

    @RequestMapping(value = "/new/deactivate", method = RequestMethod.POST)
    public Map<String, Object> deactivate(@RequestBody Map<String, String> params) {
        return newOrder(ordersService::newDeactivationOrder, params);
    }

    @RequestMapping(value = "/{id}/cancel", method = RequestMethod.POST)
    public Map<String, String> cancel(@PathVariable Long id) {
        ordersService.cancelOrder(id);
        return Collections.singletonMap("status", "success");
    }

    @RequestMapping(value = "/get/all/size/{size}/offset/{offset}", method = RequestMethod.GET)
    public Collection<FrontendOrder> getOrders(@PathVariable Long size, @PathVariable Long offset) {
        return ordersService.getUserOrders(size, offset).stream().map(FrontendOrder::fromEntity).collect(Collectors.toList());
    }

    @RequestMapping(value = "/get/byInstance/{id}/size/{size}/offset/{offset}", method = RequestMethod.GET)
    public Collection<FrontendOrder> getByProductInstance(@PathVariable Long id, @PathVariable Long size, @PathVariable Long offset) {
        return ordersService.getOrdersByProductInstance(id, size, offset).stream().map(FrontendOrder::fromEntity).collect(Collectors.toList());
    }

    @FunctionalInterface
    public interface OrderCreationFunction {
        ProductOrder apply(long instanceId) throws OrderException;
    }
}
