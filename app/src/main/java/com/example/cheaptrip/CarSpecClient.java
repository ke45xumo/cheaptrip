package com.example.cheaptrip;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CarSpecClient {
    //https://vpic.nhtsa.dot.gov/api/vehicles/getallmakes?format=json
    @GET("getallmakes")
    Call<CarResponse> getResponse(@Query("format") String format);
    //CarResponse getResponse (@Query("format") String format);
    //Single<CarResponse> getResponse(@Query("format") String format);
}
