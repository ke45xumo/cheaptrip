package com.example.cheaptrip.views;

import android.view.View;
import android.widget.TextView;

import com.example.cheaptrip.R;

import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

public class TripInfoWindow extends InfoWindow {

    private TextView tvLocation;
    public TripInfoWindow(String locationText, MapView mapView) {
        super(R.layout.map_info_window, mapView);
        tvLocation = super.mView.findViewById(R.id.tv_curr_location);
        tvLocation.setText(locationText);

    }

    public void setText(String locationName){

        tvLocation.setText(locationName);
        tvLocation.invalidate();

        // Workaround for wrapping Content (Fitting the box around)
        int visibility = tvLocation.getVisibility();
        tvLocation.setVisibility(View.GONE);
        tvLocation.setVisibility(visibility);

        visibility = mView.getVisibility();
        mView.setVisibility(View.GONE);
        mView.setVisibility(visibility);

    }
    @Override
    public void onOpen(Object item) {

    }

    @Override
    public void onClose() {

    }
}
