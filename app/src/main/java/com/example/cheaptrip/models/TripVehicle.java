package com.example.cheaptrip.models;

import java.io.Serializable;

public class TripVehicle implements Serializable {
    final private double TANK_CAPACITY = 20;        // in Litre

    private String brand;                   // Brand of the Vehicle
    private String model;                   // Model of the Vehicle
    private String year;                    // Construction year of the Vehicle
    private double remainFuelPercent;    // remaining Fuel Capacity in Percent

    private double fuelConsumptionCity;
    private double getFuelConsumptionHighway;


    public TripVehicle(){

    }

    public TripVehicle(double tank_capacity, String brand, String model, String year) {
        this.brand = brand;
        this.model = model;
        this.year = year;
    }



    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public double getGetFuelConsumptionHighway() {
        return getFuelConsumptionHighway;
    }

    public void setGetFuelConsumptionHighway(double getFuelConsumptionHighway) {
        this.getFuelConsumptionHighway = getFuelConsumptionHighway;
    }

    public double getFuelConsumptionCity() {
        return fuelConsumptionCity;
    }

    public void setFuelConsumptionCity(double fuelConsumptionCity) {
        this.fuelConsumptionCity = fuelConsumptionCity;
    }

    public double getRemainFuelPercent() {
        return remainFuelPercent;
    }

    public void setRemainFuelPercent(double remainFuelPercent) {
        this.remainFuelPercent = remainFuelPercent;
    }

}
