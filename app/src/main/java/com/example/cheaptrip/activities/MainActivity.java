package com.example.cheaptrip.activities;


import android.app.ActivityOptions;
import android.content.Intent;

import android.content.pm.PackageManager;

import android.os.Bundle;


import android.util.Pair;
import android.view.MenuInflater;
import android.view.View;

import android.widget.Button;

import android.widget.EditText;
import android.widget.ImageView;

import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.room.Room;

import com.example.cheaptrip.BuildConfig;
import com.example.cheaptrip.R;


import com.example.cheaptrip.database.VehicleDatabase;
import com.example.cheaptrip.models.TripLocation;
import com.example.cheaptrip.models.TripVehicle;
import com.example.cheaptrip.views.Gauge;


//TODO:https://github.com/Q42/AndroidScrollingImageView
public class MainActivity extends AppCompatActivity {
    // Request Codes for the Acitivities
    final static int ACTIVITY_REQ_CODE_BRAND    = 1;
    final static int ACTIVITY_REQ_CODE_MODEL    = 2;
    final static int ACTIVITY_REQ_CODE_YEAR     = 3;
    final static int ACTIVITY_REQ_CODE_START    = 4;
    final static int ACTIVITY_REQ_CODE_END      = 5;
    final static int ACTIVITY_REQ_CODE_CALC     = 6;

    Gauge gauge;

    Button btn_carBrand;
    Button btn_carModel;
    Button btn_carYear;

    EditText edit_start;
    EditText edit_end;

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
        setContentView(R.layout.activity_main);
        assignViewObjects();
        appDatabase = initDatabase();
        tripVehicle = new TripVehicle();


        /*======================================
         * Set tripVehicle (in debug mode)
         *======================================*/
        if (BuildConfig.DEBUG) {
            if(tripVehicle == null || tripVehicle.getBrand() == null || tripVehicle.getModel() == null) {

                btn_carBrand.setText("BMW");
                btn_carModel.setText("318i");
                tripVehicle = new TripVehicle();
                tripVehicle.setBrand("BMW");
                tripVehicle.setModel("318i");
            }
        }

    }


    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu, popup.getMenu());
        popup.show();
    }


    private void assignViewObjects(){
        /*===============================================
         * Assign Views
         *===============================================*/
        btn_carBrand = findViewById(R.id.btn_car_brand);
        btn_carModel = findViewById(R.id.btn_car_model);
        btn_carYear = findViewById(R.id.btn_car_year);

        //pic = findViewById(R.id.img_gas_pump);

        edit_start = findViewById(R.id.edit_start);
        edit_end = findViewById(R.id.edit_destination);

        seek_tankContents = findViewById(R.id.seek_gas_contents);

        gauge = findViewById(R.id.img_gas_pump);
    }


    /**
     * This function is a Handler for all Views of activity_main
     * @param view
     */
    public void viewClickHandler(View view){
        Intent intent;
        int requestCode;
        Bundle optionsBundle = null;    // For Transition
        String txt_start  = edit_start.getText().toString();
        String txt_end  = edit_end.getText().toString();

       // double tankPercent = (double)seek_tankContents.getProgress()/ (double)seek_tankContents.getMax();
        double tankPercent = (double)gauge.getProgress()/ (double)seek_tankContents.getMax();
        tripVehicle.setRemainFuelPercent(tankPercent);

        switch(view.getId()){
            case R.id.btn_car_brand:
                intent = new Intent(this, VehicleBrandActivity.class);
                btn_carModel.setEnabled(true);
                requestCode = ACTIVITY_REQ_CODE_BRAND;
                break;

            case R.id.btn_car_model:
                // TODO Check for str_carBrand
                intent = new Intent(this, VehicleModelActivity.class);
                intent.putExtra("brand",str_Brand);
                requestCode = ACTIVITY_REQ_CODE_MODEL;
                break;

            case R.id.btn_car_year:
                intent = new Intent(this, VehicleYearActivity.class);
                requestCode = 3;
                break;



            case R.id.edit_start:
                intent = new Intent(this, MapActivity.class);
                intent.putExtra("location_name",txt_start);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

                    Pair<View,String> pairImageMap = Pair.create(findViewById(R.id.img_gas_pump),"image_to_map");
                    Pair<View,String> pairEditStart = Pair.create((View)edit_start,"edit_start");
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,pairEditStart,pairImageMap);
                    optionsBundle = options.toBundle();
                }

                requestCode = ACTIVITY_REQ_CODE_START;
                break;

            case R.id.edit_destination:
                intent = new Intent(this, MapActivity.class);
                intent.putExtra("lat", currLatitude);
                intent.putExtra("lon", currLongitude);
                intent.putExtra("location_name",txt_end);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

                    Pair<View,String> pairImageMap = Pair.create(findViewById(R.id.img_gas_pump),"image_to_map");
                    Pair<View,String> pairEditStart = Pair.create((View)edit_end,"edit_end");
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,pairEditStart,pairImageMap);
                    optionsBundle = options.toBundle();
                }

                requestCode = ACTIVITY_REQ_CODE_END;
                break;


            case R.id.btn_find:

                boolean bIsIncomplete = false;
                String toastText = "The following Porperties must be set:\n";
                if(startLocation == null){
                    toastText += " - Start Location\n";
                }
                if(endLocation == null){
                    toastText += " - Destination/ End Location\n";
                    bIsIncomplete = true;
                }

                if(tripVehicle.getBrand() == null){
                    toastText += " - Brand of the Vehicle\n";
                    bIsIncomplete = true;
                }

                if(tripVehicle.getModel() == null){
                    toastText += " - Model of the Vehicle\n";
                    bIsIncomplete = true;
                }

                if (bIsIncomplete == true){
                    Toast.makeText(this, toastText,Toast.LENGTH_LONG).show();
                    return;
                }

                intent = new Intent(this, CalculationActivity.class);
                intent.putExtra("start", startLocation);
                intent.putExtra("end", endLocation);
                intent.putExtra("tripVehicle",tripVehicle);
                requestCode = ACTIVITY_REQ_CODE_CALC;
                break;
            default:                        return;


        }

        startActivityForResult(intent, requestCode, optionsBundle);
        
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
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // Showing the toast message
                Toast.makeText(MainActivity.this,"Location Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(MainActivity.this,"Location Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
