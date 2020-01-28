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
import com.example.cheaptrip.app.CheapTripApp;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.stream.JsonReader;

import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
//https://www.codeproject.com/Articles/5165136/Retrofit-A-REST-Client-for-Android-Retrofit-2-X
//TODO: https://stackoverflow.com/questions/29380844/how-to-set-timeout-in-retrofit-library
//TODO: Search for Vehicle in Edit Text

public class VehicleYearActivity extends ListActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((CheapTripApp)getApplication()).setCurrentActivity(this);
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

        /*LinkedTreeMap data = getFuelEconomyData();
        Set<String> years = getYears(data);
        List yearsList = new ArrayList(years);
        Collections.sort(yearsList, Collections.reverseOrder());
        Set yearsSorted = new LinkedHashSet(yearsList);
        //System.out.println(yearsSorted);

        List<String> mainList = new ArrayList<String>();
        mainList.addAll(yearsSorted);
        return mainList;*/
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

    private static Set<String> getYears(LinkedTreeMap data){
        return data.keySet();
    }

    private static LinkedTreeMap getFuelEconomyData(){
        Object json = null;
        try {
            json = parse("http://data.cheaptrip.cf/vehicles.json");
        } catch (Exception e) {
            e.printStackTrace();
        }

        LinkedTreeMap root = (LinkedTreeMap) json;
        LinkedTreeMap data = (LinkedTreeMap) root.get("data");

        return data;
    }

    // https://javadeveloperzone.com/java-8/java-parse-large-json-file-gson-example/
    private static Object parse(String urlString) {
        Object o = null;
        try {
            URL url = new URL(urlString);
            JsonReader jsonReader = new JsonReader(new InputStreamReader(url.openStream()));
            Gson gson = new GsonBuilder().create();
            jsonReader.beginArray();
            int numberOfRecords = 0;
            while (jsonReader.hasNext()) {
                o = gson.fromJson(jsonReader, Object.class);
                numberOfRecords++;
            }
            jsonReader.endArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return o;
    }

}
