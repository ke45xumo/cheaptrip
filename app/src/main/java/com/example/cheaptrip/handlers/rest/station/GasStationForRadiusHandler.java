package com.example.cheaptrip.handlers.rest.station;

import com.example.cheaptrip.dao.rest.GasStationClient;
import com.example.cheaptrip.handlers.rest.RestHandler;
import com.example.cheaptrip.models.TripGasStation;
import com.example.cheaptrip.models.tankerkoenig.GasStationResponse;
import com.example.cheaptrip.models.tankerkoenig.Station;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class GasStationForRadiusHandler extends RestHandler<List<TripGasStation>,GasStationResponse> {
    // TODO: https://dev.azure.com/tankerkoenig/362e70d1-bafa-4cf7-a346-1f3613304973/_apis/git/repositories/0d6e7286-91e4-402c-af56-fa75be1f223d/items?path=%2Fprices%2F2019%2F12&versionType=Branch
    private final static String BASE_URL = "https://creativecommons.tankerkoenig.de/";
    private final static String API_KEY = "ad5b5cac-db85-4516-832d-1bc90df23946";


    public GasStationForRadiusHandler(double lat, double lon,  GasStationClient.FuelType fuelType) {
        super(BASE_URL);
        GasStationClient gasStationClient = super.getRetrofit().create(GasStationClient.class);
        Call call = gasStationClient.getStationsInRadius(lat,lon,25.0, "all", "dist");

        super.setCall(call);
    }

    /**
     * TODO: Document
     * @param response Response from Rest-api of webservice.
     * @return
     */
    @Override
    public List<TripGasStation> extractDataFromResponse(Response<GasStationResponse> response) {
        GasStationResponse gasStationResponse = response.body();

        List<TripGasStation> tripGasStationList = new ArrayList<>();


        List<Station> stations = gasStationResponse.getStations();

        for(Station station : stations){
            tripGasStationList.add(new TripGasStation(station));
        }
        return tripGasStationList;
    }


}
