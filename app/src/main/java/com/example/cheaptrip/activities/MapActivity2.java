package com.example.cheaptrip.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.graphics.drawable.Drawable;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.cheaptrip.R;
import com.example.cheaptrip.handlers.view.LocationListHandler;
import com.example.cheaptrip.handlers.rest.RestListener;
import com.example.cheaptrip.handlers.rest.geo.GeoLocationsForNameHandler;
import com.example.cheaptrip.handlers.rest.geo.GeoNameForLocationHandler;
import com.example.cheaptrip.models.TripLocation;
import com.example.cheaptrip.models.photon.Location;
import com.example.cheaptrip.models.photon.Properties;
import com.example.cheaptrip.services.GPSService;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.XYTileSource;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.IconOverlay;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;


import java.util.ArrayList;
import java.util.List;

public class MapActivity2 extends Activity {
    protected LocationManager locationManager;
    protected LocationListener locationListener;

    private EditText editLocationInput;

    private MapView mMapView = null;
    private IMapController mMapController = null;

    private TripLocation tripLocation;
    private Marker currentMarker;

    private TextView txtView_currentLocation;

    private Animation animSlideDown;
    private Animation animSlideUp;

    private ListView locationList;

    private Marker currentSelectedMarker;


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
        setContentView(R.layout.map_activity);

        mMapView = (MapView) findViewById(R.id.map);

        editLocationInput =  findViewById(R.id.edit_start);
        locationList = findViewById(R.id.list_map_locations);

        txtView_currentLocation = findViewById(R.id.tv_curr_location);

        //final ITileSource tileSource = new HEREWeGoTileSource(this);
        //mMapView.setTileSource(tileSource);

