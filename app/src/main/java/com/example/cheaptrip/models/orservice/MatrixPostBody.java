package com.example.cheaptrip.models.orservice;

import java.util.List;

public class MatrixPostBody {

    public enum METRICS{
        DISTANCE("distance"),
        DURATION("duration");

        String metrics;
        METRICS(String metrics) {
            this.metrics = metrics;
        }
    }

    public enum UNITS{
        METERS("m"),
        KILOMETERS("km");

        String units;
        UNITS (String units) {
            this.units = units;
        }
    }

    private List<List<Double>> locations;
    private List<Integer> sources;
    private METRICS metrics;
    private UNITS units;


    public List<List<Double>> getLocations() {
        return locations;
    }

    public void setLocations(List<List<Double>> locations) {
        this.locations = locations;
    }

    public List<Integer> getSources() {
        return sources;
    }

    public void setSources(List<Integer> sources) {
        this.sources = sources;
    }


    public METRICS getMetrics() {
        return metrics;
    }

    public void setMetrics(METRICS metrics) {
        this.metrics = metrics;
    }

    public UNITS getUnits() {
        return units;
    }

    public void setUnits(UNITS units) {
        this.units = units;
    }

    public MatrixPostBody(List<List<Double>> locations, List<Integer> sources, METRICS metrics, UNITS units) {
        this.locations = locations;
        this.sources = sources;
        this.metrics = metrics;
        this.units = units;
    }
}
