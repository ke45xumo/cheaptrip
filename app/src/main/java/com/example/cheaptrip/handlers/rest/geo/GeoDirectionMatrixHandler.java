package com.example.cheaptrip.handlers.rest.geo;

import com.example.cheaptrip.dao.ORServiceClient;
import com.example.cheaptrip.handlers.rest.RestHandler;
import com.example.cheaptrip.models.orservice.GeoMatrixResponse;
import com.example.cheaptrip.models.orservice.MatrixPostBody;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class GeoDirectionMatrixHandler extends RestHandler<List<List<Double>>,GeoMatrixResponse>{

    private static String BASE_URL = "https://api.openrouteservice.org/v2/";

    List<List<Double>> coordinates;

    public GeoDirectionMatrixHandler(List<List<Double>> coordinates) {
        super(BASE_URL);
        this.coordinates = coordinates;
        String body = prepareBody(coordinates);

        ORServiceClient orServiceClient = retrofit.create(ORServiceClient.class);
        call = orServiceClient.getMatrix(body);
    }

    @Override
    public List<List<Double>> extractDataFromResponse(Response<GeoMatrixResponse> response) {
        GeoMatrixResponse geoMatrixResponse = response.body();
        return geoMatrixResponse.getDistances();
    }

    private String prepareBody(List<List<Double>> coordinates) {
        List<Integer> sources = new ArrayList<>();
        sources.add(0);

        MatrixPostBody postBody = new MatrixPostBody(
                coordinates,
                sources,
                MatrixPostBody.METRICS.DISTANCE,
                MatrixPostBody.UNITS.KILOMETERS
        );

        Gson gson = new Gson();

        String body = gson.toJson(postBody);

        return body;
    }

}