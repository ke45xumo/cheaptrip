package com.example.cheaptrip.models.orservice;

import java.util.ArrayList;
import java.util.List;

public class MatrixPostBody {

    public enum METRIC {
        DISTANCE("distance"),
        DURATION("duration");

        String metrics;
        METRIC(String metrics) {
            this.metrics = metrics;
        }
    }

    public enum UNITS{
        m("m"),
        km("km");

        String units;
        UNITS (String units) {
            this.units = units;
        }
    }



    private List<List<Double>> locations;
    private List<Integer> sources;
    private List<METRIC> metrics;
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


    public List<METRIC> getMetrics() {
        return metrics;
    }

    public void setMetrics(List<METRIC> metrics) {
        this.metrics = metrics;
    }

    public UNITS getUnits() {
        return units;
    }

    public void setUnits(UNITS units) {
        this.units = units;
    }

    public MatrixPostBody(List<List<Double>> locations, List<Integer> sources, UNITS units) {
        this.locations = locations;
        this.sources = sources;
        this.units = units;

        metrics = new ArrayList<>();
        metrics.add(METRIC.DISTANCE);
        metrics.add(METRIC.DURATION);
    }
}
