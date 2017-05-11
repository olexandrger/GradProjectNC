package com.grad.project.nc.service.locations;

public interface LocationService {
    //after this method you can obtain regionName, locationId, lat, lng
    LocationService doRequestForJSONByAddress(String address);
    LocationService doRequestForJSONByGooglePlaceId(String id);
    String getFullAddress(String language);
    String getRegionName();
    String getGooglePlaceId();
    double getRegionLng();
    double getRegionLat();
    String getStreet();
    String getBuildingNumber();
    String getCity();
}

