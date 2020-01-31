package com.example.cheaptrip.services;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.cheaptrip.activities.CalculationActivity;
import com.example.cheaptrip.dao.rest.GasStationClient;
import com.example.cheaptrip.handlers.CalculationListener;
import com.example.cheaptrip.handlers.rest.geo.GeoDirectionMatrixHandler;
import com.example.cheaptrip.handlers.rest.geo.GeoDirectionsHandler;
import com.example.cheaptrip.handlers.rest.vehicle.VehiclePropertyHandler;

import com.example.cheaptrip.handlers.rest.station.GasStationForRadiusHandler;
import com.example.cheaptrip.handlers.rest.station.GasStationHistoryHandler;
import com.example.cheaptrip.handlers.rest.station.GasStationHistoryPriceHandler;
import com.example.cheaptrip.models.TripGasStation;
import com.example.cheaptrip.models.TripLocation;
import com.example.cheaptrip.models.TripRoute;
import com.example.cheaptrip.models.TripVehicle;

import com.example.cheaptrip.models.fueleconomy.Vehicles;
import com.example.cheaptrip.models.tankerkoenig.Station;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;



public class RouteService extends AsyncTask<TripLocation,Void,Void> {

    final static double RADIUS_EARTH = 6371e3;    // Radius of Earth in meters

    private TripVehicle tripVehicle;
    List<TripRoute> tripRouteList;

    private Context context;

    CalculationListener calculationListener;    // for Callbacks

