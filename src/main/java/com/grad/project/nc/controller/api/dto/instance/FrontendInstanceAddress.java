package com.grad.project.nc.controller.api.dto.instance;

import com.grad.project.nc.model.Address;
import com.grad.project.nc.service.locations.LocationService;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Alex on 5/27/2017.
 */
@Data
@Builder
public class FrontendInstanceAddress {

    @Autowired
    LocationService locationService;


    private String apartment;
    private String city;
    private String street;
    private String building;



}
