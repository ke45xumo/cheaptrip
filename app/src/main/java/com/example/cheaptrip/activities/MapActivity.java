package com.example.cheaptrip.activities;

import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.example.cheaptrip.R;
import com.example.cheaptrip.handlers.RestListener;
import com.example.cheaptrip.handlers.rest.geo.GeoFeatureRestHandler;
import com.example.cheaptrip.handlers.rest.geo.GeoLocationRestHandler;
import com.example.cheaptrip.models.rest.photon.Feature;
import com.example.cheaptrip.models.rest.photon.Properties;
import com.example.cheaptrip.services.gps.GPSService;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.List;

public class MapActivity extends Activity {
    protected LocationManager locationManager;
    protected LocationListener locationListener;

    MapView mMapView = null;
    IMapController mMapController = null;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //handle permissions first, before mMapView is created. not depicted here

        //load/initialize the osmdroid configuration, this can be done
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        //setting this before the layout is inflated is a good idea
        //it 'should' ensure that the mMapView has a writable location for the mMapView cache, even without permissions
        //if no tiles are displayed, you can try overriding the cache path using Configuration.getInstance().setCachePath
        //see also StorageUtils
        //note, the load method also sets the HTTP User Agent to your application's package name, abusing osm's tile servers will get you banned based on this string

        //inflate and create the mMapView
        setContentView(R.layout.map);

        mMapView = (MapView) findViewById(R.id.map);


        //final ITileSource tileSource = new HEREWeGoTileSource(this);
        //mMapView.setTileSource(tileSource);

        //mMapView.setBuiltInZoomControls(true);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        final String locationName = (String) extras.get("location_name");

        initMap(locationName);
        loadMarkers2(locationName);


    }

    public void onResume(){
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        mMapView.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    public void onPause(){
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        mMapView.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }
    private void initMap(String locationName){
        mMapView.setTileSource(TileSourceFactory.MAPNIK);
        mMapView.setMultiTouchControls(true);

        GPSService gpsService = new GPSService(this);

        if (gpsService.canGetLocation()) {
            double latitude = gpsService.getLatitude();
            double longitude = gpsService.getLongitude();

            mMapController = mMapView.getController();
            mMapController.setZoom(20.0);
            GeoPoint startPoint = new GeoPoint(latitude, longitude);
            mMapController.setCenter(startPoint);

            Marker marker = new Marker(mMapView);

            marker.setPosition(new GeoPoint(latitude,longitude));
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            mMapView.getOverlays().add(marker);

            marker.setIcon(getResources().getDrawable(R.drawable.marker_default));
            marker.setTitle(locationName);
        }

    }
    private void loadMarkers2(final String locationName){
        GeoFeatureRestHandler geoFeatureRestHandler= new GeoFeatureRestHandler(locationName);

        geoFeatureRestHandler.startLoadProperties(new RestListener< List<Feature>>() {
            Double minLon;
            Double maxLon;

            Double minLat;
            Double maxLat;

            @Override
            public void OnRestSuccess( List<Feature> featureList) {

                if (featureList == null || featureList.size() <= 0){
                    return;
                }
                minLon = featureList.get(0).getCoordinates().get(0);
                maxLon = featureList.get(0).getCoordinates().get(0);

                minLat = featureList.get(0).getCoordinates().get(1);
                maxLat = featureList.get(0).getCoordinates().get(1);

                for(Feature feature: featureList){
                    List<Double> coordinates = feature.getCoordinates();

                    //String locationName = feature.getName();
                    Properties properties = feature.getProperties();

                    String city = properties.getCity();
                    if (city == null){
                        city = "";
                    }

                    String locationName = properties.getName();
                    if (locationName == null){
                        locationName = "";
                    }

                    String street = properties.getStreet();

                    if (street == null){
                        street = "";
                    }

                    String housenumber = properties.getHousenumber();
                    if (housenumber == null){
                        housenumber = "";
                    }


                    String labelText = locationName + "\nCity: " + city + "\nStreet: " + street + " " + housenumber;

                    Marker marker = new Marker(mMapView);

                    Double longitude = coordinates.get(0);
                    Double latitude = coordinates.get(1);

                    if(latitude < minLat){
                        minLat = latitude;
                    }
                    if (latitude > maxLat){
                        maxLat = latitude;
                    }

                    if (longitude < minLon){
                        minLon = longitude;
                    }

                    if (longitude > maxLon){
                        maxLon = longitude;
                    }


                    marker.setPosition(new GeoPoint(latitude,longitude));
                    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                    mMapView.getOverlays().add(marker);

                    marker.setIcon(getResources().getDrawable(R.drawable.osm_ic_center_map));
                    marker.setTitle(labelText);
                    marker.showInfoWindow();
                }

                if (featureList.size() > 1) {
                    //BoundingBoxE6 boundingBox = new BoundingBoxE6(north, east, south, west);
                    mMapView.zoomToBoundingBox(new BoundingBox(maxLat, maxLon, minLat, minLon), true);
                }else{
                    mMapController.setCenter(new GeoPoint(minLat,minLon));
                }
            }

            @Override
            public void OnRestFail() {

            }
        });
    }
    private void loadMarkers(final String locationName){
        GeoLocationRestHandler geoLocationRestHandler = new GeoLocationRestHandler(locationName);

        geoLocationRestHandler.startLoadProperties(new RestListener<List<List<Double>>>() {
            Double minLon;
            Double maxLon;

            Double minLat;
            Double maxLat;

            @Override
            public void OnRestSuccess(List<List<Double>> coordinateList) {
                minLon = coordinateList.get(0).get(0);
                maxLon = coordinateList.get(0).get(0);

                minLat = coordinateList.get(0).get(1);
                maxLat = coordinateList.get(0).get(1);

                for (List<Double> coordinates : coordinateList){



                    Marker marker = new Marker(mMapView);

                    Double longitude = coordinates.get(0);
                    Double latitude = coordinates.get(1);

                    if(latitude < minLat){
                        minLat = latitude;
                    }
                    if (latitude > maxLat){
                        maxLat = latitude;
                    }

                    if (longitude < minLon){
                        minLon = longitude;
                    }

                    if (longitude > maxLon){
                        maxLon = longitude;
                    }


                    marker.setPosition(new GeoPoint(latitude,longitude));
                    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                    mMapView.getOverlays().add(marker);

                    marker.setIcon(getResources().getDrawable(R.drawable.osm_ic_center_map));
                    marker.setTitle(locationName);
                    marker.showInfoWindow();
                }


                //BoundingBoxE6 boundingBox = new BoundingBoxE6(north, east, south, west);
                mMapView.zoomToBoundingBox(new BoundingBox(maxLat + 0.1,maxLon+ 0.1,minLat -0.1,minLon - 0.1),true);
            }

            @Override
            public void OnRestFail() {

            }
        });
    }
}