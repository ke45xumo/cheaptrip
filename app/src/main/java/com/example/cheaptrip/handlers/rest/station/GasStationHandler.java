package com.example.cheaptrip.handlers.rest.station;

import android.util.Log;

import com.example.cheaptrip.dao.rest.ODSStationClient;
import com.example.cheaptrip.handlers.rest.RestHandler;
import com.example.cheaptrip.models.TripGasStation;
import com.example.cheaptrip.models.tankerkoenig.ODSResponse;
import com.example.cheaptrip.models.tankerkoenig.Station;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class GasStationHandler extends RestHandler<List<TripGasStation>,ODSResponse> {
    private final static String BASE_URL = "http://data.cheaptrip.cf/";
    private ODSStationClient gasStationClient;

    public GasStationHandler() {
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
    public List<TripGasStation> extractDataFromResponse(Response<ODSResponse> response) {
        ODSResponse responses = response.body();

        if(responses == null ){
            Log.e("CHEAPTRIP","GasStationHandler->extractDataFromResponse: response is null");
            return null;
        }

        List<Station> stations = responses.getData();
        List<TripGasStation> tripGasStationList = new ArrayList<>();

        for(Station station : stations) {
            TripGasStation tripGasStation = new TripGasStation(station);
            tripGasStationList.add(tripGasStation);
        }
        return tripGasStationList;
    }


}
