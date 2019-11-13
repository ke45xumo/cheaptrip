package com.example.cheaptrip.activities;

import android.content.Intent;

import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import android.widget.ImageView;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.cheaptrip.R;

import com.example.cheaptrip.dao.GeoCompletionClient;
import com.example.cheaptrip.database.VehicleDatabase;

import com.example.cheaptrip.models.retfrofit.photon.Feature;
import com.example.cheaptrip.models.retfrofit.photon.PhotonResponse;
import com.example.cheaptrip.models.retfrofit.photon.Properties;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

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

    ArrayAdapter<String> completeAdapter;
    List<String> suggestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
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

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://photon.komoot.de/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        final GeoCompletionClient carSpecClient = retrofit.create(GeoCompletionClient.class);


        /*=================================================
         * Auto Completion
         *=================================================*/
        suggestions =  new ArrayList<>(); //autocomplete Suggestions


        // Listener for the start AutoCompletionTextField
        edit_start.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //retrieveData(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                    getGeoLocations(carSpecClient, s, edit_start);
            }
        });

        // Listener for the end AutoCompletionTextField
        edit_end.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //retrieveData(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                    getGeoLocations(carSpecClient, s, edit_end);
            }
        });
    }

    /**
     * This function is a Handler for all Views of activity_main2
     * @param view
     */
    public void viewClickHandler(View view){
        Intent intent;
        int requestCode;

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
                                        String txt_start  = edit_start.getText().toString();
                                        String txt_end  = edit_end.getText().toString();

                                        intent.putExtra("start", txt_start);
                                        intent.putExtra("end",txt_end);

                                        intent.putExtra("brand",str_Brand);
                                        intent.putExtra("model",str_Brand);
                                        intent.putExtra("year",str_Brand);

                                        requestCode = 4;
            break;

            case R.id.btn_start_location:   intent = new Intent(this, MapActivity.class);
                                            requestCode = 5;
                                            break;

            case R.id.btn_end_location:     intent = new Intent(this, MapActivity.class);
                                            requestCode = 6;
                                            break;

            default:                    return;


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

    /**
     * Gets the Location Name (City) for Auto-Completion
     * @param client
     * @param enteredText
     */
    private void getGeoLocations(GeoCompletionClient client, Editable enteredText , final AutoCompleteTextView completeTextView){
        String a = enteredText.toString();
        a = a+"";
        Call<PhotonResponse> carResponseCall = client.geoPos(enteredText.toString());


        carResponseCall.enqueue(new Callback<PhotonResponse>() {
            @Override
            public void onResponse(Call<PhotonResponse> call, Response<PhotonResponse> response) {
                PhotonResponse photonResponse = response.body();
                List<Feature> features = photonResponse.getFeatures();

                List<String> locationNames = new ArrayList();

                for (Feature feature : features){
                    Properties properties = feature.getProperties();

                    String name = properties.getName();
                    String country = properties.getCountry();

                    String textField = name + "(" + country + ")";

                    locationNames.add(textField);
                }

                completeAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.select_dialog_item, locationNames);

                completeTextView.setAdapter(completeAdapter);
                suggestions = locationNames;
                completeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<PhotonResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"An Error Occurred", Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }
}
