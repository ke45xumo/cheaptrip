package com.example.cheaptrip.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cheaptrip.R;
import com.example.cheaptrip.dao.GasStationClient;
import com.example.cheaptrip.handlers.rest.geo.GeoJsonHandler;
import com.example.cheaptrip.handlers.rest.station.GasStationForRadiusHandler;
import com.example.cheaptrip.models.TripLocation;
import com.example.cheaptrip.models.TripVehicle;
import com.example.cheaptrip.models.orservice.ORServiceResponse;
import com.example.cheaptrip.models.tankerkoenig.Station;
import com.example.cheaptrip.services.GPSService;
import com.example.cheaptrip.services.RouteService;

import com.google.gson.Gson;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.Marker;

import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polygon;

import java.util.ArrayList;
import java.util.List;


public class CalculationActivity extends AppCompatActivity {


    static MapView  mMapView = null;
    static IMapController mMapController = null;

    TripLocation startLocation;
    TripLocation endLocation;

    TripVehicle tripVehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculation);


        Bundle extras = getIntent().getExtras();

        startLocation = (TripLocation) getIntent().getSerializableExtra("start");
        endLocation = (TripLocation) getIntent().getSerializableExtra("end");

        tripVehicle = (TripVehicle)getIntent().getSerializableExtra("tripVehicle");
        mMapView = (MapView) findViewById(R.id.mapView);


        initMap();
        getDirections();

        RouteService routeService = new RouteService(this,tripVehicle);
        routeService.execute(startLocation,endLocation);
    }

    public void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        mMapView.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    public void onPause() {
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        mMapView.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

    private void initMap(){
        mMapView.setTileSource(TileSourceFactory.MAPNIK);
        mMapView.setMultiTouchControls(true);

        GPSService gpsService = new GPSService(this);

        if (gpsService.canGetLocation()) {


            mMapController = mMapView.getController();
            mMapController.setZoom(20.0);
            GeoPoint startPoint = new GeoPoint(startLocation.getLatitdue(), startLocation.getLongitude());
            mMapController.setCenter(startPoint);

            /*=======================================
             * Set Start Marker
             *=======================================*/
            Marker markerStart = new Marker(mMapView);
            markerStart.setPosition(new GeoPoint(startLocation.getLatitdue(),startLocation.getLongitude()));
            markerStart.setTitle(startLocation.getLocationName());

            markerStart.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            mMapView.getOverlays().add(markerStart);
            markerStart.setIcon(getResources().getDrawable(R.drawable.marker_default));
            /*=======================================
             * Set Destination Marker
             *=======================================*/
            Marker markerEnd= new Marker(mMapView);
            markerEnd.setPosition(new GeoPoint(endLocation.getLatitdue(),endLocation.getLongitude()));
            markerEnd.setTitle(endLocation.getLocationName());

            markerEnd.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            mMapView.getOverlays().add(markerEnd);
            markerEnd.setIcon(getResources().getDrawable(R.drawable.marker_default));

        }

    }

    public void getDirections(){

        List<List<Double>> cooridinates = TripLocation.getAsDoubleList(startLocation,endLocation);

        GeoJsonHandler geoJsonHandler = new GeoJsonHandler(cooridinates);
        geoJsonHandler.startFindDirections();
    }


    private static void setDirectionOverlay(String geoJSON){
        if (geoJSON != null && geoJSON.length() > 0) {
            KmlDocument kmlDocument = new KmlDocument();
            kmlDocument.parseGeoJSON(geoJSON);
            FolderOverlay myOverLay = (FolderOverlay) kmlDocument.mKmlRoot.buildOverlay(mMapView, null, null, kmlDocument);
            mMapView.getOverlays().add(myOverLay);
            mMapView.invalidate();
        }
    }

    private static void setDirectionBbox(String geoJSON){
        Gson gson = new Gson();

        ORServiceResponse orServiceResponse = gson.fromJson(geoJSON,ORServiceResponse.class);
        List<Double> responseBbox = orServiceResponse.getBbox();

        BoundingBox bbox =  new BoundingBox(
                responseBbox.get(3),
                responseBbox.get(2),
                responseBbox.get(1),
                responseBbox.get(0)
        );
        mMapView.zoomToBoundingBox(bbox,true,150);

    }

    public static void onDirectionsLoaded(String geoJSON){
        setDirectionOverlay(geoJSON);
        setDirectionBbox(geoJSON);
    }

    public void onVehiclePropertiesLoaded(TripLocation start, TripLocation end, TripLocation location, final double radius){
        /*-------------------------------------------------------------------------
         * Draw the a circle for the max Radius to reach with current tank contents
         *-------------------------------------------------------------------------*/
        ArrayList <OverlayItem> overlayItems = new ArrayList<>();
        OverlayItem overlayCircle = new OverlayItem("Radius",null,new GeoPoint(location.getLatitdue(),location.getLongitude()));
        overlayItems.add(overlayCircle);

        GeoPoint startIGeoPoint = new GeoPoint(start.getLatitdue(),start.getLongitude());

        double radiusInMeters = radius * 1000;
        List<GeoPoint>  circle = Polygon.pointsAsCircle(startIGeoPoint,radiusInMeters);

        Polygon polygon = new Polygon();
        polygon.setPoints(circle);

        mMapView.getOverlays().add(polygon);
        /*-------------------------------------------------------------------
         * Draw the marker (where to start search fo gas stations nearby)
         *-------------------------------------------------------------------*/
        Marker markerFind = new Marker(mMapView);

        markerFind.setPosition(new GeoPoint(location.getLatitdue(),location.getLongitude()));
        markerFind.setTitle(startLocation.getLocationName());

        markerFind.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mMapView.getOverlays().add(markerFind);
        markerFind.setIcon(getResources().getDrawable(R.drawable.marker_default));

        mMapView.invalidate();
    }

    public void onSetCalculationMarker(TripLocation calcLocation){
        Marker markerStart = new Marker(mMapView);
        markerStart.setPosition(new GeoPoint(calcLocation.getLatitdue(),calcLocation.getLongitude()));
        markerStart.setTitle(startLocation.getLocationName());

        markerStart.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mMapView.getOverlays().add(markerStart);
        markerStart.setIcon(getResources().getDrawable(R.drawable.person));
        mMapView.invalidate();
    }



    public void onCalculationPointDetermined(TripLocation calcLocation){
        double lat = calcLocation.getLatitdue();
        double lon = calcLocation.getLongitude();

        GasStationForRadiusHandler gasStationHandler = new GasStationForRadiusHandler(lat,lon, GasStationClient.FuelType.E10);

        List<Station> stations = gasStationHandler.getStations();
        gasStationHandler.getJsonResponse();

        /*--------------------------------------------------------
         * DRAW the Search Radius for the stations (25km)
         *--------------------------------------------------------*/
        GeoPoint geoPoint = new GeoPoint(lat,lon);
        List<GeoPoint>  circle = Polygon.pointsAsCircle(geoPoint,25000);

        Polygon polygon = new Polygon();
        polygon.setPoints(circle);

        mMapView.getOverlays().add(polygon);
        mMapView.invalidate();
        /*--------------------------------------------------------
         * Set the Markers for the Gas Stations
         *--------------------------------------------------------*/
        for (Station station : stations){

            Marker stationMarker = new Marker(mMapView);

            stationMarker.setPosition(new GeoPoint(station.getLat(),station.getLng()));
            stationMarker.setTitle(station.getName() + "\nDiesel:" + station.getDiesel() + "\nE10" + station.getE10() + "\nE5:" + station.getE5());
            stationMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

            mMapView.getOverlays().add(stationMarker);
            mMapView.invalidate();
        }
    }

}
