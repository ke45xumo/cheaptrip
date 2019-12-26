package com.example.cheaptrip.models.fueleconomy;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Vehicles
{
    @SerializedName("vehicleList")
    @Expose
    private List<Vehicle> vehicleList;

    public List<Vehicle> getVehicleList()
    {
        return vehicleList;
    }

    public void setVehicleList(List<Vehicle> vehicleList)
    {
        this.vehicleList = vehicleList;
    }

    @Override
    public String toString()
    {
        return "Vehicles [vehicleList = "+ vehicleList +"]";
    }
}

