package com.example.cheaptrip.handlers.rest.geo;

import android.text.Editable;

import com.example.cheaptrip.dao.GeoCompletionClient;
import com.example.cheaptrip.models.photon.Location;
import com.example.cheaptrip.models.photon.PhotonResponse;
import com.example.cheaptrip.models.photon.Properties;
import com.example.cheaptrip.handlers.rest.RestHandler;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * TODO: Document
 */
public class GeoNameListRestHandler extends RestHandler<List<String>,PhotonResponse> {
    private final static String BASE_URL = "http://photon.komoot.de/";

    private GeoCompletionClient geoCompletionClient = null;

    /**
     * TODO: Document
     *
     */
    public GeoNameListRestHandler(Editable enteredText, double lat, double lon) {
        super(BASE_URL);
        geoCompletionClient = super.getRetrofit().create(GeoCompletionClient.class);
        Call call = geoCompletionClient.geoPos(enteredText.toString(),lat,lon);
        super.setCall(call);
    }


    /**
     * TODO:Document
     * @param response Response from Rest-api of webservice.
     * @return
     */
    @Override
    public List<String> extractDataFromResponse(Response<PhotonResponse> response) {
        PhotonResponse photonResponse = response.body();
        List<Location> locations = photonResponse.getLocations();
        List<String> locationNames = new ArrayList<>();

        for (Location location : locations){
            //Geometry geometry = location.getGeometry();
            Properties properties = location.getProperties();

            String name = properties.getName();
            String country = properties.getCountry();
            String city = properties.getCity();
            //String textField = name + "(" + country + ")";
            String textField = city + ", " + name;

            locationNames.add(textField);
        }

        return locationNames;
    }




}
