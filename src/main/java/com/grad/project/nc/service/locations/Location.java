package com.grad.project.nc.service.locations;

public interface Location {
    String getRegionName(String address);

    double getRegionLng(String address);

    double getRegionLat(String address);
}
