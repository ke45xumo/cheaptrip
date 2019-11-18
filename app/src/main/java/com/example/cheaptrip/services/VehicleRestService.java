package com.example.cheaptrip.services;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

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
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class VehicleRestService {
    private static final String BASE_URL = "https://vpic.nhtsa.dot.gov/api/vehicles/";
    protected Retrofit retrofit = null;

    public VehicleRestService(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    //TODO: RESTCALLS


    public void startGetVehicleBrands(final Context context){

        CarSpecClient carSpecClient = retrofit.create(CarSpecClient.class);
        Call<VehicleBrandResponse> carResponseCall = carSpecClient.getBrands("car","json");

        carResponseCall.enqueue(new Callback<VehicleBrandResponse>() {
            @Override
            public void onResponse(Call<VehicleBrandResponse> call, Response<VehicleBrandResponse> response) {
                VehicleBrandResponse vehicleResponseSingle = response.body();
                List<VehicleBrand> vehicleBrands = vehicleResponseSingle.getResults();
                List<String> brandList = new ArrayList<>();

                for (VehicleBrand brand: vehicleBrands){
                    brandList.add(brand.getMakeName().trim());
                }

                Collections.sort(brandList);

                CarBrandActivity.OnCarListLoadSuccess(context, brandList);

                //generateVehicleBrandList(vehicleBrands);
            }

            @Override
            public void onFailure(Call<VehicleBrandResponse> call, Throwable t) {
                //progressBar.setVisibility(View.INVISIBLE);
                //Toast.makeText(getApplicationContext(),"An Error Occurred", Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }

}
