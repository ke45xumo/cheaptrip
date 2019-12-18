package com.example.cheaptrip.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class TripLocation implements Serializable {
    private String locationName;

    private double latitdue;
    private double longitude;

    public TripLocation(String locationName, double latitdue, double longitude) {
        this.locationName = locationName;
        this.latitdue = latitdue;
        this.longitude = longitude;
    }

    public TripLocation(double latitdue, double longitude){
        this.latitdue = latitdue;
        this.longitude = longitude;
    }
    public TripLocation(){

    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitdue() {
        return latitdue;
    }

    public void setLatitdue(double latitdue) {
        this.latitdue = latitdue;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

}
