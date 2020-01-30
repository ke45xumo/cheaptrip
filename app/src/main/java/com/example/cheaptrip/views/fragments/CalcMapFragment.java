package com.example.cheaptrip.views.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cheaptrip.R;
import com.example.cheaptrip.handlers.rest.RestListener;
import com.example.cheaptrip.handlers.rest.geo.GeoDirectionsHandler;
import com.example.cheaptrip.handlers.view.MyKmlStyler;
import com.example.cheaptrip.handlers.view.adapters.TripRouteListAdapter;
import com.example.cheaptrip.models.TripLocation;
import com.example.cheaptrip.models.TripRoute;
import com.example.cheaptrip.models.TripVehicle;
import com.example.cheaptrip.models.orservice.ORServiceResponse;
import com.example.cheaptrip.models.tankerkoenig.Station;
import com.example.cheaptrip.services.GPSService;
import com.google.gson.Gson;

import org.osmdroid.api.IMapController;
import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.bonuspack.kml.KmlFeature;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.Polygon;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CalcMapFragment extends Fragment {
    private static MapView  mMapView = null;
    private static IMapController mMapController = null;

    private String startEndRouteJSON;

    TripLocation startLocation;
    TripLocation endLocation;

    TripVehicle tripVehicle;

    private String mCurrentRouteAsJSON;                 // JSON defining the route

    private ArrayList<TripLocation> mTripLocationList;        // List of tripLocations on the list


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("mTripLocationList", mTripLocationList);
        outState.putString("mCurrentRouteAsJSON", mCurrentRouteAsJSON);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_calc_map, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);

        startLocation = (TripLocation) getActivity().getIntent().getSerializableExtra("start");
        endLocation = (TripLocation) getActivity().getIntent().getSerializableExtra("end");

        tripVehicle = (TripVehicle) getActivity().getIntent().getSerializableExtra("tripVehicle");

        mMapView = view.findViewById(R.id.mapView);

        if(savedInstanceState != null) {
            mTripLocationList = (ArrayList<TripLocation>) savedInstanceState.getSerializable("mTripLocationList");
            mCurrentRouteAsJSON = (String) savedInstanceState.getSerializable("mCurrentRouteAsJSON");
        }
        /*============================================================
         * Init the Views
         *============================================================*/
        initMap();
        getDirections();


        /*=================================================================
         * Draw the route
         *=================================================================*/
        if(mCurrentRouteAsJSON != null && mCurrentRouteAsJSON.length() > 0){
            drawRoute(mCurrentRouteAsJSON,Color.GREEN);

        }
        /*================================================================
         * Draw the Markers
         *=================================================================*/
        if(mTripLocationList != null && !mTripLocationList.isEmpty()){
            for(TripLocation tripLocation : mTripLocationList){
                drawMarker(tripLocation,R.drawable.marker_default);
            }
        }

