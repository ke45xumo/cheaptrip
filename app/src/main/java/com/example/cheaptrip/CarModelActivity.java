package com.example.cheaptrip;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import android.widget.ListView;


import java.util.ArrayList;
import java.util.List;
//https://www.codeproject.com/Articles/5165136/Retrofit-A-REST-Client-for-Android-Retrofit-2-X
public class CarModelActivity extends ListActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_car_brand);

        setList();
    }
    private void setList(){
        List<String> carModelList = getCarModels();

        ArrayAdapter<String> listDataAdapter = new ArrayAdapter<String>(this,R.layout.selection_list_row, R.id.listText,carModelList);
        setListAdapter(listDataAdapter);
    }
    /**
     * Generates a list of Car Brands from REST-API 'https://vpic.nhtsa.dot.gov/api/'
     *
     * @return
     */
    private List<String> getCarModels(){
        /*
        URI uri = UriBuilder.fromUri("https://vpic.nhtsa.dot.gov/api/vehicles").port(80).build();
        WebTarget client = ClientBuilder.newClient().target(uri).path("/GetAllMakes");
        Response response = client.request().get();

        String[] s = response.readEntity(String[].class);

        Log.d(s);

         */
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
        Intent intent = new Intent(this,MainActivity.class);

        intent.putExtra("selection", selectedItem);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }
}
