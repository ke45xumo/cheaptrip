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
import com.example.cheaptrip.models.retfrofit.VehicleBrand;
import com.example.cheaptrip.models.retfrofit.VehicleBrandResponse;

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
/**
 *
 */
public class CarBrandActivity extends ListActivity {

    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_brand);

        progressBar = findViewById(R.id.progress_brand);


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vpic.nhtsa.dot.gov/api/vehicles/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        CarSpecClient carSpecClient = retrofit.create(CarSpecClient.class);
        Call<VehicleBrandResponse> carResponseCall = carSpecClient.getBrands("car","json");


        carResponseCall.enqueue(new Callback<VehicleBrandResponse>() {
            @Override
            public void onResponse(Call<VehicleBrandResponse> call, Response<VehicleBrandResponse> response) {
                VehicleBrandResponse vehicleResponseSingle = response.body();
                List<VehicleBrand> vehicleBrands = vehicleResponseSingle.getResults();


                List<String> brandList = new ArrayList<>();

                for (VehicleBrand brand: vehicleBrands){
                    brandList.add(brand.getMakeName().trim());
                }

                Collections.sort(brandList);
                ArrayAdapter<String> listDataAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.selection_list_row, R.id.listText,brandList);
                setListAdapter(listDataAdapter);

                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<VehicleBrandResponse> call, Throwable t) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),"An Error Occurred", Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
        setList();
    }
    private void setList(){
        List<String> carBrandList = getCarBrands();

        ArrayAdapter<String> listDataAdapter = new ArrayAdapter<String>(this,R.layout.selection_list_row, R.id.listText,carBrandList);
        setListAdapter(listDataAdapter);
    }
    /**
     * Generates a list of Car Brands from REST-API 'https://vpic.nhtsa.dot.gov/api/'
     *
     * @return
     */
    private List<String> getCarBrands(){

        List<String> carBrandList = new ArrayList<>();
        for(int i =0 ; i < 30; i++){
            carBrandList.add("Brand " + i);
        }

        return carBrandList;
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

    private void startSpinner(){

    }


}
