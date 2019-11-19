package com.example.cheaptrip.handlers;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.example.cheaptrip.services.GPSService;
import com.example.cheaptrip.services.rest.GeoRestService;

import java.util.List;

public class LocationTextHandler {
    private double currLatitude;
    private double currLongitude;

    private GeoRestService geoRestService = null;

    public LocationTextHandler(double latitude, double longitude){
        currLatitude = latitude;
        currLongitude = longitude;
    }

    /**
     * TODO: Document
     * @param completeTextView
     */
    public void addTextChangedListener(final AutoCompleteTextView completeTextView){
        final Context context = completeTextView.getContext();

        // Listener for the start AutoCompletionTextField
        completeTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //retrieveData(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                geoRestService = new GeoRestService(s,currLongitude,currLongitude, completeTextView);
                geoRestService.startLoadProperties(context);
            }
        });
    }

    /**
     * TODO: Document
     * @param autoCompleteTextView
     */
    public void setCurrentLocation(Context context,AutoCompleteTextView autoCompleteTextView){
        GPSService gpsService = new GPSService(context);

        if (gpsService.canGetLocation()) {
            currLatitude = gpsService.getLatitude();
            currLongitude = gpsService.getLongitude();
            GeoRestService geoRestService = new GeoRestService(null,currLatitude,currLongitude,autoCompleteTextView);

        }else{
            gpsService.showSettingsAlert();
        }
    }

    /**
     * TODO: Document
     * @param locationNames
     * @param completeTextView
     */
    public static void onLoadLocationNameSuccess(List<String> locationNames, AutoCompleteTextView completeTextView){
        ArrayAdapter<String> completeAdapter = new ArrayAdapter<>(completeTextView.getContext(), android.R.layout.select_dialog_item, locationNames);

        if (completeTextView != null) {
            completeTextView.setAdapter(completeAdapter);
            List<String> suggestions = locationNames;
            completeAdapter.notifyDataSetChanged();
        }else{

        }
    }
}
