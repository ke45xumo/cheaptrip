package com.example.cheaptrip.handlers.rest.geo;

import android.util.Log;

import com.example.cheaptrip.dao.GeoCompletionClient;
import com.example.cheaptrip.handlers.rest.RestHandler;
import com.example.cheaptrip.models.photon.Location;
import com.example.cheaptrip.models.photon.PhotonResponse;
import com.example.cheaptrip.models.photon.Properties;


import retrofit2.Call;
import retrofit2.Response;

public class GeoNameForLocationHandler extends RestHandler<String,PhotonResponse> {
    private final static String BASE_URL = "http://photon.komoot.de/";
    private static GeoCompletionClient geoCompletionClient;

    public GeoNameForLocationHandler(double lat, double lon){
        super(BASE_URL);

        geoCompletionClient = super.getRetrofit().create(GeoCompletionClient.class);
        Call call = geoCompletionClient.getLocationName(lat,lon);
        super.setCall(call);
    }

    /**
     * TODO:Document
     * @param response Response from Rest-api of webservice.
     * @return
     */
    @Override
    public String extractDataFromResponse(Response<PhotonResponse> response) {
        PhotonResponse photonResponse = response.body();

        if(photonResponse == null){
            Log.e("CHEAPTRIP", "Cannot extract Name for Location: REST-Response is null");
            return null;
        }

        if(photonResponse.getLocations() == null){
            Log.e("CHEAPTRIP", "Cannot extract Name for Location: REST-Response has no Locations.");
            return null;
        }

        if(photonResponse.getLocations().size() < 1){
            Log.e("CHEAPTRIP", "Cannot extract Name for Location: REST-Response has empty LocationList.");
            return null;
        }

        Location location = photonResponse.getLocations().get(0);
        Properties properties = location.getProperties();


        /*====================================================
         * Extract Values
         *====================================================*/
        String postCode = properties.getPostcode();
        if (postCode == null || postCode.length() == 0){
            postCode = "";
        }


        String city = properties.getCity();
        if (city == null || city.length() == 0){
            city = "";
        }{
            city = postCode + " " + city;
        }

        String locationName = properties.getName();
        if (locationName == null || locationName.length() == 0){
            locationName = "";
        }else{
            locationName = "\n" + locationName;
        }

        String street = properties.getStreet();

        if (street == null || street.length() == 0){
            street = "";
        }else{
            street = "\n" + street;
        }

        String housenumber = properties.getHousenumber();
        if (housenumber == null){
            housenumber = "";
        }else{
            housenumber = " " + housenumber;
        }
        final String labelText = city + locationName + street + housenumber;

        return labelText;
       /* Location location = photonResponse.getLocations().get(0);

        String locationName;

        if (location == null) {
            return null;
        }

        Properties properties = location.getProperties();
        String city = properties.getCity();
        if (city == null){
            city = "";
        }

        String name = properties.getName();
        if (name == null){
            name = "";
        }

        String street = properties.getStreet();

        if (street == null){
            street = "";
        }

        String housenumber = properties.getHousenumber();
        if (housenumber == null){
            housenumber = "";
        }



//        String country = properties.getCountry();
//        locationName  = city + "," + name + "(" + country + ")";


        if (name.equals("")) {
            locationName = city + ", " + street + " " + housenumber;
        }else{
            locationName = city + ", " + name;
        }

*/
       /*
        String city = location.getCity();
        String name = location.getName();
        String postCode = location.getPostcode();
        String housNum = location.getProperties().getHousenumber();
        String street = location.getProperties().getStreet();


        String locationName =
                        "\nName: "          + name +
                        "\nCity: "          + city +
                        "\nPostCode: "      + postCode +
                        "\nStreet: "        + street +
                        "\nHousnumber: "    + housNum;*//*

        return locationName;*/
    }
}
