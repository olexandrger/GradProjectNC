package com.grad.project.nc.controller.api.dto.instance;


import com.grad.project.nc.controller.api.dto.*;
import com.grad.project.nc.model.ProductInstance;
import lombok.Builder;
import lombok.Data;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class FrontendInstance {

    private Long instanceId;
    private FrontendInstancePrice price;
    private FrontendInstanceProduct product;
    //private FrontendDomain domain;
    //private FrontendAddress address;
    private FrontendInstanceAddress address;
    private FrontendCategory status;

    private List<FrontendOrder> productOrders;

    public static FrontendInstance fromEntity(ProductInstance instance) {
        return builder()
                .instanceId(instance.getInstanceId())
                .price(FrontendInstancePrice.fromEntity(instance.getPrice()))
                .product(FrontendInstanceProduct.fromEntity(instance.getPrice().getProduct()))
                //.domain(FrontendDomain.fromEntity(instance.getDomain()))
                //.address(FrontendInstanceAddress.fromEntity(instance.getDomain().getAddress()))
                .status(FrontendCategory.fromEntity(instance.getStatus()))
                .productOrders(instance.getProductOrders().stream().map(FrontendOrder::fromEntity).collect(Collectors.toList()))
                .build();
    }
}
