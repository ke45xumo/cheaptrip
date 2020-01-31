package com.example.cheaptrip.handlers.rest.vehicle;

import com.example.cheaptrip.handlers.rest.RestListener;
import com.example.cheaptrip.dao.rest.CarSpecClient;
import com.example.cheaptrip.models.nhtsa.VehicleBrand;
import com.example.cheaptrip.models.nhtsa.VehicleBrandResponse;
import com.example.cheaptrip.handlers.rest.RestHandler;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class VehicleBrandHandler extends RestHandler<List<VehicleBrand>,VehicleBrandResponse> {
    private static final String BASE_URL = "https://vpic.nhtsa.dot.gov/api/vehicles/";
    private static CarSpecClient carSpecClient;

    /**
     * Constructor: Initializes Retrofit and creates an Client for CarSpecClient Class
     */
    public VehicleBrandHandler(){
        super(BASE_URL);
        carSpecClient = super.getRetrofit().create(CarSpecClient.class);

        Call call = carSpecClient.getBrands("car","json");
        super.setCall(call);
    }

    /**
     * Extracts the relevant Data from the Rest-Response (List of Car Brands.
     *
     * @param response Response from Rest-api of webservice (NHTSA).
     * @return List of VehicleBrands (POJO-Objects) holding names for Car Brands.
     */
    @Override
    public List<VehicleBrand> extractDataFromResponse(Response<VehicleBrandResponse> response) {
        VehicleBrandResponse vehicleResponseSingle = response.body();
        List<VehicleBrand> vehicleBrands = vehicleResponseSingle.getResults();

        return vehicleBrands;
    }

    /**
     * Start asynchronious Rest Call. An Instance of RestListener has to be passed.
     * The RestListener's functions OnRestSuccess or OnRestFail will be called when response is received
     * or an error occured.
     *
     * @param restListener Object implementing RestListener.
     *                     Its functions will be called when request is finished.
     */
    public void startGetVehicleBrandList(final RestListener restListener) {
        assert(carSpecClient != null);
        super.makeAsyncRequest(restListener);
    }


    public List<VehicleBrand> getVehicleBrandList(){
        assert(carSpecClient != null);
        List<VehicleBrand> vehicleBrandList = makeSyncRequest();
        return vehicleBrandList;
    }
}
