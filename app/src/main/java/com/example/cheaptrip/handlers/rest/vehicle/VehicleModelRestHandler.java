package com.example.cheaptrip.handlers.rest.vehicle;

import com.example.cheaptrip.dao.CarSpecClient;

import com.example.cheaptrip.handlers.rest.RestHandler;
import com.example.cheaptrip.models.nhtsa.VehicleModel;
import com.example.cheaptrip.models.nhtsa.VehicleModelResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Response;

public class VehicleModelRestHandler extends RestHandler<List<String> ,VehicleModelResponse> {
    private static final String BASE_URL = "https://vpic.nhtsa.dot.gov/api/vehicles/";

    private String carBrand;

    /**
     * Constructor: Initializes Retrofit and creates an Client for CarSpecClient Class
     */
    public VehicleModelRestHandler(String carBrand){
        super(BASE_URL);
        CarSpecClient carSpecClient = retrofit.create(CarSpecClient.class);
        super.call = carSpecClient.getModels(carBrand,"json");
        this.carBrand = carBrand;
    }

    /**
     * TODO: Document
     * @param response
     * @return
     */
    @Override
    public List<String> extractDataFromResponse(Response<VehicleModelResponse> response){

        VehicleModelResponse vehicleResponseSingle = response.body();
        List<VehicleModel> vehicleModels = vehicleResponseSingle.getResults();


        List<String> modelList = new ArrayList<>();

        for (VehicleModel model: vehicleModels){
            modelList.add(model.getModelName().trim());
        }

        Collections.sort(modelList);

        return modelList;

    }

}

