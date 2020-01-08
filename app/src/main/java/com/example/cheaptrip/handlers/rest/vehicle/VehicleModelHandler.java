package com.example.cheaptrip.handlers.rest.vehicle;

import com.example.cheaptrip.dao.CarSpecClient;
import com.example.cheaptrip.handlers.rest.RestHandler;
import com.example.cheaptrip.handlers.rest.RestListener;
import com.example.cheaptrip.models.TripVehicle;
import com.example.cheaptrip.models.nhtsa.VehicleModel;
import com.example.cheaptrip.models.nhtsa.VehicleModelResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class VehicleModelHandler extends RestHandler<List<VehicleModel>,VehicleModelResponse> {
    private static final String BASE_URL = "https://vpic.nhtsa.dot.gov/api/vehicles/";
    private static CarSpecClient carSpecClient;
    private List<TripVehicle> tripVehicleList;

    /**
     * Constructor: Initializes Retrofit and creates an Client for CarSpecClient Class
     */
    public VehicleModelHandler(String vehicleBrand){
        super(BASE_URL);
        carSpecClient = super.getRetrofit().create(CarSpecClient.class);
        Call call = carSpecClient.getModels(vehicleBrand, "json");

        super.setCall(call);
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


    public void getVehicleModelList(final RestListener restListener){
        assert (carSpecClient != null);
        super.makeAsyncRequest(restListener);
    }

    public List<VehicleModel> startGetVehicleModelList(String vehicleBrand, final RestListener restListener) {
        assert (carSpecClient != null);
        List<VehicleModel> vehicleModelList = makeSyncRequest();

        return vehicleModelList;
    }
}
