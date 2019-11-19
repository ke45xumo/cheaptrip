package com.example.cheaptrip.services.rest;

import android.content.Context;
import android.text.Editable;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.cheaptrip.dao.GeoCompletionClient;
import com.example.cheaptrip.handlers.LocationTextHandler;
import com.example.cheaptrip.models.retfrofit.photon.Feature;
import com.example.cheaptrip.models.retfrofit.photon.Geometry;
import com.example.cheaptrip.models.retfrofit.photon.PhotonResponse;
import com.example.cheaptrip.models.retfrofit.photon.Properties;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * TODO: Document
 */
public class GeoRestService extends RESTService<String,PhotonResponse> {
    private final static String BASE_URL = "http://photon.komoot.de/";

    private GeoCompletionClient geoCompletionClient = null;
    private Call<PhotonResponse> photonResponseCall = null;
    private AutoCompleteTextView autoCompleteTextView = null;

    /**
     * TODO: Document
     *
     */
    public GeoRestService(Editable enteredText, double lat, double lon, AutoCompleteTextView autoCompleteTextView) {
        super(BASE_URL);
        this.autoCompleteTextView = autoCompleteTextView;
        geoCompletionClient = retrofit.create(GeoCompletionClient.class);
        photonResponseCall = geoCompletionClient.geoPos(enteredText.toString(),lat,lon);
    }

    /**
     *
     * @param context Context from which the Method is called.
     */
    public void startLoadProperties(final Context context) {

        photonResponseCall.enqueue(new Callback<PhotonResponse>() {
            @Override
            public void onResponse(Call<PhotonResponse> call, Response<PhotonResponse> response) {
                List<String> locationsNames = extractListFromResponse(response);
                LocationTextHandler.onLoadLocationNameSuccess(locationsNames,autoCompleteTextView);
            }

            @Override
            public void onFailure(Call<PhotonResponse> call, Throwable t) {
                Toast.makeText(context,"An Error Occurred", Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }

    /**
     *
     * @param response Response from Rest-api of webservice.
     * @return
     */
    @Override
    public List<String> extractListFromResponse(Response<PhotonResponse> response) {
        PhotonResponse photonResponse = response.body();
        List<Feature> features = photonResponse.getFeatures();

        List<String> locationNames = new ArrayList();

        for (Feature feature : features){
            Geometry geometry = feature.getGeometry();
            Properties properties = feature.getProperties();

            String name = properties.getName();
            String country = properties.getCountry();

            String textField = name + "(" + country + ")";

            locationNames.add(textField);
        }

        return locationNames;
    }
}
