package com.grad.project.nc.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collection;


/**
 * Created by Alex on 4/24/2017.
 */

@Data
@NoArgsConstructor
public class Discount {
    private Long discountId;
    private String discountTitle;
    private Double discount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Collection<ProductRegionPrice> productRegionPrices;
}
