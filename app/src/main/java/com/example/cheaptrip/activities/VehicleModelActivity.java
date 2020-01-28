package com.example.cheaptrip.activities;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.cheaptrip.R;
import com.example.cheaptrip.app.CheapTripApp;
import com.example.cheaptrip.handlers.view.adapters.VehicleModelAdapter;
import com.example.cheaptrip.handlers.rest.RestListener;
import com.example.cheaptrip.handlers.rest.vehicle.VehicleModelHandler;
import com.example.cheaptrip.models.nhtsa.VehicleModel;


import java.util.List;



//https://www.codeproject.com/Articles/5165136/Retrofit-A-REST-Client-for-Android-Retrofit-2-X
//TODO: https://stackoverflow.com/questions/29380844/how-to-set-timeout-in-retrofit-library
//TODO: Search for Vehicle in Edit Text
public class VehicleModelActivity extends ListActivity {
    private ProgressBar progressBar;
    private EditText edit_searchModel;      // Edit Field for searching Models

    private String vehicleBrand;            // Previous Selected VehicleBrand

    private static VehicleModelAdapter listDataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((CheapTripApp)getApplication()).setCurrentActivity(this);
        setContentView(R.layout.activity_car_model);

        progressBar = findViewById(R.id.progress_model);
        Bundle extras = getIntent().getExtras();

        vehicleBrand = extras.getString("brand");

        edit_searchModel = findViewById(R.id.edit_model);
        setModelListView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        CheapTripApp cheapTripApp = (CheapTripApp) getApplication();
        Activity currActivity = cheapTripApp.getCurrentActivity() ;

        if ( this .equals(currActivity))
            cheapTripApp.setCurrentActivity( null ) ;
    }

    public void onResume(){
        super.onResume();
        CheapTripApp cheapTripApp = (CheapTripApp) getApplication();
        cheapTripApp .setCurrentActivity( this ) ;
    }

    public void onPause(){
        super.onPause();
        CheapTripApp cheapTripApp = (CheapTripApp) getApplication();
        Activity currActivity = cheapTripApp.getCurrentActivity() ;

        if ( this .equals(currActivity))
            cheapTripApp.setCurrentActivity( null ) ;
    }

    /**
     * TODO: Document
     * @param l
     * @param v
     * @param position
     * @param id
     */
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        VehicleModel selectedItem = (VehicleModel) getListView().getItemAtPosition(position);

        Intent intent = new Intent(this, MainActivity.class);
        //Bundle extras = new Bundle();

        intent.putExtra("selection", selectedItem.getModelName());
        setResult(Activity.RESULT_OK,intent);
        finish();
    }

    public void setModelListView() {
        VehicleModelHandler vehicleBrandHandler = new VehicleModelHandler(vehicleBrand);

        vehicleBrandHandler.makeAsyncRequest(new RestListener<List<VehicleModel>>() {
            @Override
            public void OnRestSuccess(List<VehicleModel> modelList) {
                listDataAdapter = new VehicleModelAdapter(getApplicationContext(), modelList);
                setListAdapter(listDataAdapter);
                progressBar.setVisibility(View.INVISIBLE);

                edit_searchModel.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        listDataAdapter.getFilter().filter(s.toString());
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        listDataAdapter.getFilter().filter(s.toString());
                    }
                });
            }

            @Override
            public void OnRestFail() {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "An Error Occurred", Toast.LENGTH_LONG).show();
            }
        });

    }
}
