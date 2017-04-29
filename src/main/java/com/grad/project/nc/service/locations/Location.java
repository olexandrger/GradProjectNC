package com.grad.project.nc.service.locations;

public interface Location {
    boolean doRequestForJSON(String address);
    String getFullAddress(String language);
    String getRegionName();
    String getLocationId();
    double getRegionLng();
    double getRegionLat();
}

