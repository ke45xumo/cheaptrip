package com.example.cheaptrip.models;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TripLocation implements Serializable {
    private String locationName;

    private double latitdue;
    private double longitude;

    public TripLocation(String locationName, double latitdue, double longitude) {
        this.locationName = locationName;
        this.latitdue = latitdue;
        this.longitude = longitude;
    }

    public TripLocation(List<Double> coordinate){
        assert(coordinate.size() == 2);
        assert(coordinate.get(0) != null);
        assert(coordinate.get(1) != null);

        double longitude = coordinate.get(0);
        double latitude = coordinate.get(1);


        this.latitdue = latitude;
        this.longitude = longitude;

        assertInvariants();

    }

    public TripLocation(double latitdue, double longitude){
        assertInvariants();
        this.latitdue = latitdue;
        this.longitude = longitude;
        assertInvariants();
    }

    public TripLocation(){
        assertInvariants();
    }

    public double getLongitude() {
        assertInvariants();
        return longitude;
    }

    public void setLongitude(double longitude) {
        assertInvariants();
        this.longitude = longitude;
        assertInvariants();
    }

    public double getLatitdue() {
        assertInvariants();
        return latitdue;
    }

    public void setLatitdue(double latitdue) {
        assertInvariants();
        this.latitdue = latitdue;
        assertInvariants();
    }

    public String getLocationName() {
        assertInvariants();
        return locationName;
    }

    public void setLocationName(String locationName) {
        assertInvariants();
        this.locationName = locationName;
    }



    public List<Double> getAsDoubleList(){
        assertInvariants();

        List<Double> coordinates = new ArrayList<>();

        coordinates.add(longitude);
        coordinates.add(latitdue);

        assertInvariants();
        return coordinates;
    }

    public static List<TripLocation> getAsTripLocationList(List<List<Double>> coordinates){
        List<TripLocation> tripLocationList = new ArrayList<>();

        if (coordinates != null){
            Log.e("CHEAPTRIP","getAsTripLocationList(): cannot convert List: List is null");
            return null;
        }

        if (coordinates.size() == 0){
            Log.w("CHEAPTRIP", "getAsTripLocationList(): Converting empty List");
        }

        for(List<Double> coordinate : coordinates){
            TripLocation tripLocation = new TripLocation(coordinate);
            tripLocationList.add(tripLocation);
        }

        return tripLocationList;
    }

    public static List<List<Double>> getAsDoubleList(List<TripLocation> tripLocations){
        List<List<Double>> coordinateList = new ArrayList<>();

        if (tripLocations != null){
            Log.e("CHEAPTRIP","getAsDoubleList(): cannot convert List: List is null");
            return null;
        }

        if (tripLocations.size() == 0){
            Log.w("CHEAPTRIP", "getAsDoubleList(): Converting empty List");
        }

        for(TripLocation tripLocation : tripLocations){

            List<Double> doubleList = new ArrayList<>();
            doubleList.add(tripLocation.getLongitude());
            doubleList.add(tripLocation.getLatitdue());

            coordinateList.add(doubleList);

        }

        return coordinateList;
    }


    public static List<List<Double>> getAsDoubleList(TripLocation ... tripLocations ){
        List<List<Double>> coordinateList = new ArrayList<>();

        if (tripLocations == null){
            Log.e("CHEAPTRIP","getAsDoubleList(): cannot convert List: List is null");
            return null;
        }


        for(TripLocation tripLocation : tripLocations){

            List<Double> doubleList = new ArrayList<>();
            doubleList.add(tripLocation.getLongitude());
            doubleList.add(tripLocation.getLatitdue());

            coordinateList.add(doubleList);

        }

        return coordinateList;
    }

    private void assertInvariants(){
        assert (longitude >= 0);
        assert (longitude >= 0);
    }
}
