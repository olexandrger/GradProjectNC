package com.grad.project.nc.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ProductRegionPrice {
    private Long priceId;
    private Product product;
    private Region region;
    private double price;

    private List<Discount> discounts;

    public ProductRegionPrice(Region region, double price) {
        this.region = region;
        this.price = price;
    }
}
