package com.grad.project.nc.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Alex on 4/24/2017.
 */
@Data
@NoArgsConstructor
public class Address {
    private Long addressId;
    private int apartmentNumber;
    private Long location_id;


}
