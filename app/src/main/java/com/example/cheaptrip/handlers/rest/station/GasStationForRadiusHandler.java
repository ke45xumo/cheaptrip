package com.example.cheaptrip.handlers.rest.station;

import com.example.cheaptrip.dao.GasStationClient;
import com.example.cheaptrip.handlers.rest.RestHandler;
import com.example.cheaptrip.models.tankerkoenig.GasStationResponse;
import com.example.cheaptrip.models.tankerkoenig.Station;

import java.util.List;

import retrofit2.Response;

public class GasStationForRadiusHandler extends RestHandler<List<Station>,GasStationResponse> {

    private final static String BASE_URL = "https://creativecommons.tankerkoenig.de/json/list.php";

    /**
     * TODO: Document
     * @param lon
     * @param lat
     * @param fuelType
     */
    public GasStationForRadiusHandler(double lon, double lat, GasStationClient.FuelType fuelType) {
        super(BASE_URL);

        GasStationClient gasStationClient = retrofit.create(GasStationClient.class);
        call = gasStationClient.GetAllForRadius(lat,lon,25.0, fuelType, GasStationClient.Sort.DISTANCE);
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
}
