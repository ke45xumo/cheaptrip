package com.example.cheaptrip.models.photon;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "LOCATION")

public class Properties {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name="CITY")
    @SerializedName("city")
    @Expose
    private String city;

    @ColumnInfo(name="COUNTRY")
    @SerializedName("country")
    @Expose
    private String country;

    @ColumnInfo(name="NAME")
    @SerializedName("name")
    @Expose
    private String name;

    @ColumnInfo(name="POSTCODE")
    @SerializedName("postcode")
    @Expose
    private String postcode;

    @ColumnInfo(name="STREET")
    @SerializedName("street")
    @Expose
    private String street;

    @ColumnInfo(name="HOUSENUMBER")
    @SerializedName("housenumber")
    @Expose
    private String housenumber;


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHousenumber() {
        return housenumber;
    }

    public void setHousenumber(String housenumber) {
        this.housenumber = housenumber;
    }
}