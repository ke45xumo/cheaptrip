package com.example.cheaptrip.activities;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.cheaptrip.R;
import com.example.cheaptrip.handlers.CarBrandAdapter;
import com.example.cheaptrip.handlers.rest.RestListener;
import com.example.cheaptrip.handlers.rest.vehicle.VehicleBrandHandler;
import com.example.cheaptrip.handlers.rest.vehicle.VehicleBrandRestHandler;
import com.example.cheaptrip.models.nhtsa.VehicleBrand;

import java.util.ArrayList;
import java.util.List;


//https://www.codeproject.com/Articles/5165136/Retrofit-A-REST-Client-for-Android-Retrofit-2-X
//TODO: https://stackoverflow.com/questions/29380844/how-to-set-timeout-in-retrofit-library
//TODO: Search for Vehicle in Edit Text
/**
 * TODO: Document
 */
public class CarBrandActivity extends ListActivity {

    ProgressBar progressBar;    // Loading Icon until List is loaded (using REST-API)
    EditText edit_searchBrand;  // EditText Field for Search

    private static CarBrandAdapter listDataAdapter;
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
        edit_searchBrand = findViewById(R.id.edit_brand);

        setBrandListView2();
        //setList();
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

        VehicleBrand selectedItem = (VehicleBrand) getListView().getItemAtPosition(position);

        Intent intent = new Intent(this, MainActivity.class);
        //Bundle extras = new Bundle();

        intent.putExtra("selection", selectedItem.getMakeName());
        setResult(Activity.RESULT_OK,intent);
        finish();
    }


    public void setBrandListView2() {
        VehicleBrandHandler vehicleBrandHandler = new VehicleBrandHandler();

        vehicleBrandHandler.startLoadProperties(new RestListener<List<VehicleBrand>>() {
            @Override
            public void OnRestSuccess(List<VehicleBrand> brandList) {
                listDataAdapter = new CarBrandAdapter(getApplicationContext(), brandList);
                //ArrayAdapter<String> listDataAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.selection_list_row, R.id.listText,brandList);
                setListAdapter(listDataAdapter);
                progressBar.setVisibility(View.INVISIBLE);

                edit_searchBrand.addTextChangedListener(new TextWatcher() {
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
