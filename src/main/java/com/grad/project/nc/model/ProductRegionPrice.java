package com.grad.project.nc.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Alex on 4/24/2017.
 */
@Data
@NoArgsConstructor
public class ProductRegionPrice {
    private Long priceId;
    private Long productId;
    private Long regionID;
    private double price;


}
