package com.example.cheaptrip.handlers.rest.geo;

import com.example.cheaptrip.dao.GeoCompletionClient;
import com.example.cheaptrip.handlers.rest.RestHandler;
import com.example.cheaptrip.models.photon.Location;
import com.example.cheaptrip.models.photon.PhotonResponse;
import com.example.cheaptrip.models.photon.Properties;


import retrofit2.Response;

public class GeoNameRestHandler extends RestHandler<String,PhotonResponse> {
    private final static String BASE_URL = "http://photon.komoot.de/";

    public GeoNameRestHandler(double lat, double lon){
        super(BASE_URL);

        GeoCompletionClient geoCompletionClient = retrofit.create(GeoCompletionClient.class);
        call = geoCompletionClient.getLocationName(lat,lon);
    }


    /**
     * TODO:Document
     * @param response Response from Rest-api of webservice.
     * @return
     */
    @Override
    public String extractDataFromResponse(Response<PhotonResponse> response) {
        PhotonResponse photonResponse = response.body();
        Location location = photonResponse.getLocations().get(0);

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



        return locationName;
    }
}
