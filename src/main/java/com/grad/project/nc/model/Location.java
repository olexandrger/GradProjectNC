package com.grad.project.nc.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Alex on 4/29/2017.
 */
@Data
@NoArgsConstructor
public class Location {
    private Long locationId;
    private String googlePlaceId;
    private GoogleRegion googleRegion;

}
