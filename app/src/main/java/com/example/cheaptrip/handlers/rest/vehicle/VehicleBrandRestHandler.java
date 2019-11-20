package com.example.cheaptrip.handlers.rest.vehicle;

import com.example.cheaptrip.handlers.RestListener;
import com.example.cheaptrip.dao.CarSpecClient;
import com.example.cheaptrip.models.rest.nhtsa.VehicleBrand;
import com.example.cheaptrip.models.rest.nhtsa.VehicleBrandResponse;
import com.example.cheaptrip.handlers.rest.RestHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Response;

public class VehicleBrandRestHandler extends RestHandler<List<String>,VehicleBrandResponse> {
    private static final String BASE_URL = "https://vpic.nhtsa.dot.gov/api/vehicles/";


    /**
     * Constructor: Initializes Retrofit and creates an Client for CarSpecClient Class
     */
    public VehicleBrandRestHandler(){
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
    public List<String> extractDataFromResponse(Response<VehicleBrandResponse> response){

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
     *
     */
    @Override
    public void startLoadProperties(final RestListener restListener) {

        super.startLoadProperties(restListener);
    }
}