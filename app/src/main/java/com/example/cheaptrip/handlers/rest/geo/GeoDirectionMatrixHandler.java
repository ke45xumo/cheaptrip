/*
package com.example.cheaptrip.handlers.rest.geo;

import com.example.cheaptrip.dao.ORServiceClient;
import com.example.cheaptrip.handlers.rest.RestHandler;
import com.example.cheaptrip.models.TripLocation;
import com.example.cheaptrip.models.orservice.ORServiceResponse;
import com.example.cheaptrip.models.orservice.PostBody;
import com.example.cheaptrip.models.tankerkoenig.Station;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class GeoDirectionMatrixHandler extends RestHandler<Station, ORServiceResponse> {

    private static String BASE_URL = "https://api.openrouteservice.org/v2/";

    private TripLocation tripLocationStart;
    private TripLocation tripLocationEnd;

    public GeoDirectionsHandler(TripLocation start, TripLocation destination) {
        super(BASE_URL);
        tripLocationStart = start;
        tripLocationEnd = destination;
        ORServiceClient orServiceClient = retrofit.create(ORServiceClient.class);

        String postBody = prepareBody(start,destination);
        call = orServiceClient.getDirections(postBody);
        //call = orServiceClient.getDirections2(getCoordinatesAsList(start,destination),"km");
    }

    private List<List<Double>> getCoordinatesAsList(TripLocation start, TripLocation end){
        List<List<Double>> coordinates = new ArrayList<>();
        List<Double> coordinatesStart = new ArrayList<>();
        coordinatesStart.add(start.getLongitude());
        coordinatesStart.add(start.getLatitdue());

        List<Double> coordinatesEnd = new ArrayList<>();
        coordinatesEnd.add(end.getLongitude());
        coordinatesEnd.add(end.getLatitdue());

        coordinates.add(coordinatesStart);
        coordinates.add(coordinatesEnd);

        return coordinates;
    }

    private String prepareBody(TripLocation start, TripLocation end){
        List<List<Double>> coordinates = getCoordinatesAsList(start, end);

        PostBody postBody = new PostBody(coordinates,"km");
        Gson gson = new Gson();
        String json = gson.toJson(postBody);

        return json;
    }

    @Override
    public ORServiceResponse extractDataFromResponse(Response<ORServiceResponse> response) {
        ORServiceResponse orServiceResponse = response.body();
        return orServiceResponse;
    }
}
*/
