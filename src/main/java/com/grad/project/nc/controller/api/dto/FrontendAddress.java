package com.grad.project.nc.controller.api.dto;


import com.grad.project.nc.model.Address;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FrontendAddress {
    private String city;
    private String street;
    private String building;
    private String apartment;

    public static FrontendAddress fromEntity(Address address) {
        //TODO rework after address api done
        return builder()
                .city("City")
                .street("Street")
                .building("Building")
                .apartment("0")
                .build();
    }
}
