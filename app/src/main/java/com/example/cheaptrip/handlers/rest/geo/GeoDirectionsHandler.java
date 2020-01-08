package com.example.cheaptrip.handlers.rest.geo;

import android.util.Log;

import com.example.cheaptrip.dao.ORServiceClient;
import com.example.cheaptrip.handlers.rest.RestHandler;
import com.example.cheaptrip.models.TripLocation;
import com.example.cheaptrip.models.orservice.ORServiceResponse;
import com.example.cheaptrip.models.orservice.PostBody;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class GeoDirectionsHandler extends RestHandler<ORServiceResponse,String> {
    private static final String BASE_URL = "https://api.openrouteservice.org/v2/";

    private List<TripLocation> tripLocationList;

    public GeoDirectionsHandler(List<TripLocation> tripLocationList) {
        super(BASE_URL);
        this.tripLocationList = tripLocationList;

        ORServiceClient orServiceClient = super.getRetrofit().create(ORServiceClient.class);

        String requestBody = generateBody(tripLocationList);
        Call<String> call = orServiceClient.getGeoJson(requestBody);
        super.setCall(call);
    }


    @Override
    public ORServiceResponse extractDataFromResponse(Response<String> response) {
        String geoJSON = response.body();
        Gson gson = new Gson();

        ORServiceResponse orServiceResponse = gson.fromJson(geoJSON,ORServiceResponse.class);

        return orServiceResponse;
    }

    private String generateBody(List<TripLocation> tripLocationList){

        if(tripLocationList == null){
            Log.e("CHEAPTRIP", "Cannot create Body: tripLocationList is null");
            return null;
        }

        List<List<Double>> coordinates = TripLocation.getAsDoubleList(tripLocationList);

        PostBody postBody = new PostBody(coordinates,"km");
        Gson gson = new Gson();
        String json = gson.toJson(postBody);

        return json;
    }

}
