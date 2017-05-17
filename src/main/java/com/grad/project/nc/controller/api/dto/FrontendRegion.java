package com.grad.project.nc.controller.api.dto;

import com.grad.project.nc.model.Region;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FrontendRegion {
    private Long regionId;
    private String regionName;

    public static FrontendRegion fromEntity(Region region) {
        return FrontendRegion.builder()
                .regionId(region.getRegionId())
                .regionName(region.getRegionName())
                .build();
    }

    public Region toModel() {
        return Region.builder()
                .regionId(getRegionId())
                .regionName(getRegionName())
                .build();
    }
}
