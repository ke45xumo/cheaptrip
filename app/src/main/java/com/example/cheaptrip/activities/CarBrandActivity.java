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

import androidx.room.Room;

import com.example.cheaptrip.R;
import com.example.cheaptrip.database.VehicleDatabase;
import com.example.cheaptrip.handlers.RestListener;
import com.example.cheaptrip.handlers.rest.vehicle.VehicleBrandRestHandler;
import java.util.ArrayList;
import java.util.List;


//https://www.codeproject.com/Articles/5165136/Retrofit-A-REST-Client-for-Android-Retrofit-2-X
//TODO: https://stackoverflow.com/questions/29380844/how-to-set-timeout-in-retrofit-library
//TODO: Search for Vehicle in Edit Text
/**
 * TODO: Document
 */
public class CarBrandActivity extends ListActivity {

    ProgressBar progressBar;

    /**
     * Gets Called when CarBrandActivity will be created.
     * Starts Asynchronious Call of the Webservice-Rest-API
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_brand);

        progressBar = findViewById(R.id.progress_brand);
        setBrandListView();
        //setList();
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

        String selectedItem = (String) getListView().getItemAtPosition(position);
        Intent intent = new Intent(this, MainActivity.class);

        intent.putExtra("selection", selectedItem);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }


    public void setBrandListView(){
        VehicleBrandRestHandler vehicleBrandRestHandler = new VehicleBrandRestHandler();

        vehicleBrandRestHandler.startLoadProperties(new RestListener<List<String>>() {
            @Override
            public void OnRestSuccess(List<String> brandList) {
                ArrayAdapter<String> listDataAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.selection_list_row, R.id.listText,brandList);
                setListAdapter(listDataAdapter);
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void OnRestFail() {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),"An Error Occurred", Toast.LENGTH_LONG).show();
            }
        });
    }
}
