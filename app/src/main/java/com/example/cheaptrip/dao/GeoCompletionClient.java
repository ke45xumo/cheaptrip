package com.example.cheaptrip.dao;

import com.example.cheaptrip.models.rest.photon.PhotonResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeoCompletionClient {
    //https://vpic.nhtsa.dot.gov/api/vehicles/getallmakes?format=json
    @GET("api")
    Call<PhotonResponse> geoPos(@Query("q") String location,
                                @Query("lat") double latitude,
                                @Query("lon") double longitude,
                                @Query("lang") String language);
    @GET("api")
    Call<PhotonResponse> geoPos(@Query("q") String location);

    @GET("api")
    Call<PhotonResponse> geoPos(@Query("q") String location,
                                @Query("lat") double latitude,
                                @Query("lon") double longitude);

    @GET("api")
    Call<PhotonResponse> geoPos(@Query("q") String location,
                                @Query("lang") String language);

    @GET("reverse/")
    Call<PhotonResponse> getLocationName(@Query("lat") double latitude,
                                @Query("lon") double longitude);

    @GET("api")
    Call<PhotonResponse> geoPosandWait(@Query("q") String location);



}
