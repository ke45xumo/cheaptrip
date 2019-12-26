package com.example.cheaptrip.services;

import com.example.cheaptrip.handlers.rest.RestListener;
import com.example.cheaptrip.handlers.rest.geo.GeoDirectionMatrixHandler;
import com.example.cheaptrip.models.TripLocation;
import com.example.cheaptrip.models.TripVehicle;
import com.example.cheaptrip.models.orservice.GeoMatrixResponse;

import java.util.ArrayList;
import java.util.List;



public class RouteService {

    final static double RADIUS_EARTH = 6371e3;    // Radius of Earth in meters
    final static double AVG_TANK_VOLUME = 30;

    public RouteService(){

    }

    public Double interpolateConsumption(double avgSpeed, double cityMPG, double highwayMPG){
        double citySpeed = 50.0;
        double highwaySpeed = 100.0;

        double avgConsumtion = cityMPG + ( avgSpeed - citySpeed) * (highwayMPG - cityMPG)/ (highwaySpeed -citySpeed);

        return avgConsumtion;
    }


    public Double determineMaxRange(TripVehicle tripVehicle, double tankPercent){
        return null;
    }

    public List<Double> determinePointFromDistance(final double tankRadius, List<List<Double>> coordinateList){
        final int countCoordinates = coordinateList.size();

        final List<List<Double>> currentCoordinates = new ArrayList<>();

        final int step = countCoordinates/10;

        for(int i = 0 ; i < countCoordinates; i+= step ){
            currentCoordinates.add(coordinateList.get(i));
        }

        GeoDirectionMatrixHandler geoDirectionMatrixHandler = new GeoDirectionMatrixHandler(currentCoordinates);

        geoDirectionMatrixHandler.startLoadProperties(new RestListener<GeoMatrixResponse>() {
            @Override
            public void OnRestSuccess(GeoMatrixResponse geoMatrixResponse) {
                int index = 0;
                for( Double distance : geoMatrixResponse.getDistances().get(0)){
                    while (tankRadius < distance ){
                        index += step;
                    }
                }
                int upperBound = Math.min(index,countCoordinates);
                List<List<Double>> newSection = currentCoordinates.subList(index, upperBound);

                determinePointFromDistance(tankRadius,newSection);

            }

            @Override
            public void OnRestFail() {

            }
        });

        return null;
    }


    public static TripLocation findPointfromDistance(double radius, List<TripLocation> tripLocations, double precision){
        int indexLowerBound = 0;
        int indexUpperBound = tripLocations.size();
        int indexMiddle;

        TripLocation startLocation = tripLocations.get(indexLowerBound);
        TripLocation middleLocation = tripLocations.get(indexUpperBound);

        double areaToFind = precision * radius;

        while(Math.abs(indexUpperBound - indexLowerBound) > areaToFind){
            indexMiddle =(indexUpperBound -indexLowerBound )/2;

            startLocation = tripLocations.get(indexLowerBound);
            middleLocation = tripLocations.get(indexMiddle);

            double distance = calculateDistance(startLocation, middleLocation);

            if(areaToFind < distance){
                indexUpperBound = indexMiddle;
            }else{
                indexLowerBound = indexMiddle;
            }
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


        double a = Math.pow(Math.sin(Math.sin(deltaPhi/2)),2)
                    + Math.cos(phi1) * Math.cos(phi2) * Math.pow(Math.sin(deltaLambda/2),2);

        double c = 2* Math.atan2(Math.sqrt(a),Math.sqrt(1-a));

        double distance = RADIUS_EARTH * c;

        return distance;
    }
}
