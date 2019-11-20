package com.example.cheaptrip.handlers.rest.geo;

import android.text.Editable;

import com.example.cheaptrip.dao.GeoCompletionClient;
import com.example.cheaptrip.models.rest.photon.Feature;
import com.example.cheaptrip.models.rest.photon.PhotonResponse;
import com.example.cheaptrip.models.rest.photon.Properties;
import com.example.cheaptrip.handlers.rest.RestHandler;

import java.util.ArrayList;
import java.util.List;

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
        geoCompletionClient = retrofit.create(GeoCompletionClient.class);
        super.call = geoCompletionClient.geoPos(enteredText.toString(),lat,lon);
    }


    /**
     * TODO:Document
     * @param response Response from Rest-api of webservice.
     * @return
     */
    @Override
    public List<String> extractDataFromResponse(Response<PhotonResponse> response) {
        PhotonResponse photonResponse = response.body();
        List<Feature> features = photonResponse.getFeatures();

        List<String> locationNames = new ArrayList<>();

        for (Feature feature : features){
            //Geometry geometry = feature.getGeometry();
            Properties properties = feature.getProperties();

            String name = properties.getName();
            String country = properties.getCountry();

            String textField = name + "(" + country + ")";

            locationNames.add(textField);
        }

        return locationNames;
    }




}
