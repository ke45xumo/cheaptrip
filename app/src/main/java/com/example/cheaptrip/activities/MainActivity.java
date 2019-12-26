package com.example.cheaptrip.activities;



import android.content.Intent;

import android.content.pm.PackageManager;

import android.os.Build;
import android.os.Bundle;


import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import android.widget.ImageView;

import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.cheaptrip.R;


import com.example.cheaptrip.database.VehicleDatabase;
import com.example.cheaptrip.handlers.LocationTextHandler;
import com.example.cheaptrip.models.TripLocation;
import com.example.cheaptrip.models.TripVehicle;



//TODO:https://github.com/Q42/AndroidScrollingImageView
public class MainActivity extends AppCompatActivity {
    // Request Codes for the Acitivities
    final static int ACTIVITY_REQ_CODE_BRAND    = 1;
    final static int ACTIVITY_REQ_CODE_MODEL    = 2;
    final static int ACTIVITY_REQ_CODE_YEAR     = 3;
    final static int ACTIVITY_REQ_CODE_START    = 4;
    final static int ACTIVITY_REQ_CODE_END      = 5;
    final static int ACTIVITY_REQ_CODE_CALC     = 6;


    Button btn_carBrand;
    Button btn_carModel;
    Button btn_carYear;

    AutoCompleteTextView edit_start;
    AutoCompleteTextView edit_end;

    SeekBar seek_tankContents;

    String str_Brand;
    String str_Model;
    String str_Year;

    TripLocation  startLocation;
    TripLocation  endLocation;

    ImageView pic;

    VehicleDatabase appDatabase;

    protected double currLatitude;
    protected double currLongitude;


    TripVehicle tripVehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        assignViewObjects();
        initLocationHandler();
        appDatabase = initDatabase();
        tripVehicle = new TripVehicle();

    }

    private void assignViewObjects(){
        /*===============================================
         * Assign Views
         *===============================================*/
        btn_carBrand = findViewById(R.id.btn_car_brand);
        btn_carModel = findViewById(R.id.btn_car_model);
        btn_carYear = findViewById(R.id.btn_car_year);

        pic = findViewById(R.id.img_gas_pump);

        edit_start = findViewById(R.id.edit_start);
        edit_end = findViewById(R.id.edit_destination);

        seek_tankContents = findViewById(R.id.seek_gas_contents);
    }

    private void initLocationHandler(){
        /*=================================================
         * Auto Completion
         *=================================================*/
        LocationTextHandler locationTextHandler = new LocationTextHandler(currLatitude,currLongitude);

        startLocation = new TripLocation();
        endLocation= new TripLocation();

        locationTextHandler.setCurrentLocation(this,edit_start, startLocation );
        locationTextHandler.setCurrentLocation(this,edit_end, endLocation );

        locationTextHandler.addTextChangedListener(edit_start);
        locationTextHandler.addTextChangedListener(edit_end);
    }

    /**
     * This function is a Handler for all Views of activity_main2
     * @param view
     */
    public void viewClickHandler(View view){
        Intent intent;
        int requestCode;

        String txt_start  = edit_start.getText().toString();
        String txt_end  = edit_end.getText().toString();

        double tankPercent = (double)seek_tankContents.getProgress()/ (double)seek_tankContents.getMax();

        tripVehicle.setRemainFuelPercent(tankPercent);

        switch(view.getId()){
            case R.id.btn_car_brand:        intent = new Intent(this, CarBrandActivity.class);
                                            btn_carModel.setEnabled(true);
                                            requestCode = ACTIVITY_REQ_CODE_BRAND;
                                            break;

            case R.id.btn_car_model:        // TODO Check for str_carBrand
                                            intent = new Intent(this, CarModelActivity.class);
                                            intent.putExtra("brand",str_Brand);
                                            requestCode = ACTIVITY_REQ_CODE_MODEL;
                                            break;

            case R.id.btn_car_year:         intent = new Intent(this, CarYearActivity.class);
                                            requestCode = 3;
                                            break;



            case R.id.btn_start_location:   intent = new Intent(this, MapActivity.class);
                                            intent.putExtra("location_name",txt_start);
                                            requestCode = ACTIVITY_REQ_CODE_START;
                                            break;

            case R.id.btn_end_location:     intent = new Intent(this, MapActivity.class);
                                            intent.putExtra("lat", currLatitude);
                                            intent.putExtra("lon", currLongitude);
                                            intent.putExtra("location_name",txt_end);

                                            requestCode = ACTIVITY_REQ_CODE_END;
                                            break;
            case R.id.btn_find:             intent = new Intent(this, CalculationActivity.class);
                                            intent.putExtra("start", startLocation);
                                            intent.putExtra("end", endLocation);
                                            intent.putExtra("tripVehicle",tripVehicle);
                                            requestCode = ACTIVITY_REQ_CODE_CALC;
                                            break;
            default:                        return;


        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Apply activity transition
/*
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(this, btn_carBrand, "robot");
            startActivityForResult(intent, requestCode,  options.toBundle());
  */
            startActivityForResult(intent, requestCode);
        } else {
            // Swap without transition
            startActivityForResult(intent, requestCode);
        }

        //finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK){
            return;
        }


        switch (requestCode){

            case ACTIVITY_REQ_CODE_BRAND:       str_Brand = data.getStringExtra("selection");
                                                btn_carBrand.setText(str_Brand);
                                                tripVehicle.setBrand(str_Brand);
                                                break;


            case ACTIVITY_REQ_CODE_MODEL:       str_Model = data.getStringExtra("selection");
                                                btn_carModel.setText(str_Model);
                                                tripVehicle.setModel(str_Model);
                                                break;

            case ACTIVITY_REQ_CODE_YEAR:        str_Year = data.getStringExtra("selection");
                                                btn_carYear.setText(str_Year);
                                                tripVehicle.setYear(str_Year);
                                                break;

            case ACTIVITY_REQ_CODE_START:       startLocation = (TripLocation) data.getSerializableExtra("Location");
                                                String strStart = startLocation.getLocationName();
                                                edit_start.setText(strStart);
                                                break;

            case ACTIVITY_REQ_CODE_END:         endLocation = (TripLocation) data.getSerializableExtra("Location");
                                                String strEnd = endLocation.getLocationName();
                                                edit_end.setText(strEnd);
                                                break;

            default:    break;
        }

    }

    /**
     * Starts the Calculation Activity
     *
     * @param view
     */
    protected void startCalculation(View view){
        if (str_Year == null){
            Toast.makeText(this,"Construction Year not set!", Toast.LENGTH_LONG).show();
            return;
        }
        if (str_Brand == null){
            Toast.makeText(this,"Car Brand not set!", Toast.LENGTH_LONG).show();
            return;
        }
        if (str_Model == null){
            Toast.makeText(this,"Car Model not set!", Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent(this, CalculationActivity.class);
        intent.putExtra("brand",str_Brand);
        intent.putExtra("model",str_Brand);
        intent.putExtra("year",str_Brand);

        intent.putExtra("StartLocation",startLocation);
        intent.putExtra("EndLocation", endLocation);

        startActivity(intent);
    }
    private VehicleDatabase initDatabase(){
        VehicleDatabase db = Room.databaseBuilder(getApplicationContext(),
                VehicleDatabase.class, "database-name").build();

        return db;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 4711) {

            // Checking whether user granted the permission or not.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Showing the toast message
                Toast.makeText(MainActivity.this,
                        "Location Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                Toast.makeText(MainActivity.this,
                        "Location Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }

    }
}
