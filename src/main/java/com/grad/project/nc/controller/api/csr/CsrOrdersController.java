package com.grad.project.nc.controller.api.csr;

import com.grad.project.nc.controller.api.dto.FrontendOrder;
import com.grad.project.nc.model.*;
import com.grad.project.nc.service.orders.OrdersService;
import com.grad.project.nc.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

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

    private Map<String, Object> newOrder(BiFunction<Long, Long, ProductOrder> function, Map<String, String> params) {
        Map<String, Object> result = new HashMap<>();
        try {
            long instanceId = Long.parseLong(params.get("instanceId"));
            long userId = Long.parseLong(params.get("userId"));
            ProductOrder order = function.apply(instanceId, userId);
            if (order != null) {
                result.put("status", "success");
                result.put("message", "Order created");
                result.put("id", order.getProductOrderId());
            } else {
                result.put("status", "error");
                result.put("message", "Can not create order");
            }
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
    public Collection<FrontendOrder> getOrders(@PathVariable Long size, @PathVariable Long offset) {

        return ordersService.getAllOrders(size, offset).stream().map((item) -> {
            FrontendOrder order = FrontendOrder.fromEntity(item);

            if ("CREATE".equals(item.getOrderAim().getCategoryName())) {
                order.setPossibleDomains(new HashMap<>());
                item.getUser().getDomains().forEach((domain) -> {
                    order.getPossibleDomains().put(domain.getDomainId(), getFullAddress(domain.getAddress()));
                });

                order.setPossibleProducts(new HashMap<>());
                productService.findByProductTypeId(item.getProductInstance().getPrice()
                                .getProduct().getProductType().getProductTypeId())
                        .forEach((product -> order.getPossibleProducts()
                                .put(product.getProductId(), product.getProductName())));
            }

            return order;
        }).collect(Collectors.toList());
    }

    private static String getFullAddress(Address address) {
        String apt = "";
        if (address.getApartmentNumber() != null && !address.getApartmentNumber().isEmpty()) {
            apt = " apt: " + address.getApartmentNumber();
        }
        return address.getLocation().getGooglePlaceId() + apt;
    }
}
