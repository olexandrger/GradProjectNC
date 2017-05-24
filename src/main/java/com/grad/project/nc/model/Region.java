package com.grad.project.nc.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Region {
    private Long regionId;
    private String regionName;

    //private List<Location> locations;
    private List<ProductRegionPrice> prices;

    public Region(Long regionId) {
        this.regionId = regionId;
    }
}