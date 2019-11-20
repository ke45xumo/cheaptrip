package com.example.cheaptrip.handlers.rest.geo;

import com.example.cheaptrip.dao.GeoCompletionClient;
import com.example.cheaptrip.handlers.rest.RestHandler;
import com.example.cheaptrip.models.rest.photon.Feature;
import com.example.cheaptrip.models.rest.photon.PhotonResponse;

import java.util.List;

import retrofit2.Response;

public class GeoFeatureRestHandler extends RestHandler<List<Feature>, PhotonResponse> {
    private final static String BASE_URL = "http://photon.komoot.de/";

    /**
     * TODO: Document
     *
     */
    public GeoFeatureRestHandler(String locationName) {
        super(BASE_URL);
        GeoCompletionClient geoCompletionClient = retrofit.create(GeoCompletionClient.class);

        super.call =   geoCompletionClient.geoPos(locationName);
    }

    @Override
    public List<Feature> extractDataFromResponse(Response<PhotonResponse> response) {
        PhotonResponse photonResponse = response.body();
        List<Feature> features = photonResponse.getFeatures();

        return features;
    }
}
