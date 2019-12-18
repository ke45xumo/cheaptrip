package com.example.cheaptrip.models.orservice; ;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ORServiceResponse {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("bbox")
    @Expose
    private List<Double> bbox = new ArrayList<Double>();
    @SerializedName("features")
    @Expose
    private List<Feature> features = new ArrayList<Feature>();
    @SerializedName("metadata")
    @Expose
    private Metadata metadata;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Double> getBbox() {
        return bbox;
    }

    public void setBbox(List<Double> bbox) {
        this.bbox = bbox;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void setFeatures(List<Feature> features) {
        this.features = features;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

}