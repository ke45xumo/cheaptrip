package com.example.cheaptrip.models.fueleconomy;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "VEHICLES")
public class VehicleDataSet  implements Comparable<VehicleDataSet>{
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "UID")
    private long vehicleID;

    @ColumnInfo(name = "YEAR")
    private String constructionYear;

    @ColumnInfo(name = "BRAND")
    private String vehicleBrand;

    @ColumnInfo(name = "MODEL")
    private String vehicleModel;

    @ColumnInfo(name = "REGULAR")
    private Double consumption_regular;

    @ColumnInfo(name = "PREMIUM")
    private Double consumption_premium;

    @ColumnInfo(name = "DIESEL")
    private Double consumption_diesel;

    public VehicleDataSet(String constructionYear,
                          String vehicleBrand,
                          String vehicleModel,
                          Double consumption_regular,
                          Double consumption_premium,
                          Double consumption_diesel)
    {
        this.constructionYear = constructionYear;
        this.vehicleBrand = vehicleBrand;
        this.vehicleModel = vehicleModel;
        this.consumption_regular = consumption_regular;
        this.consumption_premium = consumption_premium;
        this.consumption_diesel = consumption_diesel;
    }

    public String getConstructionYear() {
        return constructionYear;
    }

    public void setConstructionYear(String constructionYear) {
        this.constructionYear = constructionYear;
    }

    public String getVehicleBrand() {
        return vehicleBrand;
    }

    public void setVehicleBrand(String vehicleBrand) {
        this.vehicleBrand = vehicleBrand;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public Double getConsumption_regular() {
        return consumption_regular;
    }

    public Double getConsumption_premium() {
        return consumption_premium;
    }

    public Double getConsumption_diesel() {
        return consumption_diesel;
    }

    public long getVehicleID() {
        return vehicleID;
    }

    public void setVehicleID(long vehicleID) {
        this.vehicleID = vehicleID;
    }


    @Override
    public int compareTo(VehicleDataSet vehicleDataSet) {
        if(vehicleDataSet == null){
            return 1;
        }

        String thisDataString =
                getConstructionYear() + getVehicleBrand() + getVehicleModel() +
                        getConsumption_diesel() + getConsumption_premium() + getConsumption_regular();

        String dataString2 =
                vehicleDataSet.getConstructionYear() + vehicleDataSet.getVehicleBrand() + vehicleDataSet.getVehicleModel() +
                        vehicleDataSet.getConsumption_diesel() + vehicleDataSet.getConsumption_premium() + vehicleDataSet.getConsumption_regular();


        return thisDataString.compareTo(dataString2);

    }


}
