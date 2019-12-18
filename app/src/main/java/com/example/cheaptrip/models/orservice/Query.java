package com.example.cheaptrip.models.orservice; ;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Query {

    @SerializedName("coordinates")
    @Expose
    private List<List<Double>> coordinates = new ArrayList<List<Double>>();
    @SerializedName("profile")
    @Expose
    private String profile;
    @SerializedName("format")
    @Expose
    private String format;
    @SerializedName("units")
    @Expose
    private String units;

    public List<List<Double>> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<List<Double>> coordinates) {
        this.coordinates = coordinates;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

}