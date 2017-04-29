package com.grad.project.nc.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
public class ProductOrder {
    private Long productOrderId;
    private Long productInstanceId;
    private Long userId;
    private Long categoryId;
    private Long statusId;
    private Long responsibleId;
    private LocalDateTime openDate;
    private LocalDateTime closeDate;
}
