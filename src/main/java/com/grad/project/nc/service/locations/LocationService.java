package com.grad.project.nc.service.locations;

public interface LocationService {
    //after this method you can obtain regionName, locationId, lat, lng
    boolean doRequestForJSON(String address);
    String getFullAddress(String language);
    String getRegionName();
    String getLocationId();
    double getRegionLng();
    double getRegionLat();
}

