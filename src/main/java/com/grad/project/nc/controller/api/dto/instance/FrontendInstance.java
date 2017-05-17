package com.grad.project.nc.controller.api.dto;


import com.grad.project.nc.controller.api.dto.instance.FrontendInstanceProduct;
import com.grad.project.nc.model.ProductInstance;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class FrontendInstance {

    private Long instanceId;
    private FrontendPrice price;
    private FrontendInstanceProduct product;
    private FrontendDomain domain;
    private FrontendCategory status;

    private List<FrontendOrder> productOrders;

    public static FrontendInstance fromEntity(ProductInstance instance) {
        return builder()
                .instanceId(instance.getInstanceId())
                .price(FrontendPrice.fromEntity(instance.getPrice()))
                .product(FrontendInstanceProduct.fromEntity(instance.getPrice().getProduct()))
                .domain(FrontendDomain.fromEntity(instance.getDomain()))
                .status(FrontendCategory.fromEntity(instance.getStatus()))
                .productOrders(instance.getProductOrders().stream().map(FrontendOrder::fromEntity).collect(Collectors.toList()))
                .build();
    }
}
