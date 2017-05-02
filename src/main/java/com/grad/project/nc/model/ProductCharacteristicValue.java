package com.grad.project.nc.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Created by Alex on 4/24/2017.
 */

@Data
@NoArgsConstructor
public class ProductCharacteristicValue {
    private Long productId;
    private Long productCharacteristicId;
    private Double numberValue;
    private LocalDateTime dateValue;
    private String stringValue;


}
