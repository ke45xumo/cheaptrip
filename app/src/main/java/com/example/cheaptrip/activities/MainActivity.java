package com.example.cheaptrip.activities;


import android.app.Activity;
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


import com.example.cheaptrip.app.CheapTripApp;
import com.example.cheaptrip.dao.GasStationClient;
import com.example.cheaptrip.dao.VehicleDatabaseClient;
import com.example.cheaptrip.database.VehicleDatabase;
import com.example.cheaptrip.models.TripLocation;
import com.example.cheaptrip.models.TripVehicle;
import com.example.cheaptrip.models.fueleconomy.VehicleDataSet;
import com.example.cheaptrip.views.Gauge;

import java.util.ArrayList;
import java.util.List;


//TODO:https://github.com/Q42/AndroidScrollingImageView
public class MainActivity extends AppCompatActivity {
    // Request Codes for the Acitivities
    final static int ACTIVITY_REQ_CODE_BRAND    = 1;
    final static int ACTIVITY_REQ_CODE_MODEL    = 2;
    final static int ACTIVITY_REQ_CODE_YEAR     = 3;
    final static int ACTIVITY_REQ_CODE_START    = 4;
    final static int ACTIVITY_REQ_CODE_END      = 5;
    final static int ACTIVITY_REQ_CODE_CALC     = 6;

    Gauge gauge;                // Tank capacity view

    Button btn_carBrand;        // Selection for car brands
    Button btn_carModel;        // Selection for car models
    Button btn_carYear;         // Selection for car construction year

    EditText edit_start;        // Edit-Text for Starting Location input
    EditText edit_end;          // Edit-Text for Destination input

    String str_Brand;           // String to set on the button (btn_carBrand) after Brand selection
    String str_Model;           // String to set on the button (btn_carModel) after Model selection
    String str_Year;            // String to set on the button (btn_carYear) after Year selection

    TripLocation  startLocation;    // Selected Stating Location
    TripLocation  endLocation;      // Selected End Locaiton ( Destination


    TripVehicle tripVehicle;        // Vehicle to be created with the properties (Brand,Model,Year)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((CheapTripApp)getApplication()).setCurrentActivity(this);

        setContentView(R.layout.activity_main);
        assignViewObjects();
        tripVehicle = new TripVehicle();
        btn_carModel.setEnabled(false);
        btn_carYear.setEnabled(false);


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

        gauge = findViewById(R.id.tank_indicator);
    }


    /**
     * This function is a Handler for all Views of activity_main
     * @param view
     */
    public void viewClickHandler(View view){
        Intent intent;
        int requestCode;
        Bundle optionsBundle = null;    // For Transition


       // double tankPercent = (double)seek_tankContents.getProgress()/ (double)seek_tankContents.getMax();
        double tankPercent = (double)gauge.getProgress()/ 100;
        tripVehicle.setRemainFuelPercent(tankPercent);

        Bundle bundle = new Bundle();

        switch(view.getId()){
            case R.id.btn_car_brand:
                intent = new Intent(this, VehicleBrandActivity.class);

                bundle.putSerializable("vehicle",tripVehicle);
                intent.putExtras(bundle);
                requestCode = ACTIVITY_REQ_CODE_BRAND;
                break;

            case R.id.btn_car_model:
                // TODO Check for str_carBrand

                intent = new Intent(this, VehicleModelActivity.class);
                bundle.putSerializable("vehicle",tripVehicle);
                intent.putExtras(bundle);
                requestCode = ACTIVITY_REQ_CODE_MODEL;
                break;

            case R.id.btn_car_year:
                intent = new Intent(this, VehicleYearActivity.class);
                bundle.putSerializable("vehicle",tripVehicle);
                intent.putExtras(bundle);
                requestCode = 3;
                break;


            case R.id.edit_start:
                intent = new Intent(this, MapActivity.class);
                bundle.putSerializable("vehicle",tripVehicle);
                intent.putExtras(bundle);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

                    Pair<View,String> pairImageMap = Pair.create(findViewById(R.id.tank_indicator),"image_to_map");
                    Pair<View,String> pairEditStart = Pair.create((View)edit_start,"edit_start");
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(this,pairEditStart,pairImageMap);
                    optionsBundle = options.toBundle();
                }

                requestCode = ACTIVITY_REQ_CODE_START;
                break;

            case R.id.edit_destination:
                intent = new Intent(this, MapActivity.class);
                intent.putExtra("end", endLocation);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

                    Pair<View,String> pairImageMap = Pair.create(findViewById(R.id.tank_indicator),"image_to_map");
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

            case ACTIVITY_REQ_CODE_BRAND:       tripVehicle = (TripVehicle)data.getSerializableExtra("vehicle");
                                                str_Brand = tripVehicle.getBrand();
                                                btn_carBrand.setText(str_Brand);
                                                btn_carModel.setEnabled(true);
                                                break;


            case ACTIVITY_REQ_CODE_MODEL:       tripVehicle = (TripVehicle)data.getSerializableExtra("vehicle");
                                                str_Model = tripVehicle.getModel();
                                                btn_carModel.setText(str_Model);
                                                btn_carYear.setEnabled(true);
                                                break;

            case ACTIVITY_REQ_CODE_YEAR:        tripVehicle = (TripVehicle)data.getSerializableExtra("vehicle");
                                                str_Year = tripVehicle.getYear();
                                                btn_carYear.setText(str_Year);

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

    //TODO Implement + create Views
    private void setFuelTypeRadioButtons(TripVehicle tripVehicle){
        List<GasStationClient.FuelType> fuelTypeList = determineFuelType(tripVehicle);

        if(fuelTypeList.contains(GasStationClient.FuelType.DIESEL)){

        }

        if(fuelTypeList.contains(GasStationClient.FuelType.E5)){

        }
    }


    private List<GasStationClient.FuelType> determineFuelType(TripVehicle tripVehicle){
        List<GasStationClient.FuelType> fuelTypeList = new ArrayList<>();

        VehicleDatabaseClient dbClient = VehicleDatabase.getDatabase(this).vehicleDatabaseClient();

        String brand = tripVehicle.getBrand();
        String model = tripVehicle.getModel();
        String year = tripVehicle.getYear();


        VehicleDataSet vehicleDataSet = dbClient.findVehicle(brand,model,year);

        if(vehicleDataSet != null){
            if(vehicleDataSet.getConsumption_regular() != null){
                fuelTypeList.add(GasStationClient.FuelType.E5);
            }

            if(vehicleDataSet.getConsumption_diesel() != null){
                fuelTypeList.add(GasStationClient.FuelType.DIESEL);
            }
        }

        return fuelTypeList;

    }
}
