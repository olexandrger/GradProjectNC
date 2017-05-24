package com.grad.project.nc.controller.api.csr;

import com.grad.project.nc.controller.api.dto.FrontendOrder;
import com.grad.project.nc.model.*;
import com.grad.project.nc.service.exceptions.OrderException;
import com.grad.project.nc.service.orders.OrdersService;
import com.grad.project.nc.service.product.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/csr/orders")
public class CsrOrdersController {

    private OrdersService ordersService;
    private ProductService productService;


    @Autowired
    public CsrOrdersController(OrdersService ordersService, ProductService productService) {
        this.ordersService = ordersService;
        this.productService = productService;
    }

    private Map<String, Object> newOrder(OrderCreationFunction function, Map<String, String> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            long instanceId = Long.parseLong(params.get("instanceId"));
            long userId = Long.parseLong(params.get("userId"));
            ProductOrder order = function.apply(instanceId, userId);

            result.put("status", "success");
            result.put("message", "Order created");
            result.put("id", order.getProductOrderId());
        } catch (NumberFormatException ex) {
            log.error("Can not parse identifiers", ex);

            result.put("status", "error");
            result.put("message", "Can not parse identifiers");
        } catch (OrderException e) {
            log.error("Can not create order", e);

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
            long userId = Long.parseLong("userId");
            ProductOrder order = ordersService.newCreationOrder(productId, domainId, userId);
            result.put("status", "success");
            result.put("message", "Order created");
            result.put("id", order.getProductOrderId());
        }  catch (NumberFormatException ex) {
            log.error("Can not parse identifiers", ex);

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

    @RequestMapping(value = "/new/activate", method = RequestMethod.POST)
    public Map<String, Object> activate(@RequestBody Map<String, String> params) {
        return newOrder(ordersService::newContinueOrder, params);
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
            long productId = Long.parseLong(params.get("productId"));
            ProductOrder order = ordersService.updateOrderInfo(orderId, domainId, productId);

            result.put("status", "success");
            result.put("message", "Order edited");
            result.put("id", order.getProductOrderId());
        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            result.put("status", "error");
            result.put("message", "Can not parse identifiers");
        } catch (OrderException e) {
            log.error("Can not create order", e);

            result.put("status", "error");
            result.put("message", "This product is not available in the selected region");
        }

        return result;
    }

    @RequestMapping(value = "/{orderId}/start",  method = RequestMethod.POST)
    public Map<String, Object> start(@PathVariable Long orderId) {
        Map<String, Object> result = new HashMap<>();

        try {
            ordersService.startOrder(orderId);

            result.put("status", "success");
            result.put("message", "Order in progress");
        } catch (OrderException e) {
            log.error("Can not start order", e);
            result.put("status", "error");
            result.put("message", e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/{orderId}/cancel", method = RequestMethod.POST)
    public Map<String, Object> cancel(@PathVariable Long orderId) {
        Map<String, Object> result = new HashMap<>();

        try {
            ordersService.cancelOrder(orderId);

            result.put("status", "success");
            result.put("message", "Order canceled");
        } catch (OrderException e) {
            log.error("Can not cancel order", e);
            result.put("status", "error");
            result.put("message", e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/{orderId}/complete", method = RequestMethod.POST)
    public Map<String, Object> complete(@PathVariable Long orderId) {
        Map<String, Object> result = new HashMap<>();

        try {
            ordersService.completeOrder(orderId);

            result.put("status", "success");
            result.put("message", "Order completed");
        } catch (OrderException e) {
            log.error("Can not complete order", e);
            result.put("status", "error");
            result.put("message", e.getMessage());
        }
        return result;
    }

    private Collection<FrontendOrder> convertToFrontendOrders(Collection<ProductOrder> orders) {
        return orders.stream().map(order ->
                FrontendOrder.fromEntityWithModificationInfo(order,
                order.getUser().getDomains(),
                productService.findByProductTypeId(order.getProductInstance().getPrice().getProduct().getProductType().getProductTypeId())
        )).collect(Collectors.toList());
    }

    @Deprecated
    @RequestMapping(value = "/get/all/size/{size}/offset/{offset}")
    public Collection<FrontendOrder> getOrders(@PathVariable Long size, @PathVariable Long offset) {
        return convertToFrontendOrders(ordersService.getAllOrders(size, offset));
    }

    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public FrontendOrder getOrderById(@PathVariable Long id) {
        ProductOrder order = ordersService.find(id);
        if (order == null) {
            return null;
        } else {
            return  FrontendOrder.fromEntityWithModificationInfo(order,
                    order.getUser().getDomains(),
                    productService.findByProductTypeId(order.getProductInstance().getPrice().getProduct().getProductType().getProductTypeId()));
        }
    }


    @RequestMapping(value = "/get/all", method = RequestMethod.GET)
    public Collection<FrontendOrder> getOrdersByCategories(
            @RequestParam(value = "aim", required = false) String aim,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "size") Long size,
            @RequestParam(value = "offset") Long offset) {
        Collection<ProductOrder> orders;
        if (aim != null && status != null) {
            orders = ordersService.getByAimAndStatus(aim, status, size, offset);
        } else if (status != null) {
            orders = ordersService.getByStatus(status, size, offset);
        } else if (aim != null) {
            orders = ordersService.getByAim(aim, size, offset);
        } else {
            orders = ordersService.getAllOrders(size, offset);
        }
        return convertToFrontendOrders(orders);
    }

    @FunctionalInterface
    public interface OrderCreationFunction {
        ProductOrder apply(long instanceId, long userId) throws OrderException;
    }
}
