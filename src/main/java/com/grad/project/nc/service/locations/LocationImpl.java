package com.grad.project.nc.service.locations;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;

@Service
@PropertySource("classpath:locations.properties")
public class LocationImpl implements Location {
    @Value("${baseurl}")
    private String baseUrl;
    @Value("${main-language}")
    private String mainLanguage;
    @Value("${google-key}")
    private String googleKey;

    private JSONObject location;
    private JSONReader jsonReader;

    public LocationImpl() {
        jsonReader = new JSONReader();
    }

    @Override
    public boolean doRequestForJSON(String address) {
        if (doRequestForJSON(address, mainLanguage) != null) {
            location = doRequestForJSON(address, mainLanguage);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getFullAddress(String language) {
        try {
            String fullAddress = location.getString("formatted_address");
            return doRequestForJSON(fullAddress, language).getString("formatted_address");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getRegionName() {
        String regionName = null;
        try {
            regionName = getRegionJSON(location);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return regionName;
    }

    @Override
    public double getRegionLng() {
        return getCoordinate("lng");
    }

    @Override
    public double getRegionLat() {
        return getCoordinate("lat");
    }

    @Override
    public String getLocationId() {
        try {
            return location.getString("place_id");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private JSONObject doRequestForJSON(String address, String language) {
        try {
            String url = baseUrl + "language=" + language + "&address=" + URLEncoder.encode(address, "utf-8")
                    + "&key=" + googleKey;
            JSONObject response = jsonReader.getJSONObject(url);
            if ("OK".equals(response.getString("status"))) {
                return response.getJSONArray("results").getJSONObject(0);
            } else {
                return null;
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
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

    private double getCoordinate(String coordinateName) {
        double coordinate = 0;
        try {
            coordinate = location.getJSONObject("geometry").getJSONObject("location").getDouble(coordinateName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return coordinate;
    }
}

