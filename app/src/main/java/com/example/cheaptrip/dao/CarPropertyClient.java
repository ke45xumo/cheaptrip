package com.example.cheaptrip.dao;

import com.example.cheaptrip.models.fueleconomy.FuelEconomyResponse;
import com.example.cheaptrip.models.nhtsa.VehicleModelResponse;

import retrofit2.Call;
import retrofit2.http.GET;

import retrofit2.http.Query;

public interface CarPropertyClient {

    @GET("vehicles")
    Call<FuelEconomyResponse> getProperties(@Query("make") String brand, @Query("model") String model);
}
