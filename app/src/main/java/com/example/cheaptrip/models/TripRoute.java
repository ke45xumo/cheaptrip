package com.example.cheaptrip.models;

import android.util.Log;

import androidx.annotation.Nullable;

import com.example.cheaptrip.models.tankerkoenig.Station;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TripRoute {
    private List<TripLocation> stops;
    private List<Station> gasStationList;

    private String geoJSON;
    private double costs;

    private double distance;
    private double duration;

    public TripRoute(){
        stops = new ArrayList<>();
    }


    List<HashMap<String,List<String>>> routeInfosList;

    public TripRoute(List<TripLocation> stops){
        this.stops = stops;
    }

    public TripRoute(List<TripLocation> stops, double costs, double distance, double duration) {
        this.stops = stops;
        this.costs = costs;
        this.distance = distance;
        this.duration = duration;
    }


    public List<TripLocation> getStops() {
        return stops;
    }

    public void setStops(List<TripLocation> stops) {
        this.stops = stops;
    }

    public double getCosts() {
        return costs;
    }

    public void setCosts(double costs) {
        this.costs = costs;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public void insertTripLocation(TripLocation tripLocation, int index){
        if (tripLocation == null){
            Log.e("CHEAPTRIP","Cannot insert tripLocation to List: tripLocation is null");
            return ;
        }

        // append if index out of range
        if(index < 0 || index > stops.size() -1){
            addTripLocation(tripLocation);
        }else{
            stops.add(index,tripLocation);
        }
    }

    public void addTripLocation(TripLocation ... tripLocations){
        List<TripLocation> backup = new ArrayList<>(stops);

        if (tripLocations == null){
            Log.e("CHEAPTRIP","Cannot insert tripLocation to List: tripLocations is null");
            return ;
        }
        for(TripLocation tripLocation : tripLocations){
            if (tripLocation == null){
                Log.e("CHEAPTRIP","Cannot insert tripLocation to List: tripLocation is null");
                stops = backup;
                return ;
            }
            stops.add(tripLocation);
        }

    }

    public String getGeoJSON() {
        return geoJSON;
    }

    public void setGeoJSON(String geoJSON) {
        this.geoJSON = geoJSON;
    }
}
