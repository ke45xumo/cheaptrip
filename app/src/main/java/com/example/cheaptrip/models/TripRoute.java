package com.example.cheaptrip.models;

import java.util.List;

public class TripRoute {
    private List<TripLocation> stops;
    private double costs;

    private double distance;
    private double duration;

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
}
