package com.example.cheaptrip.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cheaptrip.R;

import com.example.cheaptrip.handlers.rest.RestListener;
import com.example.cheaptrip.handlers.rest.VehiclePropertyHandler;

import com.example.cheaptrip.handlers.rest.geo.GeoDirectionsBboxRestHandler;

import com.example.cheaptrip.handlers.rest.geo.GeoJsonHandler;
import com.example.cheaptrip.models.TripLocation;
import com.example.cheaptrip.models.TripVehicle;

import com.example.cheaptrip.models.orservice.ORServiceResponse;
import com.example.cheaptrip.services.GPSService;
import com.google.gson.Gson;


import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.Marker;

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
        //txt_start.setText(start);
        //txt_end.setText(end);

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

    public void setBoundingBox(){
        GeoDirectionsBboxRestHandler geoDirectionsBboxRestHandler = new GeoDirectionsBboxRestHandler(startLocation,endLocation);

        geoDirectionsBboxRestHandler.startLoadProperties(new RestListener<BoundingBox>() {
            @Override
            public void OnRestSuccess(BoundingBox bbox) {
                mMapView.zoomToBoundingBox(bbox,true);
            }

            @Override
            public void OnRestFail() {

            }
        });
    }
    public void getDirections(){

        List<List<Double>> cooridinates = TripLocation.getAsDoubleList(startLocation,endLocation);

        GeoJsonHandler geoJsonHandler = new GeoJsonHandler(cooridinates);
        geoJsonHandler.startFindDirections();
        /*
        GeoDirectionsHandler geoDirectionsHandler = new GeoDirectionsHandler(startLocation,endLocation);

        geoDirectionsHandler.startLoadProperties(new RestListener<ORServiceResponse>() {
            @Override
            public void OnRestSuccess(ORServiceResponse response) {
                response.getBbox();
                Gson gson = new Gson();
                String geoJSON = gson.toJson(response);

            }

            @Override
            public void OnRestFail() {

            }
        });
        //setBoundingBox();*/
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

    private void startGetRoute(String geoJSON){
        Gson gson = new Gson();
        ORServiceResponse orServiceResponse = gson.fromJson(geoJSON,ORServiceResponse.class);

        List<List<Double>> coordinateList = orServiceResponse.getFeatures().get(0).getGeometry().getCoordinates();
        List<TripLocation> tripLocationList = TripLocation.getAsTripLocationList(coordinateList);


        VehiclePropertyHandler vehiclePropertyHandler = new VehiclePropertyHandler(tripVehicle);

        vehiclePropertyHandler.startGetProperties();

        //TripLocation stationTripLocation = TripLocation.findPointfromDistance()

    }

    public static void onVehiclePropertiesLoaded(TripVehicle vehicle){

    }

}
