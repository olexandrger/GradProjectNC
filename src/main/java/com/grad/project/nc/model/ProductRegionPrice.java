package com.grad.project.nc.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;


@Data
@NoArgsConstructor
public class ProductRegionPrice {
    private Long priceId;
    private Product product;
    private Region region;
    private double price;
    private Collection<Discount> discounts;
}
