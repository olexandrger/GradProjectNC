package com.grad.project.nc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Alex on 4/29/2017.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    private Long locationId;
    private String googlePlaceId;
    private Region region;
}
