package com.grad.project.nc.service.locations;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;

@Service
@PropertySource("classpath:locations.properties")
public class LocationServiceImpl implements LocationService {
    @Value("${baseurl}")
    private String baseUrl;
    @Value("${main-language}")
    private String mainLanguage;
    @Value("${google-key}")
    private String googleKey;

    private JSONObject location;
    private JSONLocationReader jsonLocationReader = new JSONLocationReader();

    @Override
    public LocationService doRequestForJSONByAddress(String address) {
        if (doRequestForJSONByAddress(address, mainLanguage) != null) {
            location = doRequestForJSONByAddress(address, mainLanguage);
        }
        return this;
    }

    @Override
    public LocationService doRequestForJSONByGooglePlaceId(String id) {
        if (doRequestForJSONByGooglePlaceId(id, mainLanguage) != null) {
            location = doRequestForJSONByGooglePlaceId(id, mainLanguage);

        }
        return this;
    }

    @Override
    public String getFullAddress(String language) {
        try {
            String fullAddress = location.getString("formatted_address");
            return doRequestForJSONByAddress(fullAddress, language).getString("formatted_address");
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
    public String getGooglePlaceId() {
        try {
            return location.getString("place_id");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getStreet() {
        try {
            return location.getJSONArray("address_components").getJSONObject(1).getString("long_name");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getBuildingNumber() {
        try {
            return location.getJSONArray("address_components").getJSONObject(0).getString("long_name");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getCity() {
        try {
            return location.getJSONArray("address_components").getJSONObject(3).getString("long_name");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private JSONObject doRequestForJSONByAddress(String address, String language) {
        try {
            String url = baseUrl + "language=" + language + "&address=" + URLEncoder.encode(address, "utf-8")
                    + "&key=" + googleKey;
            return doRequestForJSON(url);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private JSONObject doRequestForJSONByGooglePlaceId(String id, String language) {
        try {
            String url = baseUrl + "language=" + language + "&place_id=" + id
                    + "&key=" + googleKey;
            return doRequestForJSON(url);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private JSONObject doRequestForJSON(String url) throws IOException, JSONException {
        JSONObject response = jsonLocationReader.getJSONObject(url);
        if ("OK".equals(response.getString("status"))) {
            return response.getJSONArray("results").getJSONObject(0);
        } else {
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

    private final class JSONLocationReader {
        private String readJSON(Reader rd) throws IOException {
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            return sb.toString();
        }

        public JSONObject getJSONObject(String url) throws IOException, JSONException {
            final InputStream is = new URL(url).openStream();
            try {
                BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
                String jsonText = readJSON(rd);
                JSONObject json = new JSONObject(jsonText);
                return json;
            } finally {
                is.close();
            }
        }
    }
}

