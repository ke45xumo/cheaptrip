package com.example.cheaptrip.activities;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.example.cheaptrip.R;

import java.util.ArrayList;
import java.util.List;
//https://www.codeproject.com/Articles/5165136/Retrofit-A-REST-Client-for-Android-Retrofit-2-X
//TODO: https://stackoverflow.com/questions/29380844/how-to-set-timeout-in-retrofit-library
//TODO: Search for Vehicle in Edit Text

public class VehicleYearActivity extends ListActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_car_brand);

        setList();
    }
    private void setList(){
        List<String> carYearList = getCarYears();


        ArrayAdapter<String> listDataAdapter = new ArrayAdapter<String>(this, R.layout.selection_list_row, R.id.listText,carYearList){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the current item from ListView
                View view = super.getView(position,convertView,parent);
                if(position %2 == 1) {
                    // Set a background color for ListView regular row/item
                    view.setBackgroundColor(Color.parseColor("#CCCCCC"));
                }else{
                    view.setBackgroundColor(Color.WHITE);
                }
                return view;
            }
        };
        setListAdapter(listDataAdapter);
    }
    /**
     * Generates a list of Car Brands from REST-API 'https://vpic.nhtsa.dot.gov/api/'
     *
     * @return
     */
    private List<String> getCarYears(){
        List<String> carYearList = new ArrayList<>();

        for(int i =2019 ; i >1908; i--){
            carYearList.add(""+i);
        }
        return carYearList;
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
