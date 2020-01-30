package com.example.cheaptrip.models;

import android.util.Log;

import androidx.annotation.Nullable;

import com.example.cheaptrip.models.orservice.Segment;
import com.example.cheaptrip.models.tankerkoenig.Station;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TripRoute implements Cloneable{
    private List<TripLocation> stops;

    private String geoJSON;
    private double costs;

    private double distance;
    private double duration;

    private List<Segment> routeSegments = new ArrayList<>();

    private List<TripLocation> pointsForPolyLine;                   // Points of the PolyLine to draw


    public TripRoute(){
        stops = new ArrayList<>();
    }


    /**
     * Copy Constructor
     *
     * @param tripRoute tripRoute to be Copied
     */
    public TripRoute(TripRoute tripRoute) {
        this.stops = tripRoute.getStops();
        this.geoJSON = tripRoute.getGeoJSON();
        this.costs = tripRoute.getCosts();
        this.distance = tripRoute.getDistance();
        this.duration = tripRoute.getDuration();
        this.routeSegments = tripRoute.routeSegments;
        this.pointsForPolyLine = tripRoute.getPointsForPolyLine();
    }

    /**
     * Constructor
     * @param stops List of Locations along the Route
     */
    public TripRoute(List<TripLocation> stops){
        this.stops = stops;
    }

    /**
     * Constructor
     * @param stops     List of Locations along the Route
     * @param costs     costs of traveling this route
     * @param distance  distance of this route
     * @param duration  duration of this route
     */
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

    public void setRouteSegments(List<Segment> segments){
        routeSegments = segments;
    }

    public List<Segment> getRouteSegments() {
        return routeSegments;
    }

    public List<TripLocation> getPointsForPolyLine() {
        return pointsForPolyLine;
    }

    public void setPointsForPolyLine(List<TripLocation> pointsForPolyLine) {
        this.pointsForPolyLine = pointsForPolyLine;
    }
}
