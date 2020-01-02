package com.example.cheaptrip.models.tankerkoenig;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


@Entity
public class Station {

    @SerializedName("id")
    @Expose
    @ColumnInfo(name="ID")
    private String id;

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("brand")
    @Expose
    private String brand;
    @SerializedName("street")
    @Expose
    private String street;
    @SerializedName("place")
    @Expose
    private String place;
    @SerializedName("lat")
    @Expose
    private Double lat;
    @SerializedName("lng")
    @Expose
    private Double lng;
    @SerializedName("dist")
    @Expose
    private Double dist;
    @SerializedName("diesel")
    @Expose
    private Double diesel;
    @SerializedName("e5")
    @Expose
    private Double e5;
    @SerializedName("e10")
    @Expose
    private Double e10;
    @SerializedName("isOpen")
    @Expose
    private Boolean isOpen;
    @SerializedName("houseNumber")
    @Expose
    private String houseNumber;
    @SerializedName("postCode")
    @Expose
    private Integer postCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getDist() {
        return dist;
    }

    public void setDist(Double dist) {
        this.dist = dist;
    }

    public Double getDiesel() {
        return diesel;
    }

    public void setDiesel(Double diesel) {
        this.diesel = diesel;
    }

    public Double getE5() {
        return e5;
    }

    public void setE5(Double e5) {
        this.e5 = e5;
    }

    public Double getE10() {
        return e10;
    }

    public void setE10(Double e10) {
        this.e10 = e10;
    }

    public Boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(Boolean isOpen) {
        this.isOpen = isOpen;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public Integer getPostCode() {
        return postCode;
    }

    public void setPostCode(Integer postCode) {
        this.postCode = postCode;
    }

}