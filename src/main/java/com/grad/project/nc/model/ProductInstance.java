package com.grad.project.nc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by Alex on 4/24/2017.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductInstance {
    private Long instanceId;
    private ProductRegionPrice price;
    private Domain domain;
    private Category status;

    private List<ProductOrder> productOrders;
}
