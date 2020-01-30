package com.example.cheaptrip.activities;


import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import androidx.viewpager.widget.ViewPager;

import com.example.cheaptrip.R;
import com.example.cheaptrip.app.CheapTripApp;
import com.example.cheaptrip.handlers.rest.RestListener;
import com.example.cheaptrip.handlers.rest.geo.GeoDirectionsHandler;
import com.example.cheaptrip.views.fragments.CalcGasStationFragment;
import com.example.cheaptrip.views.fragments.CalcMapFragment;
import com.example.cheaptrip.views.fragments.CalcRouteFragment;
import com.example.cheaptrip.handlers.CalculationListener;

import com.example.cheaptrip.handlers.view.adapters.CalcViewPagerAdapter;


import com.example.cheaptrip.handlers.view.adapters.TripRouteListAdapter;
import com.example.cheaptrip.models.TripGasStation;
import com.example.cheaptrip.models.TripLocation;
import com.example.cheaptrip.models.TripRoute;
import com.example.cheaptrip.models.TripVehicle;

import com.example.cheaptrip.services.RouteService;

import com.google.android.material.tabs.TabLayout;


import org.osmdroid.api.IMapController;

import java.util.List;


public class CalculationActivity extends AppCompatActivity {

    private static IMapController mMapController = null;

    private ListView lvRoutes;
    private TripRouteListAdapter tripRouteListAdapter;

    private ProgressBar progressBar;
    TripLocation startLocation;
    TripLocation endLocation;

    TripVehicle tripVehicle;

    private CalcGasStationFragment mStationFragment;
    private CalcMapFragment mMapFragment;
    private CalcRouteFragment mRouteFragment;

    private ViewPager mViewPager;


    /**
     * This function will be called on Activity creation
     * It takes care of initializing the views attached to the layout
     * and starts calculating routes with a gas station as intermediate stop
     *
     * @param savedInstanceState    Bundle, containing the state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((CheapTripApp)getApplication()).setCurrentActivity(this);
        /*============================================================
         * Init the Views
         *============================================================*/
        setContentView(R.layout.activity_calculation);

        startLocation = (TripLocation) getIntent().getSerializableExtra("start");
        endLocation = (TripLocation) getIntent().getSerializableExtra("end");

        tripVehicle = (TripVehicle) getIntent().getSerializableExtra("tripVehicle");
        progressBar = findViewById(R.id.progress_brand);
        lvRoutes = findViewById(R.id.list_routes);

        mViewPager = findViewById(R.id.viewpager_calc);
        /*============================================================
         * Start Actions
         *============================================================*/
        initFragments();
        initList();
        startCalculation();

    }

    private void initFragments(){
        mMapFragment = new CalcMapFragment();
        mStationFragment = new CalcGasStationFragment();
        mRouteFragment = new CalcRouteFragment();


        CalcViewPagerAdapter calcViewPagerAdapter = new CalcViewPagerAdapter(getSupportFragmentManager());
        calcViewPagerAdapter.addFragment(mStationFragment,"Station");
        calcViewPagerAdapter.addFragment(mMapFragment,"Map");
        calcViewPagerAdapter.addFragment(mRouteFragment,"Route");

        mViewPager.setAdapter(calcViewPagerAdapter);
        TabLayout tabLayout = findViewById(R.id.tab_layout_calc);

        mViewPager.setCurrentItem(1);

        tabLayout.setupWithViewPager(mViewPager);
    }

    private void initList(){
        tripRouteListAdapter = new TripRouteListAdapter(this);
        lvRoutes.setAdapter(tripRouteListAdapter);

        lvRoutes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mMapFragment.clearMap();

                final TripRoute oldTripRoute = (TripRoute)lvRoutes.getItemAtPosition(position);

                if(oldTripRoute.getGeoJSON() != null){
                    fillFragments(oldTripRoute);
                    return;
                }


                final GeoDirectionsHandler geoDirectionsHandler = new GeoDirectionsHandler(oldTripRoute.getStops(),oldTripRoute);

                geoDirectionsHandler.makeAsyncRequest(new RestListener<TripRoute>() {
                    @Override
                    public void OnRestSuccess(TripRoute tripRoute) {
                        List<TripRoute> tripRouteList = tripRouteListAdapter.getTripRouteList();
                        int index = tripRouteList.indexOf(oldTripRoute);

                        tripRouteList.set(index,tripRoute);

                        tripRouteListAdapter.setTripRouteList(tripRouteList);
                        tripRouteListAdapter.notifyDataSetChanged();

                        fillFragments(tripRoute);
                    }

                    @Override
                    public void OnRestFail() {

                    }
                });




            }
        });
    }

    private void startCalculation(){
        RouteService routeService = new RouteService(this, tripVehicle, new CalculationListener() {
            @Override
            public void onCalculationDone(List<TripRoute> tripRouteList) {
                tripRouteListAdapter.setTripRouteList(tripRouteList);
                tripRouteListAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        routeService.execute(startLocation, endLocation);
    }

    private void getPropertiesforTripRoute(TripRoute tripRoute){

        final TripRoute oldTripRoute = tripRoute;
        final GeoDirectionsHandler geoDirectionsHandler = new GeoDirectionsHandler(tripRoute.getStops(),tripRoute);

        geoDirectionsHandler.makeAsyncRequest(new RestListener<TripRoute>() {
            @Override
            public void OnRestSuccess(TripRoute tripRoute) {
                List<TripRoute> tripRouteList = tripRouteListAdapter.getTripRouteList();
                int index = tripRouteList.indexOf(oldTripRoute);

                tripRouteList.set(index,tripRoute);

                tripRouteListAdapter.setTripRouteList(tripRouteList);
                tripRouteListAdapter.notifyDataSetChanged();
            }

            @Override
            public void OnRestFail() {

            }
        });

    }

    private void fillFragments(TripRoute tripRoute){
        boolean draw = false;
        if(mViewPager.getCurrentItem() == 1){
            draw = true;
        }

        String geoJSON = tripRoute.getGeoJSON();

        mMapFragment.updateCurrentRoute(geoJSON,draw);
        mMapFragment.updateMarkers(tripRoute.getStops(),draw);
        //mMapFragment.drawRoute(geoJSON,Color.GREEN);

        //mMapFragment.drawRoute(startEndRouteJSON,Color.BLACK);

        for(TripLocation tripLocation : tripRoute.getStops()){
            if(tripLocation instanceof TripGasStation){
                mStationFragment.setGasStationInfo((TripGasStation)tripLocation);
            }
        }

        mRouteFragment.updateList(tripRoute);
    }

}
