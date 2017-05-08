package com.grad.project.nc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductOrder {
    private Long productOrderId;
    private ProductInstance productInstance;
    private User user;
    private Category orderAim;
    private Category status;
    private User responsible;
    private OffsetDateTime openDate;
    private OffsetDateTime closeDate;
}