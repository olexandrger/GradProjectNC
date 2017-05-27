package com.grad.project.nc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRegionPrice {
    private Long priceId;
    private Product product;
    private Region region;
    private double price;

    private List<Discount> discounts;
    private List<ProductInstance> productInstances;

}
