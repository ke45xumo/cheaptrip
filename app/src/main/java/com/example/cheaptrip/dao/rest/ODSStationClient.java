package com.example.cheaptrip.dao.rest;

import com.example.cheaptrip.handlers.converter.annotations.Raw;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ODSStationClient {

    @Raw
    @GET("stations_new.json#")
    Call<String> getStationData();
}
