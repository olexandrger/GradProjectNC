package com.grad.project.nc.service.locations;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;

@Service
public class LocationImpl implements Location {
    public String getRegionName(String address) {
        JSONObject location = null;
        String regionName = null;
        try {
            location = getLocationJSON(address);
            regionName = getRegionJSON(location);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return regionName;
    }

    public double getRegionLng(String address) {
        return getCoordinate(address, "lng");
    }

    public double getRegionLat(String address) {
        return getCoordinate(address, "lat");
    }

    private String getRegionJSON(JSONObject location) throws JSONException {
        String region = null;
        region = getRegionLevelJSON(location, 1);
        if (region == null) {
            region = getRegionLevelJSON(location, 2);
        }
        return region;
    }

    private String getRegionLevelJSON(JSONObject location, int administrativeLevel) throws JSONException {
        String region = null;
        String administrativeLevelStr = "administrative_area_level_" + administrativeLevel;
        JSONArray addressComponents = location.getJSONArray("address_components");
        for (int i = 0; i < addressComponents.length(); i++) {
            JSONArray types = addressComponents.getJSONObject(i).getJSONArray("types");
            for (int j = 0; j < types.length(); j++) {
                if (administrativeLevelStr.equals(types.getString(j))) {
                    return addressComponents.getJSONObject(i).getString("long_name");
                }
            }
            if (region != null) {
                break;
            }
        }
        return null;
    }

    private double getCoordinate(String address, String coordinateName) {
        double coordinate = 0;
        JSONObject location = null;
        try {
            location = getLocationJSON(address);
            location = location.getJSONObject("geometry");
            location = location.getJSONObject("location");
            coordinate = location.getDouble(coordinateName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return coordinate;
    }

    private JSONObject getLocationJSON(String address) throws JSONException {
        JSONObject location = null;
        try {
            String baseUrl = "http://maps.googleapis.com/maps/api/geocode/json?language=en&address=";
            String url = baseUrl + URLEncoder.encode(address, "utf-8");
            JSONObject response = JsonReader.read(url);
            System.out.println(url);
            location = response.getJSONArray("results").getJSONObject(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return location;
    }
}
