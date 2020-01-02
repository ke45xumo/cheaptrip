package com.example.cheaptrip.services;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.cheaptrip.activities.CalculationActivity;
import com.example.cheaptrip.handlers.rest.RestListener;
import com.example.cheaptrip.handlers.rest.VehiclePropertyHandler;
import com.example.cheaptrip.handlers.rest.geo.GeoDirectionMatrixHandler;

import com.example.cheaptrip.handlers.rest.geo.GeoJsonHandler;
import com.example.cheaptrip.handlers.rest.station.GasStationHistoryHandler;
import com.example.cheaptrip.handlers.rest.station.GasStationHistoryPriceHandler;
import com.example.cheaptrip.models.TripLocation;
import com.example.cheaptrip.models.TripVehicle;

import com.example.cheaptrip.models.fueleconomy.Vehicles;
import com.example.cheaptrip.models.orservice.GeoMatrixResponse;
import com.example.cheaptrip.models.orservice.ORServiceResponse;
import com.example.cheaptrip.models.tankerkoenig.Station;


import java.util.ArrayList;
import java.util.List;



public class RouteService extends AsyncTask<TripLocation,Void,Void> {

    final static double RADIUS_EARTH = 6371e3;    // Radius of Earth in meters
    final static double AVG_TANK_VOLUME = 30;
    private static String BASE_URL = "https://api.openrouteservice.org/v2/";

    private TripVehicle tripVehicle;

    private Context context;
    public RouteService(Context context, TripVehicle tripVehicle){
        this.tripVehicle = tripVehicle;
        this.context = context;
    }

    @Override
    protected Void doInBackground(TripLocation... tripLocations) {
        if (tripLocations.length < 2){
            Log.e("CHEAPTRIP","locations must be greater than 2 (start + destination)");
            return null;
        }

        TripLocation startTripLocation = tripLocations[0];
        TripLocation endTripLocation = tripLocations[1];

        if (startTripLocation == null){
            Log.e("CHEAPTRIP","Start location is empty.");
            return null;
        }

        if (endTripLocation == null){
            Log.e("CHEAPTRIP","End location is empty.");
            return null;
        }

        List<List<Double>> coordinateList = TripLocation.getAsDoubleList(startTripLocation,endTripLocation);
        GeoJsonHandler geoJsonHandler = new GeoJsonHandler(coordinateList);

        ORServiceResponse orServiceResponse = geoJsonHandler.makeRequest();

        double distance = orServiceResponse.getFeatures().get(0).getProperties().getSummary().getDistance();
        double duration = orServiceResponse.getFeatures().get(0).getProperties().getSummary().getDuration();

        double hours = duration /3600;
        double avgSpeed = distance/hours;

        List<List<Double>> routeCoordinates = orServiceResponse.getFeatures().get(0).getGeometry().getCoordinates();
        List<TripLocation> tripLocationList = TripLocation.getAsTripLocationList(routeCoordinates);

        VehiclePropertyHandler vehiclePropertyHandler = new VehiclePropertyHandler(tripVehicle);
        //String xml = vehiclePropertyHandler.getStringResponse();

        Vehicles vehicles = vehiclePropertyHandler.makeRequest();

        String strCityMPG = vehicles.getVehicleList().get(0).getCity08();
        double cityMPG = Double.parseDouble(strCityMPG);

        String strHighwayMPG = vehicles.getVehicleList().get(0).getHighway08();
        double highwayMPG = Double.parseDouble(strHighwayMPG);

        double cityKML = convertMPGto100KML(cityMPG);
        double highwayKML = convertMPGto100KML(highwayMPG);

        tripVehicle.setGetFuelConsumptionHighway(highwayKML);
        tripVehicle.setFuelConsumptionCity(cityKML);

        double maxRange = determineMaxRange(tripVehicle, avgSpeed);

        TripLocation pointInRange = findPointfromDistance(maxRange,tripLocationList,25);

        CalculationActivity calculationActivity = (CalculationActivity) context;

        calculationActivity.onVehiclePropertiesLoaded(startTripLocation,endTripLocation,pointInRange,maxRange);
        calculationActivity.onCalculationPointDetermined(pointInRange);
        return null;
    }


    public Double interpolateConsumption(double avgSpeed, double cityMPG, double highwayMPG){
        double citySpeed = 50.0;
        double highwaySpeed = 100.0;

        double avgConsumtion = cityMPG + ( avgSpeed - citySpeed) * (highwayMPG - cityMPG)/ (highwaySpeed -citySpeed);

        return avgConsumtion;
    }


    public double determineMaxRange(TripVehicle tripVehicle, double avgSpeed){
        double percentRest = tripVehicle.getRemainFuelPercent();
        double cityMPG = tripVehicle.getFuelConsumptionCity();
        double highwayMPG = tripVehicle.getFuelConsumptionCity();

        double avgConsumption = interpolateConsumption(avgSpeed,cityMPG,highwayMPG);
        //double avgConsumption =  interpolateConsumption();

        double maxRange = percentRest * AVG_TANK_VOLUME * avgConsumption;

        return maxRange;
    }

