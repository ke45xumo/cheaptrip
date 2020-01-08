package com.example.cheaptrip.handlers.rest.vehicle;

import android.util.Xml;

import com.example.cheaptrip.activities.CalculationActivity;
import com.example.cheaptrip.dao.CarPropertyClient;
import com.example.cheaptrip.models.TripVehicle;
import com.example.cheaptrip.models.fueleconomy.FuelEconomyResponse;
import com.example.cheaptrip.models.fueleconomy.Vehicle;
import com.example.cheaptrip.models.fueleconomy.Vehicles;

import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;

import java.io.IOException;
import java.util.List;

import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class VehiclePropertyHandler {

    private static String BASE_URL = "https://www.fueleconomy.gov/ws/rest/ympg/shared/";

    private CarPropertyClient carPropertyClient;

    TripVehicle tripVehicle;

    public VehiclePropertyHandler(TripVehicle tripVehicle){
        this.tripVehicle = tripVehicle;

        carPropertyClient = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                //.addConverterFactory(ScalarsConverterFactory.create())
                //.addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())) // rx stuff
                .build()
                .create(CarPropertyClient.class);
    }

    public void startGetProperties(){
        final String vehicleBrand = tripVehicle.getBrand();
        String vehicleModel = tripVehicle.getModel();
        Call<Vehicles> call = carPropertyClient.getProperties(vehicleBrand,vehicleModel);


        call.enqueue(new Callback<Vehicles>() {
            @Override
            public void onResponse(Call<Vehicles> call, Response<Vehicles> response) {
                Vehicles vehicles = response.body();
                List<Vehicle> vehicleList = vehicles.getVehicleList();

                for (Vehicle vehicle : vehicleList){
                    if (vehicle.getYear().equals(tripVehicle.getYear())){
                        double consumptionCity = Double.parseDouble((vehicle.getCity08U()));
                        double consumptionHighway = Double.parseDouble((vehicle.getHighway08U()));

                        tripVehicle.setFuelConsumptionCity(consumptionCity);
                        tripVehicle.setGetFuelConsumptionHighway(consumptionHighway);

                        //CalculationActivity.onVehiclePropertiesLoaded(tripVehicle);

                    }
                }
            }

            @Override
            public void onFailure(Call<Vehicles> call, Throwable t) {

            }

        });

    }

    public Vehicles makeRequest(){
        final String vehicleBrand = tripVehicle.getBrand();
        String vehicleModel = tripVehicle.getModel();
        Call<Vehicles> call = carPropertyClient.getProperties(vehicleBrand,vehicleModel);


        Vehicles vehicles = null;
        try {

            Response<Vehicles> response = call.execute();
            vehicles = response.body();


        } catch (IOException e) {
            e.printStackTrace();
        }

        return vehicles;
    }

    public String getStringResponse(){
        final String vehicleBrand = tripVehicle.getBrand();
        String vehicleModel = tripVehicle.getModel();
        Call<String> call = carPropertyClient.getPropertiesAsString(vehicleBrand,vehicleModel);


        String strResponse = null;
        try {

            Response<String> response = call.execute();
            strResponse = response.body();


        } catch (IOException e) {
            e.printStackTrace();
        }

        return strResponse;
    }
}
