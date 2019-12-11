package com.example.cheaptrip.handlers;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.cheaptrip.handlers.rest.geo.GeoNameRestHandler;
import com.example.cheaptrip.services.GPSService;
import com.example.cheaptrip.handlers.rest.geo.GeoNameListRestHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Document
 */
public class LocationTextHandler {
    private double currLatitude;
    private double currLongitude;

    private GeoNameListRestHandler geoNameListRestHandler = null;

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
                geoNameListRestHandler = new GeoNameListRestHandler(s,currLongitude,currLongitude);

                geoNameListRestHandler.startLoadProperties(new RestListener<List<String>>() {
                    @Override
                    public void OnRestSuccess(List<String> locationNames) {
                        ArrayAdapter<String> completeAdapter = new ArrayAdapter<>(completeTextView.getContext(), android.R.layout.select_dialog_item, locationNames);

                        if (completeTextView != null) {
                            completeTextView.setAdapter(completeAdapter);
                            List<String> suggestions = locationNames;
                            completeAdapter.notifyDataSetChanged();
                        }else{
                            Toast.makeText(completeTextView.getContext(),"An Error Occured",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void OnRestFail() {
                        Toast.makeText(completeTextView.getContext(),"An Error Occured",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    /**
     * TODO: Document
     * @param autoCompleteTextView
     */
    public void setCurrentLocation(Context context, final AutoCompleteTextView autoCompleteTextView){
        GPSService gpsService = new GPSService(context);

        if (gpsService.canGetLocation()) {
            currLatitude = gpsService.getLatitude();
            currLongitude = gpsService.getLongitude();
            GeoNameRestHandler geoNameRestHandler = new GeoNameRestHandler(currLatitude,currLongitude);
            geoNameRestHandler.startLoadProperties(new RestListener<String>() {
                @Override
                public void OnRestSuccess(String locationName) {
                    if (autoCompleteTextView != null){
                        autoCompleteTextView.setText(locationName);
                    }else{
                        Toast.makeText(autoCompleteTextView.getContext(),"An Error Occured", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void OnRestFail() {
                    Toast.makeText(autoCompleteTextView.getContext(),"An Error Occured", Toast.LENGTH_LONG).show();
                }
            });

        }else{
            gpsService.showSettingsAlert();
        }
    }

    /**
     * TODO: Document
     * @param locationNames
     * @param completeTextView
     */
    public static void onLoadLocationSuggestionsSuccess(List<String> locationNames, AutoCompleteTextView completeTextView){
        ArrayAdapter<String> completeAdapter = new ArrayAdapter<>(completeTextView.getContext(), android.R.layout.select_dialog_item, locationNames);

        if (completeTextView != null) {
            completeTextView.setAdapter(completeAdapter);
            List<String> suggestions = locationNames;
            completeAdapter.notifyDataSetChanged();
        }else{
            Toast.makeText(completeTextView.getContext(),"An Error Occured",Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Todo: Document
     * @param locationName
     * @param autoCompleteTextView
     */
    public static void onLoadLocationNameSuccess(String locationName, AutoCompleteTextView autoCompleteTextView){
        if (autoCompleteTextView != null){
            autoCompleteTextView.setText(locationName);
        }else{
            Toast.makeText(autoCompleteTextView.getContext(),"An Error Occured", Toast.LENGTH_LONG).show();
        }
    }
}