        //mMapView.setBuiltInZoomControls(true);
        animSlideDown = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_down);
        animSlideUp = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_up);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        final String locationName = (String) extras.get("location_name");

        initMap();
        initList();
        loadMarkers(locationName);
        editLocationInput.requestFocus();

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

    private void initList() {
        GPSService gpsService = new GPSService(this);

        if (gpsService.canGetLocation()) {
            final double latitude = gpsService.getLatitude();
            final double longitude = gpsService.getLongitude();
            LocationListHandler locationListHandler = new LocationListHandler(editLocationInput, locationList,latitude,longitude);
            locationListHandler.setInputListener();
            locationListHandler.setListTouchListeners();
        }

    }

    private void initMap(){
        //mMapView.setTileSource(TileSourceFactory.MAPNIK);
        mMapView.setTileSource(
                new XYTileSource("HttpMapnik",
                        0, 19, 256, ".png", new String[] {
                        "http://a.tile.openstreetmap.org/",
                        "http://b.tile.openstreetmap.org/",
                        "http://c.tile.openstreetmap.org/" },
                        "© OpenStreetMap contributors")
        );

        mMapView.setMultiTouchControls(true);

        GPSService gpsService = new GPSService(this);

        if (gpsService.canGetLocation()) {
            final double latitude = gpsService.getLatitude();
            final double longitude = gpsService.getLongitude();

            GeoNameForLocationHandler geoNameForLocationHandler = new GeoNameForLocationHandler(latitude, longitude);
            geoNameForLocationHandler.makeAsyncRequest(new RestListener<String>() {
                @Override
                public void OnRestSuccess(String s) {
                    currentMarker.setTitle(s);
                    currentMarker.showInfoWindow();
                }

                @Override
                public void OnRestFail() {

                }
            });

            mMapController = mMapView.getController();
            mMapController.setZoom(20.0);
            GeoPoint startPoint = new GeoPoint(latitude, longitude);
            mMapController.setCenter(startPoint);

            currentMarker= new Marker(mMapView);
            currentMarker.setPosition(new GeoPoint(latitude,longitude));
            currentMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            currentMarker.setIcon(getResources().getDrawable(R.drawable.marker_default));
            //currentMarker.setTitle(locationName);

            mMapView.getOverlays().add(currentMarker);
            setTouchListener();


        }

    }

    public void loadMarker(Location location){
        List<Location> locationList = new ArrayList<>();
        locationList.add(location);
        loadMarkers(locationList);
    }


    public void loadMarkers(List<Location> locationList){
        if (locationList == null || locationList.size() <= 0){
            return;
        }

        for(Location location : locationList){
            List<Double> coordinates = location.getCoordinates();
            final String labelText = generateMarkerLabel(location);
            /*====================================================
             * Set
             *====================================================*/
            Marker marker = new Marker(mMapView);

            Double longitude = coordinates.get(0);
            Double latitude = coordinates.get(1);


            marker.setPosition(new GeoPoint(latitude,longitude));
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            mMapView.getOverlays().add(marker);

            marker.setIcon(getResources().getDrawable(R.drawable.osm_ic_center_map));

            marker.setTitle(labelText);

            setMarkerClickListener(marker);

        }

        if (locationList.size() > 1) {
            BoundingBox boundingBox = determineBoundingBox(locationList);
            mMapView.zoomToBoundingBox(boundingBox, true,150);
        }else if (locationList.size() == 1 ){
            double lon = locationList.get(0).getCoordinates().get(0);
            double lat = locationList.get(0).getCoordinates().get(1);

            mMapController.setCenter(new GeoPoint(lat,lon));
            mMapController.setZoom(18.0);
        }else{
            Log.w("CHEAPTRIP","location List is empty.");
        }



    }
    private void loadMarkers(final String locationName){
        final GeoLocationsForNameHandler geoLocationsForNameHandler = new GeoLocationsForNameHandler(locationName);

        geoLocationsForNameHandler.makeAsyncRequest(new RestListener< List<Location>>() {

            @Override
            public void OnRestSuccess( List<Location> locationList) {

                if (locationList == null || locationList.size() <= 0){
                    return;
                }

                for(Location location : locationList){
                    List<Double> coordinates = location.getCoordinates();
                    final String labelText = generateMarkerLabel(location);
                    /*====================================================
                     * Set
                     *====================================================*/
                    Marker marker = new Marker(mMapView);

                    Double longitude = coordinates.get(0);
                    Double latitude = coordinates.get(1);


                    marker.setPosition(new GeoPoint(latitude,longitude));
                    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                    mMapView.getOverlays().add(marker);

                    marker.setIcon(getResources().getDrawable(R.drawable.osm_ic_center_map));

                    marker.setTitle(labelText);
                    setMarkerClickListener(marker);

                }

                if (locationList.size() > 1) {
                    BoundingBox boundingBox = determineBoundingBox(locationList);
                    mMapView.zoomToBoundingBox(boundingBox, true,150);
                }else if (locationList.size() == 1 ){
                    double lon = locationList.get(0).getCoordinates().get(0);
                    double lat = locationList.get(0).getCoordinates().get(1);

                    mMapController.setCenter(new GeoPoint(lat,lon));
                }else{
                    Log.w("CHEAPTRIP","location List is empty.");
                }
            }

            @Override
            public void OnRestFail() {
                Context context = getApplicationContext();
                Toast.makeText(context,"Could not load Positions",Toast.LENGTH_LONG);
            }
        });
    }

    private static String generateMarkerLabel(Location location){
        Properties properties = location.getProperties();

        /*====================================================
         * Extract Values
         *====================================================*/
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
        final String labelText = locationName + "\nCity: " + city + "\nStreet: " + street + " " + housenumber;

        return labelText;
    }

    private static BoundingBox determineBoundingBox(List<Location> locationList){

        if (locationList == null || locationList.size() <= 0){
            Log.e("CHEAPTRIP", "Cannot Determine BoundingBox for Empty or noninitialized location list.");
            return null;
        }

        Double minLon = locationList.get(0).getCoordinates().get(0);
        Double maxLon = locationList.get(0).getCoordinates().get(0);

        Double minLat = locationList.get(0).getCoordinates().get(1);
        Double maxLat = locationList.get(0).getCoordinates().get(1);

        for(Location location : locationList) {
            List<Double> coordinates = location.getCoordinates();

            Double longitude = coordinates.get(0);
            Double latitude = coordinates.get(1);

            if (latitude < minLat) {
                minLat = latitude;
            }
            if (latitude > maxLat) {
                maxLat = latitude;
            }

            if (longitude < minLon) {
                minLon = longitude;
            }

            if (longitude > maxLon) {
                maxLon = longitude;
            }
        }

        return new BoundingBox(maxLat, maxLon, minLat, minLon);
    }

    private void setTouchListener(){
        final MapEventsReceiver mReceive = new MapEventsReceiver(){

            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                mMapView.getOverlayManager().remove(currentMarker);
                mMapView.getOverlays().remove(currentMarker);

                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(mMapView.getWindowToken(), 0);

                currentMarker.setPosition(new GeoPoint(p.getLatitude(),p.getLongitude()));
                mMapView.getOverlays().add(currentMarker);
                mMapView.invalidate();


                //mMapView.getOverlays().add(currentMarker);
                final GeoNameForLocationHandler geoNameListRestHandler = new GeoNameForLocationHandler(p.getLatitude(),p.getLongitude());

                geoNameListRestHandler.makeAsyncRequest(new RestListener<String>() {
                    @Override
                    public void OnRestSuccess(final String locationName) {
                        currentMarker.setTitle(locationName);

                        txtView_currentLocation.setText(locationName);
                        final double latitude = currentMarker.getPosition().getLatitude();
                        final double longitude = currentMarker.getPosition().getLongitude();
                        tripLocation = new TripLocation(locationName, latitude,longitude);

                        currentMarker.showInfoWindow();
                        currentMarker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker, MapView mapView) {
                                //Toast.makeText(getApplicationContext(),labelText,Toast.LENGTH_LONG).show();
                                marker.showInfoWindow();
                                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                in.hideSoftInputFromWindow(mMapView.getWindowToken(), 0);


                                tripLocation = new TripLocation(locationName,latitude,longitude);
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.putExtra("Location",tripLocation);

                                setResult(Activity.RESULT_OK,intent);
                                finish();

                                return true;
                            }
                        });

                    }

                    @Override
                    public void OnRestFail() {

                    }
                });

                return false;
            }
            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        };
        mMapView.getOverlays().add(new MapEventsOverlay(mReceive));
    }

    public void setMarkerClickListener(Marker marker){


        marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {

                if (marker != null && marker == marker){
                    double latitude = marker.getPosition().getLatitude();
                    double longitude = marker.getPosition().getLongitude();
                    String locationName = marker.getTitle();

                    tripLocation = new TripLocation(locationName,latitude,longitude);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("Location",tripLocation);

                    setResult(Activity.RESULT_OK,intent);


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        finishAfterTransition();
                    }else{
                        finish();
                    }
                }

                if(currentSelectedMarker != null) {
                    currentSelectedMarker.setIcon(getResources().getDrawable(R.drawable.osm_ic_center_map));
                }

                currentSelectedMarker = marker;

                //Toast.makeText(getApplicationContext(),labelText,Toast.LENGTH_LONG).show();
                marker.showInfoWindow();

                InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(mMapView.getWindowToken(), 0);

                final String locationName = marker.getTitle();
                final double latitude = marker.getPosition().getLatitude();
                final double longitude= marker.getPosition().getLongitude();

                tripLocation = new TripLocation(locationName,latitude,longitude);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                intent.putExtra("Location",tripLocation);

                setResult(Activity.RESULT_OK,intent);


                IconOverlay overlay = new IconOverlay();

                Drawable myIcon = getResources().getDrawable(R.drawable.apply_location);
                myIcon.setBounds(100, 0, myIcon.getIntrinsicWidth()+100, myIcon.getIntrinsicHeight());

                double lat = marker.getPosition().getLatitude();
                double lon = marker.getPosition().getLongitude();

                overlay.set(new GeoPoint(lat,lon),myIcon);

                marker.setIcon(myIcon);

                //mMapView.getOverlays().remove(marker);
                //mMapView.getOverlays().add();

                //marker.setImage(myIcon);
                //locationName = marker.getTitle();
                txtView_currentLocation.setText(locationName);

                Projection projection  = mapView.getProjection();


                //Point point = projection.toPixels(marker.getPosition(),null);
                //point.offset(100,-100);

                //GeoPoint geoPoint = (GeoPoint) projection.fromPixels(point.x,point.y);


                //overlay.set(geoPoint,myIcon);

                //mMapView.getOverlays().add(overlay);
                mMapView.invalidate();

                //finish();
                return true;
            }
        });
    }

    public void onLocationSelected(View view){

        double latitude = currentMarker.getPosition().getLatitude();
        double longitude = currentMarker.getPosition().getLongitude();
        String locationName = currentMarker.getTitle();

        tripLocation = new TripLocation(locationName,latitude,longitude);
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.putExtra("Location",tripLocation);

        setResult(Activity.RESULT_OK,intent);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        }else{
            finish();
        }
    }
}

