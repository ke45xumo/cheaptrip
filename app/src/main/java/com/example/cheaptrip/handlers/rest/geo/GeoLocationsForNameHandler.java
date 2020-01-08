package com.example.cheaptrip.handlers.rest.geo;

import android.text.Editable;

import com.example.cheaptrip.dao.GeoCompletionClient;
import com.example.cheaptrip.handlers.rest.RestHandler;
import com.example.cheaptrip.handlers.rest.RestListener;
import com.example.cheaptrip.models.photon.Location;
import com.example.cheaptrip.models.photon.PhotonResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class GeoLocationsForNameHandler extends RestHandler<List<Location>, PhotonResponse> {
    private final static String BASE_URL = "http://photon.komoot.de/";
    private static GeoCompletionClient geoCompletionClient;

    /**
     * TODO: Document
     *
     */
    public GeoLocationsForNameHandler(String locationName) {
        super(BASE_URL);
        geoCompletionClient = super.getRetrofit().create(GeoCompletionClient.class);

        Call call =   geoCompletionClient.geoPos(locationName);
        super.setCall(call);
    }

    public GeoLocationsForNameHandler(String enteredText, double lat, double lon){
        super(BASE_URL);
        geoCompletionClient = super.getRetrofit().create(GeoCompletionClient.class);
        Call call = geoCompletionClient.geoPos(enteredText,lat,lon);
        super.setCall(call);
    }

    @Override
    public List<Location> extractDataFromResponse(Response<PhotonResponse> response) {
        PhotonResponse photonResponse = response.body();
        List<Location> locations = photonResponse.getLocations();

        return locations;
    }
}
