package com.example.cheaptrip.models.photon;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PhotonResponse {

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("features")
    @Expose
    private List<Location> locations = new ArrayList<Location>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

}