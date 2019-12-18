package com.example.cheaptrip.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowInsets;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cheaptrip.R;
import com.example.cheaptrip.handlers.LocationTextHandler;
import com.example.cheaptrip.handlers.rest.RestListener;
import com.example.cheaptrip.handlers.rest.geo.GeoFeatureRestHandler;
import com.example.cheaptrip.models.TripLocation;
import com.example.cheaptrip.models.photon.Location;
import com.example.cheaptrip.models.photon.Properties;
import com.example.cheaptrip.services.GPSService;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.overlays.GroundOverlay;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ClickableIconOverlay;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.infowindow.InfoWindow;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends Activity {
    protected LocationManager locationManager;
    protected LocationListener locationListener;

    AutoCompleteTextView autoCompleteTextView;

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
        autoCompleteTextView =  findViewById(R.id.edit_start);


        //final ITileSource tileSource = new HEREWeGoTileSource(this);
        //mMapView.setTileSource(tileSource);

        //mMapView.setBuiltInZoomControls(true);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        final String locationName = (String) extras.get("location_name");

        initMap(locationName);
        loadMarkers(locationName);



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

    private void initAutoComplete(double currLatitude, double currLongitude){
        /*=================================================
         * Auto Completion
         *=================================================*/
        //suggestions =  new ArrayList<>(); //autocomplete Suggestions
        LocationTextHandler locationTextHandler = new LocationTextHandler(currLatitude,currLongitude);
        locationTextHandler.setCurrentLocation(this,autoCompleteTextView, new TripLocation());

        locationTextHandler.addTextChangedListener(autoCompleteTextView);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String Location = (String)parent.getAdapter().getItem(position);
                loadMarkers(Location);
            }
        });



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
            initAutoComplete(latitude,longitude);
        }

    }
    private void loadMarkers(final String locationName){
        final GeoFeatureRestHandler geoFeatureRestHandler= new GeoFeatureRestHandler(locationName);

        geoFeatureRestHandler.startLoadProperties(new RestListener< List<Location>>() {
            Double minLon;
            Double maxLon;

            Double minLat;
            Double maxLat;

            @Override
            public void OnRestSuccess( List<Location> locationList) {

                if (locationList == null || locationList.size() <= 0){
                    return;
                }
                minLon = locationList.get(0).getCoordinates().get(0);
                maxLon = locationList.get(0).getCoordinates().get(0);

                minLat = locationList.get(0).getCoordinates().get(1);
                maxLat = locationList.get(0).getCoordinates().get(1);

                for(Location location : locationList){
                    List<Double> coordinates = location.getCoordinates();

                    //String locationName = location.getName();
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

                    /*====================================================
                     * Set
                     *====================================================*/
                    final String labelText = locationName + "\nCity: " + city + "\nStreet: " + street + " " + housenumber;

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

                    marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker, MapView mapView) {

                            //Toast.makeText(getApplicationContext(),labelText,Toast.LENGTH_LONG).show();
                            marker.showInfoWindow();
                            InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            in.hideSoftInputFromWindow(mMapView.getWindowToken(), 0);

                            String locationName = marker.getTitle();
                            double latitude = marker.getPosition().getLatitude();
                            double longitude= marker.getPosition().getLongitude();

                            TripLocation tripLocation = new TripLocation(locationName,latitude,longitude);
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                            intent.putExtra("Location",tripLocation);

                            setResult(Activity.RESULT_OK,intent);
                            finish();
                            return true;
                        }
                    });

                }

                if (locationList.size() > 1) {
                    //BoundingBoxE6 boundingBox = new BoundingBoxE6(north, east, south, west);
                    mMapView.zoomToBoundingBox(new BoundingBox(maxLat, maxLon, minLat, minLon), true,150);
                }else{
                    mMapController.setCenter(new GeoPoint(minLat,minLon));
                }
            }

            @Override
            public void OnRestFail() {
                Context context = getApplicationContext();
                Toast.makeText(context,"Could not load Positions",Toast.LENGTH_LONG);
            }
        });
    }
}

