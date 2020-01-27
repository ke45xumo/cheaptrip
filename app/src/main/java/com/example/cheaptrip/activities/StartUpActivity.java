package com.example.cheaptrip.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cheaptrip.R;
import com.example.cheaptrip.database.VehicleDatabase;
import com.example.cheaptrip.handlers.StartupListener;
import com.example.cheaptrip.handlers.rest.RestListener;
import com.example.cheaptrip.handlers.rest.vehicle.VehicleDataSetHandler;
import com.example.cheaptrip.models.fueleconomy.VehicleDataSet;
import com.example.cheaptrip.services.DatabasePopulationService;

import java.util.List;

public class StartUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        DatabasePopulationService databasePopulationService = new DatabasePopulationService(this, new StartupListener() {
            @Override
            public void onVehiclesLoaded() {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
        databasePopulationService.execute();


/*
        VehicleDataSetHandler vehicleDataSetHandler  = new VehicleDataSetHandler();

        vehicleDataSetHandler.makeAsyncRequest(new RestListener<List<VehicleDataSet>>() {
            @Override
            public void OnRestSuccess(List<VehicleDataSet> vehicleDataSets) {
                VehicleDatabase vehicleDatabase = VehicleDatabase.getDatabase(getApplicationContext());
                vehicleDatabase.vehicleDatabaseClient().insertAll(vehicleDataSets);

                vehicleDatabase.close();

                Intent intent = new Intent(getApplicationContext(),MainActivity.class);

                startActivity(intent);
            }

            @Override
            public void OnRestFail() {

            }
        });*/


    }
}