    public List<Double> determinePointFromDistance(final double maxRange, List<List<Double>> coordinateList){
        final int countCoordinates = coordinateList.size();

        final List<List<Double>> currentCoordinates = new ArrayList<>();

        final int step = countCoordinates/10;

        for(int i = 0 ; i < countCoordinates; i+= step ){
            currentCoordinates.add(coordinateList.get(i));
        }

        GeoDirectionMatrixHandler geoDirectionMatrixHandler = new GeoDirectionMatrixHandler(currentCoordinates);

        geoDirectionMatrixHandler.makeAsyncRequest(new RestListener<GeoMatrixResponse>() {
            @Override
            public void OnRestSuccess(GeoMatrixResponse geoMatrixResponse) {
                int index = 0;
                for( Double distance : geoMatrixResponse.getDistances().get(0)){
                    while (maxRange < distance ){
                        index += step;
                    }
                }
                int upperBound = Math.min(index,countCoordinates);
                List<List<Double>> newSection = currentCoordinates.subList(index, upperBound);

                determinePointFromDistance(maxRange,newSection);

            }

            @Override
            public void OnRestFail() {

            }
        });

        return null;
    }




    public TripLocation findPointfromDistance(double radius, List<TripLocation> tripLocations, double offset){
        int indexLowerBound = 0;
        int indexUpperBound = tripLocations.size()-1;
        int indexMiddle;

        TripLocation startLocation = tripLocations.get(indexLowerBound);
        TripLocation middleLocation = tripLocations.get(indexUpperBound);

        TripLocation lowerLocation = startLocation;
        double distance = calculateDistance2(startLocation, middleLocation);
        //double areaToFind = (1- precision) * radius;
        double areaToFind =  radius - offset;
        CalculationActivity calculationActivity = (CalculationActivity) context;

        double ratio = Math.abs(distance - areaToFind)/areaToFind;

        // > 1 because of rounding
        while( Math.abs(indexLowerBound - indexUpperBound) > 1){
            indexMiddle =(indexUpperBound +indexLowerBound )/2;

            //lowerLocation = tripLocations.get(indexLowerBound);
            middleLocation = tripLocations.get(indexMiddle);

            distance = calculateDistance2(startLocation, middleLocation);

            if(areaToFind < distance){
                indexUpperBound = indexMiddle;
            }else{
                indexLowerBound = indexMiddle;
            }

            calculationActivity.onSetCalculationMarker(middleLocation);
            ratio = Math.abs(distance - areaToFind)/areaToFind;
        }

        return  middleLocation;
    }


    public static double calculateDistance(TripLocation location1, TripLocation location2){
        double latitude1 = location1.getLatitdue();
        double longitude1 = location1.getLongitude();

        double latitude2 = location2.getLatitdue();
        double longitude2 = location2.getLongitude();

        double phi1 = Math.toRadians(latitude1);
        double phi2 = Math.toRadians(location2.getLatitdue());


        double deltaPhi = Math.toRadians(latitude2 - latitude2);
        double deltaLambda = Math.toRadians(longitude2 -longitude1);


        double a = Math.pow(Math.sin(deltaPhi/2),2)
                    + Math.cos(phi1) * Math.cos(phi2) * Math.pow(Math.sin(deltaLambda/2),2);

        double c = 2* Math.atan2(Math.sqrt(a),Math.sqrt(1-a));

        double distance = RADIUS_EARTH * c;

        return distance/1000;
    }


    public static double calculateDistance2(TripLocation location1, TripLocation location2){
        double latitude1 = location1.getLatitdue();
        double longitude1 = location1.getLongitude();

        double latitude2 = location2.getLatitdue();
        double longitude2 = location2.getLongitude();

        double theta = longitude1 - longitude2;
        double dist = Math.sin(deg2rad(latitude1)) * Math.sin(deg2rad(latitude2)) + Math.cos(deg2rad(latitude1)) * Math.cos(deg2rad(latitude2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;

        return (dist);
    }

    public List<Station> loadHistoricPrices(int year, int month, int day){
        GasStationHistoryPriceHandler gasStationHistoryPriceHandler = new GasStationHistoryPriceHandler(year,month,day);
        List<Station> priceList = gasStationHistoryPriceHandler.getHistory(year, month, day);

        return priceList;
    }

    public List<Station> loadHistoricStationProperties(int year, int month, int day){
        GasStationHistoryHandler gasStationHistoryHandler = new GasStationHistoryHandler(2019,12, 10);
        List<Station> stationList = gasStationHistoryHandler.getHistory();

        return stationList;
    }
    private double convertBarrelsToLitre(double barrels){
        return barrels/0.0062898;
    }

    private double convertMilesToKM(double miles){
        return 1.60934 * miles;
    }

    private double convertMPGto100KML(double mpg){
        return 235.215/mpg;
    }



    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
