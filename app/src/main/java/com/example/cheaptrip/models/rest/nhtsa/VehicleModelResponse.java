package com.example.cheaptrip.models.rest.nhtsa;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


//http://www.jsonschema2pojo.org/
//https://android.jlelse.eu/rest-api-on-android-made-simple-or-how-i-learned-to-stop-worrying-and-love-the-rxjava-b3c2c949cad4
public class VehicleModelResponse extends VehicleProperty{

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
    private List<VehicleModel> results = new ArrayList<VehicleModel>();

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

    public List<VehicleModel> getResults() {
        return results;
    }

    public void setResults(List<VehicleModel> results) {
        this.results = results;
    }

}