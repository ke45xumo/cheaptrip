package com.example.cheaptrip.models.orservice;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GeoMatrixResponse {

    @SerializedName("distances")
    @Expose
    private List<List<Double>> distances = new ArrayList<List<Double>>();
    @SerializedName("destinations")
    @Expose
    private List<Destination> destinations = new ArrayList<Destination>();
    @SerializedName("sources")
    @Expose
    private List<Source> sources = new ArrayList<Source>();
    @SerializedName("metadata")
    @Expose
    private Metadata metadata;

    public List<List<Double>> getDistances() {
        return distances;
    }

    public void setDistances(List<List<Double>> distances) {
        this.distances = distances;
    }

    public List<Destination> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<Destination> destinations) {
        this.destinations = destinations;
    }

    public List<Source> getSources() {
        return sources;
    }

    public void setSources(List<Source> sources) {
        this.sources = sources;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

}