    public RouteService(Context context, TripVehicle tripVehicle, CalculationListener calculationListener){
        assert(tripVehicle != null);

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
        GeoDirectionsHandler geoDirectionsHandler = new GeoDirectionsHandler(Arrays.asList(tripLocations),null);
        TripRoute tripRoute = geoDirectionsHandler.makeSyncRequest();

        if(tripRoute == null){
            Log.e("CHEAPTRIP","Error on calling OpenrouteService: TripLocation is null");
            return null;
        }

        double distance = tripRoute.getDistance();
        double duration = tripRoute.getDuration();

        double hours = duration /3600;
        double avgSpeed = distance/hours;

        double maxRange = determineMaxRange(tripVehicle, avgSpeed);

        List<TripLocation> tripLocationList = tripRoute.getPointsForPolyLine();
        /*============================================================================================
         * Determine the Point on the Route from where to find Gas Stations nearby
         *============================================================================================*/
        TripLocation pointInRange = findPointfromDistance(maxRange,tripLocationList,25);
        /*============================================================================================
         * Determine the Point on the Route from where to find Gas Stations nearby
         *============================================================================================*/
        List<Station> stationList = getStationsInRange(pointInRange);

        tripRouteList = determineTripRoutes(startTripLocation,endTripLocation,stationList,tripVehicle);

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


    public List<TripRoute> determineTripRoutes(TripLocation startLocation, TripLocation endLocation, List<Station> stationList, TripVehicle tripVehicle){

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

        Log.d("CHEAPTRIP","NUMBER OF GASSTATIONS: " + stationList.size());

        /*==========================================================================================
         * Generate the Coordinate List for the POST Body of Matrix Request (ORService)
         *========================================================================================*/
        List<List<Double>> matrixCoordinateList = new ArrayList<>();

        matrixCoordinateList.add(startLocation.getAsDoubleList());
        matrixCoordinateList.add(endLocation.getAsDoubleList());

        for(Station station : stationList) {
            if (station == null) {
                continue;
            }

            List<Double> stationCoordinates = new ArrayList<>();

            double lon = station.getLng();
            double lat = station.getLat();

            stationCoordinates.add(lon);
            stationCoordinates.add(lat);

            matrixCoordinateList.add(stationCoordinates);
        }
        /*==========================================================================================
         * Get TripRoutes with Durations and Distances from ORService API
         *========================================================================================*/
        List<Integer> locationPosition = new ArrayList<Integer>();     // Location of start and end
        locationPosition.add(0);
        locationPosition.add(1);

        GeoDirectionMatrixHandler matrixHandler =  new GeoDirectionMatrixHandler(matrixCoordinateList,locationPosition,null);
        List<TripRoute> tripRouteList = matrixHandler.makeSyncRequest();
        /*================================================================
         * Initialize the TripRoute ( Set Stops and Costs)
         *================================================================*/
        List<TripRoute> resultList = new ArrayList<>();         // list with all routes that hold costs

        Iterator tripListIterator = tripRouteList.iterator();

        for(Station station : stationList){

            if(!station.isOpen()){
                continue;
            }

            TripGasStation tripGasStation = new TripGasStation(station);


            TripRoute tripRoute = (TripRoute) tripListIterator.next();

            if(tripRoute == null){
                Log.w("CHEAPTRIP", "RouteService->determineTripRoutes: tripRoute is null. " +
                        "This should not happen (stationList size and tripRoute List size shout be the same");
                continue;
            }

            /*=========================================================
             * Determine Consumption
             *=========================================================*/
            double distance = tripRoute.getDistance();
            double duration = tripRoute.getDuration();

            tripRoute.addTripLocation(startLocation,tripGasStation,endLocation);

            double avgSpeed = distance/(duration/3600);     // in KMH

            double cityMPG = tripVehicle.getFuelConsumptionCity();
            double highwayMPG = tripVehicle.getFuelConsumptionCity();

            double avgConsumption = interpolateConsumption(avgSpeed,cityMPG,highwayMPG);

            GasStationClient.FuelType fuelType = tripVehicle.getFueltype();
            /*=========================================================
             * Get GasStation Price
             *=========================================================*/
            Double pricePerLiter = null;
            if(fuelType == GasStationClient.FuelType.E5) {
                pricePerLiter = station.getE5();
            }

            if(fuelType == GasStationClient.FuelType.E10) {
                pricePerLiter = station.getE10();
            }

            if(fuelType == GasStationClient.FuelType.DIESEL) {
                pricePerLiter = station.getDiesel();
            }

            if(pricePerLiter == null){
                continue;
            }
            /*=========================================================
             * Determine Costs
             *=========================================================*/
            double costs = distance * pricePerLiter * avgConsumption/100;

            if(costs == 0){
                continue;
            }


            tripRoute.setCosts(costs);
            resultList.add(tripRoute);

        }
       /* /*================================================================
         * Iterate over the Received Stations
         *================================================================*//*
        for(Station station : stationList){
            if(station == null){
                continue;
            }

            TripGasStation tripGasStation = new TripGasStation(station);

            // Prepare Arguments
            List<TripLocation> stops = new ArrayList<>();
            stops.add(startLocation);
            stops.add(tripGasStation);
            stops.add(endLocation);

            GeoDirectionsHandler geoDirectionsHandler = new GeoDirectionsHandler(stops);
            TripRoute tripRoute  = geoDirectionsHandler.makeSyncRequest();

            if(tripRoute == null){
                Log.e("CHEAPTRIP","Could not get TripRoute from OpenRouteService: result is null.");
               continue;
            }

            double distance = tripRoute.getDistance();
            double duration = tripRoute.getDuration();

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
            tripRoute.setStops(stops);

            tripRouteList.add(tripRoute);

           *//* if(calcMapFragment instanceof CalculationActivity){
                CalculationActivity calculationActivity = (CalculationActivity) calcMapFragment;
                calculationActivity.drawRoute(geoJSON, Color.BLACK);
            }*//*


            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }*/

        return resultList;
    }

    public List<Station> getStationsInRange(TripLocation calcLocation){
        double lat = calcLocation.getLatitdue();
        double lon = calcLocation.getLongitude();

        GasStationForRadiusHandler gasStationHandler = new GasStationForRadiusHandler(lat,lon, tripVehicle.getFueltype());

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

        double maxRange = percentRest * tripVehicle.getTANK_CAPACITY() * avgConsumption;

        return maxRange;
    }

    public TripLocation findPointfromDistance(double radius, List<TripLocation> tripLocations, double offset){
        int indexLowerBound = 0;
        int indexUpperBound = tripLocations.size()-1;
        int indexMiddle;

        TripLocation startLocation = tripLocations.get(indexLowerBound);
        TripLocation middleLocation = tripLocations.get(indexUpperBound);

        TripLocation lowerLocation = startLocation;
        double distance = calculateDistance(startLocation, middleLocation);

        double areaToFind =  radius - offset;


        double ratio = Math.abs(distance - areaToFind)/areaToFind;

        // > 1 because of rounding
        while( Math.abs(indexLowerBound - indexUpperBound) > 1){
            indexMiddle =(indexUpperBound +indexLowerBound )/2;

            //lowerLocation = tripLocations.get(indexLowerBound);
            middleLocation = tripLocations.get(indexMiddle);

            distance = calculateDistance(startLocation, middleLocation);

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
