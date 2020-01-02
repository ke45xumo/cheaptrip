package com.example.cheaptrip.handlers.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.example.cheaptrip.handlers.rest.RestListener;
import com.example.cheaptrip.handlers.rest.geo.GeoNameRestHandler;
import com.example.cheaptrip.models.TripLocation;
import com.example.cheaptrip.services.GPSService;
import com.example.cheaptrip.handlers.rest.geo.GeoNameListRestHandler;

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

                geoNameListRestHandler.makeAsyncRequest(new RestListener<List<String>>() {
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
    public void setCurrentLocation(Context context, final AutoCompleteTextView autoCompleteTextView, final TripLocation tripLocation){
        GPSService gpsService = new GPSService(context);

        if (gpsService.canGetLocation()) {
            currLatitude = gpsService.getLatitude();
            currLongitude = gpsService.getLongitude();
            GeoNameRestHandler geoNameRestHandler = new GeoNameRestHandler(currLatitude,currLongitude);


            geoNameRestHandler.makeAsyncRequest(new RestListener<String>() {
                @Override
                public void OnRestSuccess(String locationName) {
                    if (autoCompleteTextView != null){
                        //autoCompleteTextView.setText(locationName);
                        autoCompleteTextView.setHint(locationName);
                    }else{
                        Toast.makeText(autoCompleteTextView.getContext(),"An Error Occured", Toast.LENGTH_LONG).show();
                    }

                    if (tripLocation != null){
                        tripLocation.setLocationName(locationName);
                        tripLocation.setLatitdue(currLatitude);
                        tripLocation.setLongitude(currLongitude);
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
}
