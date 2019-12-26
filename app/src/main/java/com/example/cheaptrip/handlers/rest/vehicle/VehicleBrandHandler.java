package com.example.cheaptrip.handlers.rest.vehicle;

import com.example.cheaptrip.handlers.rest.RestListener;
import com.example.cheaptrip.dao.CarSpecClient;
import com.example.cheaptrip.models.nhtsa.VehicleBrand;
import com.example.cheaptrip.models.nhtsa.VehicleBrandResponse;
import com.example.cheaptrip.handlers.rest.RestHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Response;

public class VehicleBrandHandler extends RestHandler<List<VehicleBrand>,VehicleBrandResponse> {
    private static final String BASE_URL = "https://vpic.nhtsa.dot.gov/api/vehicles/";


    /**
     * Constructor: Initializes Retrofit and creates an Client for CarSpecClient Class
     */
    public VehicleBrandHandler(){
        super(BASE_URL);
        CarSpecClient carSpecClient = retrofit.create(CarSpecClient.class);
        super.call = carSpecClient.getBrands("car","json");
    }

    /**
     * TODO: Document
     * @param response
     * @return
     */
    @Override
    public List<VehicleBrand> extractDataFromResponse(Response<VehicleBrandResponse> response) {

        VehicleBrandResponse vehicleResponseSingle = response.body();
        List<VehicleBrand> vehicleBrands = vehicleResponseSingle.getResults();
        return vehicleBrands;

    }

    /**
     * TODO: Document
     *
     */
    @Override
    public void startLoadProperties(final RestListener restListener) {

        super.startLoadProperties(restListener);
    }
}
