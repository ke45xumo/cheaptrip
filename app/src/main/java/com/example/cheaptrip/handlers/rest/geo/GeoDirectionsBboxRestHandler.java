package com.example.cheaptrip.handlers.rest.geo;

import com.example.cheaptrip.dao.ORServiceClient;
import com.example.cheaptrip.handlers.rest.RestHandler;
import com.example.cheaptrip.models.TripLocation;
import com.example.cheaptrip.models.orservice.ORServiceResponse;
import com.example.cheaptrip.models.orservice.PostBody;
import com.google.gson.Gson;

import org.osmdroid.util.BoundingBox;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class GeoDirectionsBboxRestHandler extends RestHandler<BoundingBox, ORServiceResponse> {

    private static String BASE_URL = "https://api.openrouteservice.org/v2/";

    private TripLocation tripLocationStart;
    private TripLocation tripLocationEnd;

    public GeoDirectionsBboxRestHandler(TripLocation start, TripLocation destination) {
        super(BASE_URL);
        tripLocationStart = start;
        tripLocationEnd = destination;
        ORServiceClient orServiceClient = retrofit.create(ORServiceClient.class);

        String postBody = prepareBody(start,destination);
        super.call = orServiceClient.getDirections(postBody);
    }

    private String prepareBody(TripLocation start, TripLocation end){
        List<List<Double>> coordinates = new ArrayList<>();

        List<Double> coordinatesStart = new ArrayList<>();
        coordinatesStart.add(start.getLongitude());
        coordinatesStart.add(start.getLatitdue());

        List<Double> coordinatesEnd = new ArrayList<>();
        coordinatesEnd.add(end.getLongitude());
        coordinatesEnd.add(end.getLatitdue());

        coordinates.add(coordinatesStart);
        coordinates.add(coordinatesEnd);

        PostBody postBody = new PostBody(coordinates,"km");
        Gson gson = new Gson();
        String json = gson.toJson(postBody);

        return json;
    }

    @Override
    public BoundingBox extractDataFromResponse(Response<ORServiceResponse> response) {
        ORServiceResponse orServiceResponse = response.body();

        List<Double> bbox = orServiceResponse.getBbox();

        BoundingBox boundingBox = new BoundingBox(bbox.get(0),bbox.get(1),bbox.get(2),bbox.get(3));

        return boundingBox;
    }
}
