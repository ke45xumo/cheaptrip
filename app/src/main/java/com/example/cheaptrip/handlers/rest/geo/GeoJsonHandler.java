package com.example.cheaptrip.handlers.rest.geo;

import com.example.cheaptrip.activities.CalculationActivity;
import com.example.cheaptrip.dao.ORServiceClient;
import com.example.cheaptrip.models.orservice.PostBody;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class GeoJsonHandler {

    private static String BASE_URL = "https://api.openrouteservice.org/v2/";

    List<List<Double>> coordinates;


    private Retrofit retrofit;

    public GeoJsonHandler(List<List<Double>> coordinates){
        this.coordinates = coordinates;


        retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();
    }


    private String formatToJSON(){
        PostBody postBody = new PostBody(coordinates,"km");
        Gson gson = new Gson();
        String json = gson.toJson(postBody);

        return json;
    }

    public void startFindDirections(){
        ORServiceClient orServiceClient = retrofit.create(ORServiceClient.class);

        String body = formatToJSON();
        Call<String> geoPoints = orServiceClient.getGeoJson(body);

        geoPoints.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String geoJSON = response.body();
                CalculationActivity.onDirectionsLoaded(geoJSON);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }


}
