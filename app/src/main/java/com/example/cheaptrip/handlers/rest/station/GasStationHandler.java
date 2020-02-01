package com.example.cheaptrip.handlers.rest.station;

import android.util.Log;

import com.example.cheaptrip.dao.rest.ODSStationClient;
import com.example.cheaptrip.handlers.rest.RestHandler;
import com.example.cheaptrip.models.TripGasStation;
import com.example.cheaptrip.models.tankerkoenig.Station;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class GasStationHandler extends RestHandler<List<TripGasStation>,List<Station>> {
    private final static String BASE_URL = "https://dev.azure.com/tankerkoenig/362e70d1-bafa-4cf7-a346-1f3613304973/_apis/git/repositories/0d6e7286-91e4-402c-af56-fa75be1f223d/";
    private ODSStationClient gasStationClient;

    public GasStationHandler(int year, int month,  int day) {
        super(BASE_URL);

        gasStationClient = super.getRetrofit().create(ODSStationClient.class);
        Call call = gasStationClient.getStationData();
        super.setCall(call);
    }

    /**
     * TODO: Document
     * @param response Response from Rest-api of webservice.
     * @return
     */
    @Override
    public List<TripGasStation> extractDataFromResponse(Response<List<Station>> response) {
        List<Station> stations = response.body();

        if(stations == null || stations.isEmpty()){
            Log.e("CHEAPTRIP","GasStationHandler->extractDataFromResponse: stations is null");
            return null;
        }

        List<TripGasStation> tripGasStationList = new ArrayList<>();

        for(Station station : stations) {
            TripGasStation tripGasStation = new TripGasStation(station);
            tripGasStationList.add(tripGasStation);
        }
        return tripGasStationList;
    }


}
