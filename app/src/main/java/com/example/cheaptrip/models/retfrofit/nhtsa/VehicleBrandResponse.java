package com.example.cheaptrip.models.retfrofit.nhtsa;

import java.util.ArrayList;
import java.util.List;

import com.example.cheaptrip.models.retfrofit.nhtsa.VehicleBrand;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


//http://www.jsonschema2pojo.org/
//https://android.jlelse.eu/rest-api-on-android-made-simple-or-how-i-learned-to-stop-worrying-and-love-the-rxjava-b3c2c949cad4
public class VehicleBrandResponse extends VehicleProperty{

    @SerializedName("Count")
    @Expose
    private Integer count;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("SearchCriteria")
    @Expose
    private Object searchCriteria;
    @SerializedName("Results")
    @Expose
    private List<VehicleBrand> results = new ArrayList<VehicleBrand>();

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(Object searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public List<VehicleBrand> getResults() {
        return results;
    }

    public void setResults(List<VehicleBrand> results) {
        this.results = results;
    }

}