package com.example.cheaptrip.activities;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.cheaptrip.R;
import com.example.cheaptrip.handlers.view.VehicleBrandAdapter;
import com.example.cheaptrip.handlers.rest.RestListener;
import com.example.cheaptrip.handlers.rest.vehicle.VehicleBrandHandler;
import com.example.cheaptrip.models.nhtsa.VehicleBrand;

import java.util.List;


//https://www.codeproject.com/Articles/5165136/Retrofit-A-REST-Client-for-Android-Retrofit-2-X
//TODO: https://stackoverflow.com/questions/29380844/how-to-set-timeout-in-retrofit-library
//TODO: Search for Vehicle in Edit Text
/**
 * This Classes purpose is to provide all current Vehicle Brands as List,
 * so that the user can choose the relevant brand for further calculation.
 *
 * This is done by calling the web interface (REST-Api)
 * of the National Highway and Security Administration and populating the list for further interaction.
 *
 * As the user chooses a vehicle brand it will return to the calling activity whilst delivering the
 * selection (brand name) to it (as extra).
 */
public class CarBrandActivity extends ListActivity {

    private ProgressBar progressBar;                // Loading Icon until List is loaded (using REST-API)
    private EditText edit_searchBrand;              // EditText Field for Search

    private VehicleBrandAdapter listDataAdapter;    // List Adapter for VehicleBrands

    /**
     * Gets Called when CarBrandActivity will be created.
     * Starts Asynchronious Call of the Webservice-Rest-API
     * @param savedInstanceState: State of this unique Instance
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize Views
        setContentView(R.layout.activity_car_brand);
        progressBar = findViewById(R.id.progress_brand);
        edit_searchBrand = findViewById(R.id.edit_brand);

        // Initialize ListAdapter
        listDataAdapter = new VehicleBrandAdapter(this);
        setListAdapter(listDataAdapter);

        setBrandListView();

        assertMembersInitialized();
    }

    /**
     * This is a Callback function, which gets triggered when an item gets clicked.
     * It will send the Item Title (=Brand) back to calling Activity ( MainActivity) and finishes.
     *
     * @param l:        the ListActivity's listView
     * @param v:        the listItem (as View)
     * @param position: the position of the clicked item in the List
     * @param id:       Identifier of the clicked Item
     */
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        assertMembersInitialized();
        super.onListItemClick(l, v, position, id);
        /*----------------------------------------------------------
         * Get the Brand from the selected  item of the  ListView
         *---------------------------------------------------------*/
        VehicleBrand selectedItem = (VehicleBrand) getListView().getItemAtPosition(position);
        String brand = selectedItem.getMakeName();
        /*----------------------------------------------------------
         * Send the Brand Name back to MainActivity
         *----------------------------------------------------------*/
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("selection", brand);
        setResult(Activity.RESULT_OK,intent);

        finish();
    }

    /**
     * Populates the ListView by Calling the Rest-Interface of NHTSA and storing the response's
     * data as List to  listDataAdapter (Type: VehicleBrandAdapter).
     *
     * The response is received asynchroniously and removes the Progressbar.
     */
    public void setBrandListView() {
        assertMembersInitialized();
        VehicleBrandHandler vehicleBrandHandler = new VehicleBrandHandler();

        vehicleBrandHandler.startGetVehicleBrandList(new RestListener<List<VehicleBrand>>() {
            @Override
            public void OnRestSuccess(List<VehicleBrand> brandList) {
                listDataAdapter.setList(brandList);
                progressBar.setVisibility(View.INVISIBLE);
                setTextChangedListener();
            }

            @Override
            public void OnRestFail() {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "An Error Occurred", Toast.LENGTH_LONG).show();
                Log.e("CHEAPTRIP", "Could not get Rest-Response.");
            }
        });
        assertMembersInitialized();
    }

    /**
     * Sets a Listener to the EditText-View.
     * When the text is changed a filter will be applied to ths List according to the entered Content
     */
    private void setTextChangedListener(){
        assertMembersInitialized();

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

        assertMembersInitialized();
    }

    /**
     * A Class Invariant.
     * Reassures that all the relevant Member Variables are initialized.
     */
    private void assertMembersInitialized(){
        assert (listDataAdapter != null);
        assert (edit_searchBrand != null);
        assert (progressBar != null);
    }
}
