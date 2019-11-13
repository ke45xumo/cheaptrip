package com.example.cheaptrip.activities;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.example.cheaptrip.dao.CarSpecClient;
import com.example.cheaptrip.R;
import com.example.cheaptrip.models.retfrofit.nhtsa.VehicleModel;
import com.example.cheaptrip.models.retfrofit.nhtsa.VehicleModelResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

//https://www.codeproject.com/Articles/5165136/Retrofit-A-REST-Client-for-Android-Retrofit-2-X
//TODO: https://stackoverflow.com/questions/29380844/how-to-set-timeout-in-retrofit-library
//TODO: Search for Vehicle in Edit Text
public class CarModelActivity extends ListActivity {
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_model);

        progressBar = findViewById(R.id.progress_model);


        Bundle extras = getIntent().getExtras();
        
        String selectedBrand = extras.getString("brand");

        setList();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vpic.nhtsa.dot.gov/api/vehicles/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        CarSpecClient carSpecClient = retrofit.create(CarSpecClient.class);
        Call<VehicleModelResponse> carResponseCall = carSpecClient.getModels(selectedBrand,"json");


        carResponseCall.enqueue(new Callback<VehicleModelResponse>() {
            @Override
            public void onResponse(Call<VehicleModelResponse> call, Response<VehicleModelResponse> response) {
                VehicleModelResponse vehicleResponseSingle = response.body();
                List<VehicleModel> vehicleModels = vehicleResponseSingle.getResults();


                List<String> modelList = new ArrayList<>();

                for (VehicleModel model: vehicleModels){
                    modelList.add(model.getModelName().trim());
                }

                Collections.sort(modelList);
                ArrayAdapter<String> listDataAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.selection_list_row, R.id.listText,modelList);
                setListAdapter(listDataAdapter);

                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<VehicleModelResponse> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),"An Error Occurred", Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }
    private void setList(){
        List<String> carModelList = getCarModels();

        ArrayAdapter<String> listDataAdapter = new ArrayAdapter<String>(this,R.layout.selection_list_row, R.id.listText,carModelList);
        setListAdapter(listDataAdapter);
    }
    /**
     * Generates a list of Car Models from REST-API 'https://vpic.nhtsa.dot.gov/api/'
     *
     * @return
     */
    private List<String> getCarModels(){

        List<String> carModelList = new ArrayList<>();

        for(int i =0 ; i < 30; i++){
            carModelList.add("Model " + i);
        }
        return carModelList;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String selectedItem = (String) getListView().getItemAtPosition(position);
        Intent intent = new Intent(this, MainActivity.class);

        intent.putExtra("selection", selectedItem);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }
}
