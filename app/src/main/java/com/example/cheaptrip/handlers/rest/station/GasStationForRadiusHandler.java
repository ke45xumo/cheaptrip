package com.example.cheaptrip.handlers.rest.station;

import com.example.cheaptrip.dao.GasStationClient;
import com.example.cheaptrip.handlers.rest.RestHandler;
import com.example.cheaptrip.models.TripLocation;
import com.example.cheaptrip.models.tankerkoenig.GasStationResponse;
import com.example.cheaptrip.models.tankerkoenig.Station;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class GasStationForRadiusHandler extends RestHandler<List<Station>,GasStationResponse> {
    // TODO: https://dev.azure.com/tankerkoenig/362e70d1-bafa-4cf7-a346-1f3613304973/_apis/git/repositories/0d6e7286-91e4-402c-af56-fa75be1f223d/items?path=%2Fprices%2F2019%2F12&versionType=Branch
    private final static String BASE_URL = "https://creativecommons.tankerkoenig.de/";
    private final static String API_KEY = "ad5b5cac-db85-4516-832d-1bc90df23946";

    private GasStationClient gasStationClient;
    private List<Station> stationList;

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
    public List<Station> extractDataFromResponse(Response<GasStationResponse> response) {
        GasStationResponse gasStationResponse = response.body();

        List<Station> stations = gasStationResponse.getStations();

        return stations;
    }

    public List<Station> getStations(){
        stationList = makeSyncRequest();

        return stationList;
    }

    public String getJsonResponse(){
        Gson gson = new Gson();
        if (stationList == null) {
            stationList = getStations();
        }

        String result = gson.toJson(stationList);

        return  result;
    }
}
