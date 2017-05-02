package com.grad.project.nc.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
public class ProductOrder {
    private Long productOrderId;
    private ProductInstance productInstance;
    private User user;
    private Category orderAim;
    private Category status;
    private User responsible;
    private LocalDateTime openDate;
    private LocalDateTime closeDate;
}
