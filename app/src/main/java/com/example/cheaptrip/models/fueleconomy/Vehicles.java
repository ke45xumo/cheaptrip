package com.example.cheaptrip.models.fueleconomy;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

@Root(name="vehicles",strict = false)
public class Vehicles
{
    @ElementList(name="vehicle", inline = true)
    private ArrayList<Vehicle> vehicleList;

    public List<Vehicle> getVehicleList()
    {
        return vehicleList;
    }

    public void setVehicleList(ArrayList<Vehicle> vehicleList)
    {
        this.vehicleList = vehicleList;
    }

    @Override
    public String toString()
    {
        return "Vehicles [vehicleList = "+ vehicleList +"]";
    }
}

