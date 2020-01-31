package com.example.cheaptrip.handlers.rest.station;

import com.example.cheaptrip.dao.rest.GasStationClient;
import com.example.cheaptrip.handlers.converter.CsvConverter;
import com.example.cheaptrip.handlers.rest.RestHandler;
import com.example.cheaptrip.models.tankerkoenig.Station;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class GasStationHistoryPriceHandler extends RestHandler<List<Station>,List<Station>> {
    private final static String BASE_URL = "https://dev.azure.com/tankerkoenig/362e70d1-bafa-4cf7-a346-1f3613304973/_apis/git/repositories/0d6e7286-91e4-402c-af56-fa75be1f223d/";
//    private final static String BASE_URL = "https://dev.azure.com/tankerkoenig/362e70d1-bafa-4cf7-a346-1f3613304973/_apis/git/repositories/0d6e7286-91e4-402c-af56-fa75be1f223d/";

    private static GasStationClient gasStationClient;

    public GasStationHistoryPriceHandler(int year, int month, int day) {
        super(BASE_URL);
        gasStationClient = super.getRetrofit().create(GasStationClient.class);
        String path = buildPath(year,month,day);
        Call call = gasStationClient.getHistory(path);
        super.setCall(call);
    }


    /**
     * TODO: Document
     * @param response Response from Rest-api of webservice.
     * @return
     */
    @Override
    public List<Station> extractDataFromResponse(Response<List<Station>> response) {
        List<Station> stations = response.body();

        return stations;
    }


    public List<Station> getHistory(int year, int month,  int day){
        List<Station> stationList = null;
        String priceHistory = null;

        String path = buildPath(year,month,day);
        Call call = gasStationClient.getHistoryAsString(path);

        try {

            Response<String> response = call.execute();
            priceHistory = response.body();

            CsvConverter csvConverter = new CsvConverter(Station.class);
            stationList = csvConverter.convert(priceHistory);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return  stationList;
    }

    public String getHistoryAsString(int year, int month,  int day) {
        String priceHistory = null;

        String path = buildPath(year,month,day);
        Call call = gasStationClient.getHistoryAsString(path);

        try {

            Response<String> response = call.execute();
            priceHistory = response.body();


        } catch (IOException e) {
            e.printStackTrace();
        }
        return priceHistory;
    }

    private String buildPath(int year, int month, int day){
        String strMonth = (month < 10) ? "0"+month : ""+month;
        String strDay = (day < 10) ? "0"+day: ""+day;

        String path = String.format("prices/%d/%s/%d-%s-%s-prices.csv", year,strMonth,year,strMonth,strDay);
        return path;
    }
}
