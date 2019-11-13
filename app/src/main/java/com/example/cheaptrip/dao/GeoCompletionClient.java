package com.example.cheaptrip.dao;

import com.example.cheaptrip.models.retfrofit.photon.PhotonResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GeoCompletionClient {
    //https://vpic.nhtsa.dot.gov/api/vehicles/getallmakes?format=json
    @GET("?limit=4")
    Call<PhotonResponse> geoPos(@Query("q") String location,
                                @Query("lat") int latitude,
                                @Query("long") int longitude,
                                @Query("lang") int language);
    @GET("api")
    Call<PhotonResponse> geoPos(@Query("q") String location);

    @GET("?limit=4")
    Call<PhotonResponse> geoPos(@Query("q") String location,
                                @Query("lang") int language);

    @GET("?limit=4")
    Call<PhotonResponse> geoPos(@Query("q") String location,
                                @Query("lat") int latitude,
                                @Query("long") int longitude);

}
