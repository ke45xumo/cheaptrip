package com.example.cheaptrip.services;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.cheaptrip.activities.CalculationActivity;
import com.example.cheaptrip.dao.GasStationClient;
import com.example.cheaptrip.handlers.CalculationListener;
import com.example.cheaptrip.handlers.rest.RestListener;
import com.example.cheaptrip.handlers.rest.geo.GeoDirectionsHandler;
import com.example.cheaptrip.handlers.rest.vehicle.VehiclePropertyHandler;
import com.example.cheaptrip.handlers.rest.geo.GeoDirectionMatrixHandler;

import com.example.cheaptrip.handlers.rest.station.GasStationForRadiusHandler;
import com.example.cheaptrip.handlers.rest.station.GasStationHistoryHandler;
import com.example.cheaptrip.handlers.rest.station.GasStationHistoryPriceHandler;
import com.example.cheaptrip.models.TripGasStation;
import com.example.cheaptrip.models.TripLocation;
import com.example.cheaptrip.models.TripRoute;
import com.example.cheaptrip.models.TripVehicle;

import com.example.cheaptrip.models.fueleconomy.Vehicles;
import com.example.cheaptrip.models.orservice.GeoMatrixResponse;
import com.example.cheaptrip.models.orservice.ORServiceResponse;
import com.example.cheaptrip.models.orservice.Segment;
import com.example.cheaptrip.models.tankerkoenig.Station;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class RouteService extends AsyncTask<TripLocation,Void,Void> {

    final static double RADIUS_EARTH = 6371e3;    // Radius of Earth in meters
    final static double AVG_TANK_VOLUME = 30;
    private static String BASE_URL = "https://api.openrouteservice.org/v2/";

    private TripVehicle tripVehicle;
    List<TripRoute> tripRouteList;

    private Context context;

    CalculationListener calculationListener;    // for Callbacks

    public RouteService(Context context, TripVehicle tripVehicle, CalculationListener calculationListener){
        this.tripVehicle = tripVehicle;
        this.context = context;
        tripRouteList = new ArrayList<>();
        this.calculationListener = calculationListener;
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
        /*============================================================================================
         * Initialize the TripVehicle (set its Properties from Rest-API, e.g. Consumption)
         *============================================================================================*/
        tripVehicle = initTripVehicle(tripVehicle);

        if (tripVehicle == null){
            return null;
        }
        /*==============================================================================================
         * Determine Route from Start to End Location
         *==============================================================================================*/
        GeoDirectionsHandler geoDirectionsHandler = new GeoDirectionsHandler(Arrays.asList(tripLocations));
        ORServiceResponse orServiceResponse = geoDirectionsHandler.makeSyncRequest();

        if(orServiceResponse == null){
            Log.e("CHEAPTRIP","OrServiceResponse is null");
            return null;
        }

        double distance = orServiceResponse.getFeatures().get(0).getProperties().getSummary().getDistance();
        double duration = orServiceResponse.getFeatures().get(0).getProperties().getSummary().getDuration();

        double hours = duration /3600;
        double avgSpeed = distance/hours;

        double maxRange = determineMaxRange(tripVehicle, avgSpeed);

        List<List<Double>> routeCoordinates = orServiceResponse.getFeatures().get(0).getGeometry().getCoordinates();
        List<TripLocation> tripLocationList = TripLocation.getAsTripLocationList(routeCoordinates);

       /* if(context != null && context instanceof CalcMapFragment){
            ((CalculationActivity) context).drawRange(startTripLocation, maxRange);
        }*/

        /*============================================================================================
         * Determine the Point on the Route from where to find Gas Stations nearby
         *============================================================================================*/
        TripLocation pointInRange = findPointfromDistance(maxRange,tripLocationList,25);

       /* if( calcMapFragment instanceof CalculationActivity) {
             CalculationActivity calculationActivity = (CalculationActivity) calcMapFragment;
            calculationActivity.drawRange(pointInRange, 25);

        }*/
        /*============================================================================================
         * Determine the Point on the Route from where to find Gas Stations nearby
         *============================================================================================*/
        List<Station> stationList = getStationsInRange(pointInRange);

        /*if (calcMapFragment instanceof CalculationActivity){
            CalculationActivity calculationActivity = (CalculationActivity) calcMapFragment;
            calculationActivity.drawStations(stationList);
        }*/

        tripRouteList = determineRoutes(startTripLocation,endTripLocation,stationList,tripVehicle);

        //calculationActivity.onCalculationDone(tripRouteList);
        return null;
    }

    public TripVehicle initTripVehicle(TripVehicle tripVehicle){
        if (tripVehicle == null){
            Log.e("CHEAPTRIP","Cannot assign Properties to tripVehicle: tripVehicle is null");
            return null;
        }

        VehiclePropertyHandler vehiclePropertyHandler = new VehiclePropertyHandler(tripVehicle);

        Vehicles vehicles = vehiclePropertyHandler.makeRequest();

        if( vehicles == null){
            Log.e("CHEAPTRIP","Cannot assign Properties to tripVehicle: Could not get Properties from REST-API");
            return null;
        }

        String strCityMPG = vehicles.getVehicleList().get(0).getCity08();
        double cityMPG = Double.parseDouble(strCityMPG);

        String strHighwayMPG = vehicles.getVehicleList().get(0).getHighway08();
        double highwayMPG = Double.parseDouble(strHighwayMPG);

        double cityKML = convertMPGto100KML(cityMPG);
        double highwayKML = convertMPGto100KML(highwayMPG);

        tripVehicle.setGetFuelConsumptionHighway(highwayKML);
        tripVehicle.setFuelConsumptionCity(cityKML);

        return tripVehicle;
    }

    public static double determineRouteCosts(TripRoute tripRoute, TripVehicle tripVehicle){
        return 0.0;
    }

    public List<TripRoute> determineRoutes(TripLocation startLocation, TripLocation endLocation, List<Station> stationList, TripVehicle tripVehicle){
        List<TripRoute> tripRouteList = new ArrayList<>();
        if(tripVehicle == null){
            Log.e("CHEAPTRIP","Cannot determine Routes: tripVehicle may not be null");
            return tripRouteList;
        }

        if(startLocation == null){
            Log.e("CHEAPTRIP","Cannot determine Routes: StartLocation may not be null");
            return tripRouteList;
        }

        if(startLocation == null){
            Log.e("CHEAPTRIP","Cannot determine Routes: EndLocation may not be null");
            return tripRouteList;
        }

        if(stationList == null){
            Log.e("CHEAPTRIP","Cannot determine Routes: stationList may not be null");
            return tripRouteList;
        }
        if(stationList.size() < 1){
            Log.e("CHEAPTRIP","Cannot determine Routes: stationList has no Elements");
            return tripRouteList;
        }

        for(Station station : stationList){
            if(station == null){
                continue;
            }


            TripRoute tripRoute = new TripRoute();
            TripGasStation tripGasStation = new TripGasStation(station);
            tripRoute.addTripLocation(startLocation,tripGasStation,endLocation);

            GeoDirectionsHandler geoDirectionsHandler = new GeoDirectionsHandler(tripRoute.getStops());
            ORServiceResponse orServiceResponse = geoDirectionsHandler.makeSyncRequest();

            if(orServiceResponse == null){
                Log.e("CHEAPTRIP","OrService Response is null");
               continue;
            }

            double distance = orServiceResponse.getFeatures().get(0).getProperties().getSummary().getDistance();
            double duration = orServiceResponse.getFeatures().get(0).getProperties().getSummary().getDuration();

            List<Segment> segments = orServiceResponse.getFeatures().get(0).getProperties().getSegments();


            Gson gson = new Gson();
            String geoJSON = gson.toJson(orServiceResponse);

            double avgSpeed = distance/duration;

            double cityMPG = tripVehicle.getFuelConsumptionCity();
            double highwayMPG = tripVehicle.getFuelConsumptionCity();

            double avgConsumption = interpolateConsumption(avgSpeed,cityMPG,highwayMPG);
            Double pricePerLiter = station.getE5();

            if(pricePerLiter == null){
                pricePerLiter = new Double(0);
            }

            double costs = distance * pricePerLiter * avgConsumption/100;


            tripRoute.setCosts(costs);
            tripRoute.setGeoJSON(geoJSON);
            tripRoute.setDistance(distance);
            tripRoute.setDuration(duration);

            tripRouteList.add(tripRoute);
           /* if(calcMapFragment instanceof CalculationActivity){
                CalculationActivity calculationActivity = (CalculationActivity) calcMapFragment;
                calculationActivity.drawRoute(geoJSON, Color.BLACK);
            }*/

        }
        return tripRouteList;
    }

    public List<Station> getStationsInRange(TripLocation calcLocation){
        double lat = calcLocation.getLatitdue();
        double lon = calcLocation.getLongitude();

        GasStationForRadiusHandler gasStationHandler = new GasStationForRadiusHandler(lat,lon, GasStationClient.FuelType.E10);

        List<Station> stations = gasStationHandler.getStations();

        return stations;
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

  /*  public List<Double> determinePointFromDistance(final double maxRange, List<List<Double>> coordinateList){
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
*/



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

            //calculationActivity.drawMarker(middleLocation, R.drawable.person);
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

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if(context != null && context instanceof CalculationActivity)
            calculationListener.onCalculationDone(tripRouteList);
    }
}
