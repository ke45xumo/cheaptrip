
package com.example.cheaptrip.models.nhtsa;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
public class VehicleBrand extends VehicleProperty{

    @SerializedName("MakeId")
    @Expose
    @PrimaryKey
    private Integer makeId;

    @SerializedName("MakeName")
    @Expose
    @ColumnInfo
    private String makeName;

    @SerializedName("VehicleTypeId")
    @Expose
    @ColumnInfo
    private Integer vehicleTypeId;

    @SerializedName("VehicleTypeName")
    @Expose
    @ColumnInfo
    private String vehicleTypeName;

    public Integer getMakeId() {
        return makeId;
    }

    public void setMakeId(Integer makeId) {
        this.makeId = makeId;
    }

    public String getMakeName() {
        return makeName;
    }

    public void setMakeName(String makeName) {
        this.makeName = makeName;
    }

    public Integer getVehicleTypeId() {
        return vehicleTypeId;
    }

    public void setVehicleTypeId(Integer vehicleTypeId) {
        this.vehicleTypeId = vehicleTypeId;
    }

    public String getVehicleTypeName() {
        return vehicleTypeName;
    }

    public void setVehicleTypeName(String vehicleTypeName) {
        this.vehicleTypeName = vehicleTypeName;
    }

}