package com.example.cheaptrip.dao;

import com.example.cheaptrip.handlers.converter.annotations.Json;
import com.example.cheaptrip.models.photon.PhotonResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeoCompletionClient {
    //https://vpic.nhtsa.dot.gov/api/vehicles/getallmakes?format=json
    @Json
    @GET("api")
    Call<PhotonResponse> geoPos(@Query("q") String location,
                                @Query("lat") double latitude,
                                @Query("lon") double longitude,
                                @Query("lang") String language);
    @Json
    @GET("api")
    Call<PhotonResponse> geoPos(@Query("q") String location);

    @Json
    @GET("api")
    Call<PhotonResponse> geoPos(@Query("q") String location,
                                @Query("lat") double latitude,
                                @Query("lon") double longitude);
    @Json
    @GET("api")
    Call<PhotonResponse> geoPos(@Query("q") String location,
                                @Query("lang") String language);
    @Json
    @GET("reverse/")
    Call<PhotonResponse> getLocationName(@Query("lat") double latitude,
                                @Query("lon") double longitude);

    @Json
    @GET("api")
    Call<PhotonResponse> geoPosandWait(@Query("q") String location);

}
