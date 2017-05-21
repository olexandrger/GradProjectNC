package com.grad.project.nc.controller.api.dto;


import com.grad.project.nc.model.Address;
import com.grad.project.nc.service.locations.LocationService;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@Builder
public class FrontendAddress {
    private Long addressId;
    private String city;
    private String street;
    private String building;
    private String apartment;
    private static String googlePlaceId;
    @Autowired
    private static LocationService locationService;

    public static FrontendAddress fromEntity(Address address) {
        googlePlaceId = address.getLocation().getGooglePlaceId();
        locationService.doRequestForJSONByGooglePlaceId(googlePlaceId);
        return builder()
                .city(locationService.getCity())
                .street(locationService.getStreet())
                .building(locationService.getBuildingNumber())
                .apartment(address.getApartmentNumber())
                .build();
    }

    public Address toModel(){
        return Address.builder()
                .addressId(getAddressId())
                //TODO location setter
                //.location()
                .build();
    }
}
