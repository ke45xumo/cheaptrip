package com.example.cheaptrip.handlers.rest;

import android.util.Log;

import com.example.cheaptrip.handlers.converter.MultiConverterFactory;
import com.example.cheaptrip.models.nhtsa.VehicleBrandResponse;
import com.example.cheaptrip.models.tankerkoenig.Station;

import java.io.IOException;

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
    private Retrofit retrofit; // Refrofit Client
    private Call call;
    /**
     * TODO: Document
     *
     * @param BASE_URL
     */
    public RestHandler(String BASE_URL) {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(new MultiConverterFactory<RestResponse>(Station.class))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

    }

    protected Call getCall(){
        return call;
    }

    protected void setCall(Call call){
        this.call = call;
    }

    protected Retrofit getRetrofit(){
        return retrofit;
    }
    protected void setRetrofit(Retrofit retrofit){
        this.retrofit = retrofit;
    }
    /**
     * Starts the Rest-API Call and waits asynchroniously for the Response
     *
     * @param restListener
     */
    public void makeAsyncRequest(final RestListener restListener) {
        if (restListener == null) {
            Log.e("CHEAPTRIP", "Cannot make asynchronious Rest Request: RestListener is null.");
            return;
        }

        if (call == null) {
            Log.e("CHEAPTRIP", "Cannot make asynchronious Rest Request: Call is null.");
            return;
        }

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
     * Cancels current call (if exists)
      */
    public void cancel(){
        if(call != null){
            call.cancel();
        }
    }
    /**
     * @return
     */
    public RestContent makeSyncRequest() {
        if (call == null) {
            Log.e("CHEAPTRIP", "Cannot make synchronious Rest Request: Call is null.");
            return null;
        }

        RestContent restContent = null;

        try {
            Response<RestResponse> response = call.execute();
            restContent = extractDataFromResponse(response);

        } catch (IOException e) {
            Log.e("CHEAPTRIP", "Error during Rest-Request Execution: " + e.getLocalizedMessage());
            return null;
        }

        return restContent;
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
