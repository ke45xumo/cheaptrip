package com.example.cheaptrip.handlers.rest;

import com.example.cheaptrip.handlers.RestListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * TODO: Document
 * @param <RestContent>
 * @param <RestResponse>
 */
public abstract class RestHandler<RestContent, RestResponse> {
    protected Retrofit retrofit = null; // Refrofit Client
    protected Call<RestResponse> call;             // REST-Api Call to be called

    /**
     * TODO: Document
     * @param BASE_URL
     */
    public RestHandler(String BASE_URL){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

    }

    /**
     * Starts the Rest-API Call and waits asynchroniously for the Response
     * @param context Context from which the Method is called.
     *                (This is for calling some Callback methods as soon the Response got received).
     */
    public void startLoadProperties(final RestListener restListener){
        call.enqueue(new Callback<RestResponse>() {
            @Override
            public void onResponse(Call<RestResponse> call, Response<RestResponse> response) {
                RestContent responseData = extractDataFromResponse(response);
                restListener.OnRestSuccess(responseData);
            }

            @Override
            public void onFailure(Call<RestResponse> call, Throwable t) {
                restListener.OnRestFail();
                t.printStackTrace();
            }
        });
    }


    /**
     * Extracts a List of Properties from the Response of the REST-API call
     * called by startLoadProperties().
     *
     * @param response Response from Rest-api of webservice.
     * @return List of extracted desired Properties from the Webservice Response
     */
    public abstract RestContent extractDataFromResponse(Response<RestResponse> response);
}
