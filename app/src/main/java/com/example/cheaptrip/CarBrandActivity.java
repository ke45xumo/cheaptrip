package com.example.cheaptrip;

import android.app.Activity;
import android.app.AppComponentFactory;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.example.cheaptrip.adapters.CarBrandAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.provider.AlarmClock.EXTRA_MESSAGE;
//https://www.codeproject.com/Articles/5165136/Retrofit-A-REST-Client-for-Android-Retrofit-2-X
public class CarBrandActivity extends AppCompatActivity {
    Button btn_carBrand;

    ListView brandListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_brand);
        btn_carBrand = findViewById (R.id.btn_car_brand);

        brandListView = findViewById(R.id.list_car_brand);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vpic.nhtsa.dot.gov/api/vehicles/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        CarSpecClient carSpecClient = retrofit.create(CarSpecClient.class);
        Call<CarResponse> carResponseCall = carSpecClient.getResponse("json");


        carResponseCall.enqueue(new Callback<CarResponse>() {
            @Override
            public void onResponse(Call<CarResponse> call, Response<CarResponse> response) {
                CarResponse carResponseSingle = response.body();
                List<CarBrand> carBrands = carResponseSingle.getResults();
                ArrayAdapter adapter = new CarBrandAdapter(getApplicationContext(),carBrands);

                brandListView.setAdapter(adapter);
                //setListAdapter(new CarBrandAdapter(CarBrandActivity.this,carBrands));
                /*
                brandListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String selectedItem = (String) getListView().getItemAtPosition(position);
                        Intent intent = new Intent(this,MainActivity.class);

                        intent.putExtra("selection", selectedItem);
                        setResult(Activity.RESULT_OK,intent);
                        finish();
                    }
                });

                 */
            }

            @Override
            public void onFailure(Call<CarResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
        setList();
    }
    private void setList(){
        List<String> carBrandList = getCarBrands();

        ArrayAdapter<String> listDataAdapter = new ArrayAdapter<String>(this,R.layout.selection_list_row, R.id.listText,carBrandList);
        brandListView.setAdapter(listDataAdapter);
    }
    /**
     * Generates a list of Car Brands from REST-API 'https://vpic.nhtsa.dot.gov/api/'
     *
     * @return
     */
    private List<String> getCarBrands(){
        /*
        URI uri = UriBuilder.fromUri("https://vpic.nhtsa.dot.gov/api/vehicles").port(80).build();
        WebTarget client = ClientBuilder.newClient().target(uri).path("/GetAllMakes");
        Response response = client.request().get();

        String[] s = response.readEntity(String[].class);

        //System.out.println(s);
        Log.d(s);

         */

        List<String> carBrandList = new ArrayList<>();
        for(int i =0 ; i < 30; i++){
            carBrandList.add("Brand " + i);
        }



        /*
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://vpic.nhtsa.dot.gov/api/vehicles/")
        .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        Response<CarResponse> response;
        CarSpecClient carSpecClient = retrofit.create(CarSpecClient.class);
        //Single<CarResponse> carResponseSingle = carSpecClient.getResponse("json");

        Call<CarResponse> carResponseCall = carSpecClient.getResponse("json");

        final List<String> carBrandList = new ArrayList<>();

        // Asynchronious
        /*
        carResponseCall.enqueue(new Callback<CarResponse>() {
            @Override
            public void onResponse(Call<CarResponse> call, Response<CarResponse> response) {
                CarResponse carResponseSingle = response.body();
                List<CarBrand> carBrands = carResponseSingle.getResults();

                for (CarBrand brand: carBrands){
                    carBrandList.add(brand.getMakeName());
                }

            }

            @Override
            public void onFailure(Call<CarResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });


        try {
            response = carResponseCall.execute();
            CarResponse carResponse = response.body();
        }catch(Exception ex){
            //Log.e("ERROR_CHEAPTRIP",ex.);
            ex.printStackTrace();
            /*
            for(StackTraceElement el :ex.getStackTrace()){
                Log.e("ERROR_CHEAPTRIP",el.toString());

            }


            //ex.getMessage();
            //TODO: Error Handling
        }


        /*
        CarResponse resp = carResponseSingle.blockingGet();
        List<CarBrand> carBrands = resp.getResults();

        for (CarBrand brand : carBrands){
            carBrandList.add(brand.getMakeName());
        }

        /*
        for(int i =0 ; i < 30; i++){
            carBrandList.add("Brand " + i);
        }
        */



        return carBrandList;
    }
    /*
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String selectedItem = (String) getListView().getItemAtPosition(position);
        Intent intent = new Intent(this,MainActivity.class);

        intent.putExtra("selection", selectedItem);
        setResult(Activity.RESULT_OK,intent);
        finish();
    }

     */
}
