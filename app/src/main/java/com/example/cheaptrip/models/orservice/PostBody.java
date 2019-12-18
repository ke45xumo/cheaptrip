package com.example.cheaptrip.models.orservice;

import java.util.List;

public class PostBody {
    private List<List<Double>> coordinates;
    private String units;

    public PostBody(List<List<Double>> coordinates,String units) {
        this.units = units;
        this.coordinates = coordinates;
    }


}
