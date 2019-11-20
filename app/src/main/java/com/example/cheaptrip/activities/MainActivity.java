package com.example.cheaptrip.activities;


import android.content.Intent;

import android.content.pm.PackageManager;

import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import android.widget.ImageView;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.cheaptrip.R;

import com.example.cheaptrip.dao.GeoCompletionClient;
import com.example.cheaptrip.database.VehicleDatabase;

import com.example.cheaptrip.handlers.LocationTextHandler;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;

//TODO:https://github.com/Q42/AndroidScrollingImageView
public class MainActivity extends AppCompatActivity {
    Button btn_carBrand;
    Button btn_carModel;
    Button btn_carYear;

    AutoCompleteTextView edit_start;
    AutoCompleteTextView edit_end;

    String str_Brand;
    String str_Model;
    String str_Year;

    ImageView pic;

    VehicleDatabase appDatabase;
    List<String> suggestions;

    protected double currLatitude;
    protected double currLongitude;

    Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        //getActionBar().hide();
        /*===============================================
         * Assign Views
         *===============================================*/
        btn_carBrand = findViewById(R.id.btn_car_brand);
        btn_carModel = findViewById(R.id.btn_car_model);
        btn_carYear = findViewById(R.id.btn_car_year);

        pic = findViewById(R.id.img_gas_pump);

        edit_start = findViewById(R.id.edit_start);
        edit_end = findViewById(R.id.edit_destination);

        appDatabase = initDatabase();
        /*=================================================
         * Auto Completion
         *=================================================*/
        //suggestions =  new ArrayList<>(); //autocomplete Suggestions
        LocationTextHandler locationTextHandler = new LocationTextHandler(currLatitude,currLongitude);
        locationTextHandler.setCurrentLocation(this,edit_start);

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

        switch(view.getId()){
            case R.id.btn_car_brand:    intent = new Intent(this, CarBrandActivity.class);
                                        btn_carModel.setEnabled(true);
                                        requestCode = 1;
                                        break;

            case R.id.btn_car_model:    // TODO Check for str_carBrand
                                        intent = new Intent(this, CarModelActivity.class);
                                        intent.putExtra("brand",str_Brand);
                                        requestCode = 2;
                                        break;

            case R.id.btn_car_year:     intent = new Intent(this, CarYearActivity.class);
                                        requestCode = 3;
                                        break;

            case R.id.btn_find:         intent = new Intent(this, CalculationActivity.class);
                                        intent.putExtra("start", txt_start);
                                        intent.putExtra("end",txt_end);
                                        intent.putExtra("brand",str_Brand);
                                        intent.putExtra("model",str_Brand);
                                        intent.putExtra("year",str_Brand);

                                        requestCode = 4;
                                        break;

            case R.id.btn_start_location:   intent = new Intent(this, MapActivity.class);
                                            //intent.putExtra("lat", currLatitude);
                                            //intent.putExtra("lon", currLongitude);
                                            intent.putExtra("location_name",txt_start);
                                            requestCode = 5;
                                            break;

            case R.id.btn_end_location:     intent = new Intent(this, MapActivity.class);
                                            intent.putExtra("lat", currLatitude);
                                            intent.putExtra("lon", currLongitude);
                                            intent.putExtra("location_name",txt_end);

                                            requestCode = 6;
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

        String str_listSelection = data.getStringExtra("selection");

        switch (requestCode){
            case 1:     str_Brand = str_listSelection;
                btn_carBrand.setText(str_Brand);
                break;


            case 2:     str_Model = str_listSelection;
                btn_carModel.setText(str_Model);
                break;

            case 3:     str_Year = str_listSelection;
                btn_carYear.setText(str_Year);
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

        startActivity(intent);
    }
    private VehicleDatabase initDatabase(){
        VehicleDatabase db = Room.databaseBuilder(getApplicationContext(),
                VehicleDatabase.class, "database-name").build();

        return db;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super
                .onRequestPermissionsResult(requestCode,
                        permissions,
                        grantResults);

        if (requestCode == 4711) {

            // Checking whether user granted the permission or not.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Showing the toast message
                Toast.makeText(MainActivity.this,
                        "Camera Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                Toast.makeText(MainActivity.this,
                        "Camera Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }

    }
}
