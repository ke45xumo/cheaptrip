package com.example.cheaptrip.models;

import com.example.cheaptrip.models.tankerkoenig.Station;

public class TripGasStation extends TripLocation {

    private double mPriceE5;
    private double mPriceE10;
    private double mPriceDiesel;

    private String mBrand;

    public TripGasStation(Station station) {

        double lat = station.getLat();
        double lon  = station.getLng();
        String name = station.getName();

        String place = station.getPlace();
        String houseNumber = station.getHouseNumber();
        String street = station.getStreet();
        int postcode = station.getPostCode();

        setLatitdue(lat);
        setLongitude(lon);
        setName(name);

        setLocationName(name);
        setStreet(street);
        setHousenumber(houseNumber);
        setPostcode(""+postcode);
        setCity(place);

        mPriceE5 = station.getE5();
        mPriceE10 = station.getE10();
        mPriceDiesel = station.getDiesel();
        mBrand = station.getBrand();
    }

    public double getPriceE5() {
        return mPriceE5;
    }

    public void setPriceE5(double mPriceE5) {
        this.mPriceE5 = mPriceE5;
    }

    public double getPriceE10() {
        return mPriceE10;
    }

    public void setPriceE10(double mPriceE10) {
        this.mPriceE10 = mPriceE10;
    }

    public double getPriceDiesel() {
        return mPriceDiesel;
    }

    public void setPriceDiesel(double mPriceDiesel) {
        this.mPriceDiesel = mPriceDiesel;
    }

    public String getBrand() {
        return mBrand;
    }

    public void setBrand(String mBrand) {
        this.mBrand = mBrand;
    }

}