/*
        RouteService routeService = new RouteService(this,tripVehicle);
        routeService.execute(startLocation,endLocation);*/

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

    public void clearMap(){
        if(mMapView == null){
            Log.e("Cheaptrip", "Cannot clear Map. mMapView is null");
            return;
        }

        mMapView.getOverlays().clear();
    }

    private void initMap(){
        mMapView.setTileSource(TileSourceFactory.MAPNIK);
        mMapView.setMultiTouchControls(true);

        GPSService gpsService = new GPSService(getActivity().getApplicationContext());

        if (gpsService.canGetLocation()) {

            mMapController = mMapView.getController();
            mMapController.setZoom(20.0);
            GeoPoint startPoint = new GeoPoint(startLocation.getLatitdue(), startLocation.getLongitude());
            mMapController.setCenter(startPoint);

            drawMarker(startLocation,R.drawable.marker_default);
            drawMarker(endLocation,R.drawable.marker_default);

        }

    }

    public void getDirections(){

        List<TripLocation> tripLocationList = new ArrayList<>();
        tripLocationList.add(startLocation);
        tripLocationList.add(endLocation);

        GeoDirectionsHandler geoDirectionsHandler = new GeoDirectionsHandler(tripLocationList,null);


        geoDirectionsHandler.makeAsyncRequest(new RestListener<TripRoute>() {
            @Override
            public void OnRestSuccess(TripRoute tripRoute) {
                if(tripRoute == null){
                    Log.e("CHEAPTRIP","Received null as ORService response.");
                    return;
                }

                String geoJSON = tripRoute.getGeoJSON();
                startEndRouteJSON = geoJSON;
                drawRoute(geoJSON, Color.GREEN);
                setDirectionBbox(geoJSON);
            }

            @Override
            public void OnRestFail() {
                Log.e("CHEAPTRIP","Error getting Directions from OpenRoute Service.");

            }
        });
    }


    public void drawMarker(TripLocation tripLocation, int resourceIcon){
        Marker marker= new Marker(mMapView);
        marker.setPosition(new GeoPoint(tripLocation.getLatitdue(),tripLocation.getLongitude()));
        marker.setTitle(tripLocation.getLocationName());

        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        marker.setIcon(getResources().getDrawable(resourceIcon));

        mMapView.getOverlays().add(marker);
        mMapView.invalidate();
    }

    public void drawRoute(String geoJSON, int color){
        if (geoJSON != null && geoJSON.length() > 0) {
            try {
                KmlDocument kmlDocument = new KmlDocument();
                kmlDocument.parseGeoJSON(geoJSON);
                KmlFeature.Styler styler = new MyKmlStyler(color);
                FolderOverlay myOverLay = (FolderOverlay) kmlDocument.mKmlRoot.buildOverlay(mMapView, null, styler, kmlDocument);
                mMapView.getOverlays().add(myOverLay);
                mMapView.invalidate();


                setDirectionBbox(geoJSON);
            }catch(IllegalStateException e){
                Log.e("CHEAPTRIP","Cannot draw Route from GeoJSON: " +  e.getLocalizedMessage());
                return;
            }
        }
    }

    private static void setDirectionBbox(String geoJSON){
        if(geoJSON == null || geoJSON.length() < 1){
            Log.e("CHEAPTRIP", "Cannot set BoundingBox from Empty GeoJSON String");
            return;
        }

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

    public void onDirectionsLoaded(String geoJSON){
        startEndRouteJSON = geoJSON;
        drawRoute(geoJSON,Color.GREEN);
        setDirectionBbox(geoJSON);
    }

    public void drawRange(TripLocation centeredLocation, final double radius) {
        /*-------------------------------------------------------------------------
         * Draw the a circle for the max Radius to reach with current tank contents
         *-------------------------------------------------------------------------*/
        drawMarker(startLocation,R.drawable.marker_default);
        ArrayList<OverlayItem> overlayItems = new ArrayList<>();
        OverlayItem overlayCircle = new OverlayItem("Radius", null, new GeoPoint(centeredLocation.getLatitdue(), centeredLocation.getLongitude()));
        overlayItems.add(overlayCircle);

        GeoPoint startIGeoPoint = new GeoPoint(centeredLocation.getLatitdue(), centeredLocation.getLongitude());

        double radiusInMeters = radius * 1000;
        List<GeoPoint> circle = Polygon.pointsAsCircle(startIGeoPoint, radiusInMeters);

        Polygon polygon = new Polygon();
        polygon.setPoints(circle);

        mMapView.getOverlays().add(polygon);
        mMapView.invalidate();
    }

    public void drawStations(List<Station> stations){
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



    public void updateCurrentRoute(String geoJSON, boolean draw) {
        this.mCurrentRouteAsJSON = geoJSON;
        if(startEndRouteJSON != null){
            drawRoute(startEndRouteJSON, Color.BLACK);
        }

        if(draw) {
            drawRoute(geoJSON, Color.GREEN);
        }
    }

    public void updateMarkers(List<TripLocation> tripLocationList, boolean draw) {
        this.mTripLocationList = (ArrayList<TripLocation>)tripLocationList;

        if(draw){
            for(TripLocation tripLocation : tripLocationList){
                drawMarker(tripLocation,R.drawable.marker_default);
            }
        }
    }
}
