package com.example.cheaptrip.activities;

import android.content.Context;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cheaptrip.R;

import com.example.cheaptrip.dao.ORServiceClient;
import com.example.cheaptrip.handlers.rest.RestListener;
import com.example.cheaptrip.handlers.rest.geo.GeoDirectionsBboxRestHandler;
import com.example.cheaptrip.handlers.rest.geo.GeoDirectionsHandler;
import com.example.cheaptrip.handlers.rest.geo.GeoJsonHandler;
import com.example.cheaptrip.models.TripLocation;
import com.example.cheaptrip.models.orservice.ORServiceResponse;
import com.example.cheaptrip.services.GPSService;
import com.google.gson.Gson;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.ArrayList;
import java.util.List;


public class CalculationActivity extends AppCompatActivity {


    MapView mMapView = null;
    IMapController mMapController = null;

    TripLocation startLocation;
    TripLocation endLocation;

    String vehicleModel;
    String vehicleBrand;
    String vehicleYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculation);


        Bundle extras = getIntent().getExtras();

        startLocation = (TripLocation) getIntent().getSerializableExtra("start");
        endLocation = (TripLocation) getIntent().getSerializableExtra("end");

        vehicleModel = (String) getIntent().getStringExtra("model");
        vehicleBrand = (String) getIntent().getStringExtra("brand");
        vehicleYear = (String) getIntent().getStringExtra("year");



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

            Marker markerStart = new Marker(mMapView);
            markerStart.setPosition(new GeoPoint(startLocation.getLatitdue(),startLocation.getLongitude()));
            markerStart.setTitle(startLocation.getLocationName());

            markerStart.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            mMapView.getOverlays().add(markerStart);
            markerStart.setIcon(getResources().getDrawable(R.drawable.marker_default));

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

        List<List<Double>> cooridinates = new ArrayList<>();

        List<Double> coordinatesStart = new ArrayList<>();
        coordinatesStart.add(startLocation.getLongitude());
        coordinatesStart.add(startLocation.getLatitdue());

        List<Double> coordinatesEnd = new ArrayList<>();
        coordinatesEnd.add(endLocation.getLongitude());
        coordinatesEnd.add(endLocation.getLatitdue());

        cooridinates.add(coordinatesStart);
        cooridinates.add(coordinatesEnd);

        GeoJsonHandler geoJsonHandler = new GeoJsonHandler(cooridinates);
        geoJsonHandler.getJson(mMapView);




       /* GeoDirectionsHandler geoDirectionsHandler = new GeoDirectionsHandler(startLocation,endLocation);

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


    public Double interpolateConsumption(double avgSpeed, double cityMPG, double highwayMPG){
        double citySpeed = 50.0;
        double highwaySpeed = 100.0;

        double avgConsumtion = cityMPG + ( avgSpeed - citySpeed) * (highwayMPG - cityMPG)/ (highwaySpeed -citySpeed);

        return avgConsumtion;
    }

}
