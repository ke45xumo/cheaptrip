package com.example.cheaptrip.dao;

import com.example.cheaptrip.models.orservice.GeoMatrixResponse;
import com.example.cheaptrip.models.orservice.ORServiceResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ORServiceClient {

    @Headers({
            "Authorization: 5b3ce3597851110001cf624809343b3866e54cf488fc96bdabd8b9a2",
            "Accept: application/json, application/geo+json, application/gpx+xml, img/png; charset=utf-8",
            "Content-Type: application/json"
    })
    @POST("directions/driving-car/geojson")
    Call<String> getGeoJson(@Body String body);



    @Headers({
            "Authorization: 5b3ce3597851110001cf624809343b3866e54cf488fc96bdabd8b9a2",
            "Accept: application/json, application/geo+json, application/gpx+xml, img/png; charset=utf-8",
            "Content-Type: application/json"
    })
    @POST("directions/driving-car/geojson")
    Call<ORServiceResponse> getDirections(@Body String body);


    @FormUrlEncoded
    @Headers({
            "Authorization: 5b3ce3597851110001cf624809343b3866e54cf488fc96bdabd8b9a2",
            "Accept: application/json, application/geo+json, application/gpx+xml, img/png; charset=utf-8",
            "Content-Type: application/json"
    })
    @POST("directions/driving-car/geojson")
    Call<ORServiceResponse> getDirections2(@Field("coordinates")List<List<Double>> coordinates,@Field("unit") String unit);


    @FormUrlEncoded
    @Headers({
            "Authorization: 5b3ce3597851110001cf624809343b3866e54cf488fc96bdabd8b9a2",
            "Accept: application/json, application/geo+json, application/gpx+xml, img/png; charset=utf-8",
            "Content-Type: application/json"
    })
    @POST("directions/driving-car/geojson")
    Call<GeoMatrixResponse> getMatrix(@Body String body);

}
