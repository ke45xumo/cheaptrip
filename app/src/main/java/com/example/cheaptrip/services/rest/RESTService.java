package com.example.cheaptrip.services.rest;

import android.content.Context;

import java.util.List;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * TODO: Document
 * @param <T>
 * @param <V>
 */
public abstract class RESTService<T,V> {
    protected Retrofit retrofit = null;

    /**
     * TODO: Document
     * @param BASE_URL
     */
    public RESTService(String BASE_URL){
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
    public abstract void startLoadProperties(Context context);

    /**
     * Extracts a List of Properties from the Response of the REST-API call
     * called by startLoadProperties().
     *
     * @param response Response from Rest-api of webservice.
     * @return List of extracted desired Properties from the Webservice Response
     */
    public abstract List<T> extractListFromResponse(Response<V> response);
}
