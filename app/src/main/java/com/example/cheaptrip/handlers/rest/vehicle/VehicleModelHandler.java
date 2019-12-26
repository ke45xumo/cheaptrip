package com.example.cheaptrip.handlers.rest.vehicle;

import com.example.cheaptrip.dao.CarSpecClient;
import com.example.cheaptrip.handlers.rest.RestHandler;
import com.example.cheaptrip.handlers.rest.RestListener;
import com.example.cheaptrip.models.nhtsa.VehicleModel;
import com.example.cheaptrip.models.nhtsa.VehicleModelResponse;

import java.util.List;

import retrofit2.Response;

public class VehicleModelHandler extends RestHandler<List<VehicleModel>,VehicleModelResponse> {
    private static final String BASE_URL = "https://vpic.nhtsa.dot.gov/api/vehicles/";


    /**
     * Constructor: Initializes Retrofit and creates an Client for CarSpecClient Class
     */
    public VehicleModelHandler(String brand){
        super(BASE_URL);
        CarSpecClient carSpecClient = retrofit.create(CarSpecClient.class);
        super.call = carSpecClient.getModels(brand,"json");
    }

    /**
     * TODO: Document
     * @param response
     * @return
     */
    @Override
    public List<VehicleModel> extractDataFromResponse(Response<VehicleModelResponse> response) {

        VehicleModelResponse vehicleResponseSingle = response.body();
        List<VehicleModel> vehicleModels = vehicleResponseSingle.getResults();
        return vehicleModels;

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
