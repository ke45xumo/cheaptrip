package com.example.cheaptrip.services.rest;

import android.content.Context;

import com.example.cheaptrip.activities.CarBrandActivity;
import com.example.cheaptrip.dao.CarSpecClient;
import com.example.cheaptrip.models.retfrofit.nhtsa.VehicleBrand;
import com.example.cheaptrip.models.retfrofit.nhtsa.VehicleBrandResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VehicleRestService extends RESTService<String,VehicleBrandResponse> {
    private static final String BASE_URL = "https://vpic.nhtsa.dot.gov/api/vehicles/";
    protected CarSpecClient carSpecClient = null;

    /**
     * Constructor: Initializes Retrofit and creates an Client for CarSpecClient Class
     */
    public VehicleRestService(){
        super(BASE_URL);
        carSpecClient = retrofit.create(CarSpecClient.class);
    }

    /**
     * TODO: Document
     * @param response
     * @return
     */
    @Override
    public List<String> extractListFromResponse(Response<VehicleBrandResponse> response){

        VehicleBrandResponse vehicleResponseSingle = response.body();
        List<VehicleBrand> vehicleBrands = vehicleResponseSingle.getResults();
        List<String> brandList = new ArrayList<>();

        for (VehicleBrand brand: vehicleBrands){
            brandList.add(brand.getMakeName().trim());
        }

        Collections.sort(brandList);

        return brandList;
    }

    /**
     * TODO: Document
     * @param context Context from which the Method is called.
     */
    @Override
    public void startLoadProperties(final Context context) {
        Call<VehicleBrandResponse> carResponseCall = carSpecClient.getBrands("car","json");

        carResponseCall.enqueue(new Callback<VehicleBrandResponse>() {
            @Override
            public void onResponse(Call<VehicleBrandResponse> call, Response<VehicleBrandResponse> response) {
                List<String> brandList = extractListFromResponse(response);
                CarBrandActivity.OnCarListLoadSuccess(context, brandList);
            }

            @Override
            public void onFailure(Call<VehicleBrandResponse> call, Throwable t) {
                CarBrandActivity.OnCarListLoadFail(context);
                t.printStackTrace();
            }
        });
    }
}
