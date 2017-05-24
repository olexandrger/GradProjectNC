package com.grad.project.nc.controller.api.dto;


import com.grad.project.nc.model.Address;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FrontendAddress {
    private Long addressId;
    private String apartment;
    private FrontendLocation location;

    public static FrontendAddress fromEntity(Address address){
        return FrontendAddress.builder()
                .addressId(address.getAddressId())
                .apartment(address.getApartmentNumber())
                .location(FrontendLocation.fromEntity(address.getLocation()))
                .build();
    }

    public Address toModel(){
        return Address.builder()
                .addressId(getAddressId())
                .apartmentNumber(getApartment())
                .location(getLocation().toModel())
                .build();
    }
}
