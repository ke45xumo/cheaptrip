package com.example.cheaptrip.models.fueleconomy;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FuelEconomyResponse
{
    @SerializedName("vehicles")
    @Expose
    private Vehicles vehicles;

    public Vehicles getVehicles ()
    {
        return vehicles;
    }

    public void setVehicles (Vehicles vehicles)
    {
        this.vehicles = vehicles;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [vehicles = "+vehicles+"]";
    }
}

