package com.example.cheaptrip.dao;

import com.example.cheaptrip.models.rest.nhtsa.VehicleBrandResponse;
import com.example.cheaptrip.models.rest.nhtsa.VehicleModelResponse;
import com.example.cheaptrip.models.rest.tankerkoenig.GasStationResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;



public interface GasStationClient {

    final static String API_KEY = "ad5b5cac-db85-4516-832d-1bc90df23946";

    @GET("json/list.php?type=all&sort=dist%apikey="+API_KEY)
    Call<GasStationResponse> GetAllForRadius(@Query("lat") Double lat,
                                             @Query("lng") Double lon,
                                             @Query("rad") Double radius
                                             );

    @GET("GetMakesForVehicleType/{type}")
    Call<VehicleBrandResponse> getBrands(@Path("type") String vehicleType, @Query("format") String format);

    @GET("GetModelsForMake/{brand}")
    Call<VehicleModelResponse> getModels(@Path("brand") String vehicleBrand, @Query("format") String format);

}
