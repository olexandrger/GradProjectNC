package com.grad.project.nc.controller.api.dto;

import com.grad.project.nc.model.Location;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FrontendLocation {
    private Long locationId;
    private String googlePlaceId;
    private FrontendRegion region;

    public static FrontendLocation fromEntity(Location location){
        return builder()
                .locationId(location.getLocationId())
                .googlePlaceId(location.getGooglePlaceId())
                .region(FrontendRegion.fromEntity(location.getRegion()))
                .build();
    }

    public Location toModel(){
        return Location.builder()
                .locationId(getLocationId())
                .googlePlaceId(getGooglePlaceId())
                .region(region.toModel())
                .build();
    }
}
