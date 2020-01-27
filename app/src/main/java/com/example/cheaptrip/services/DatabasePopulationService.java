package com.example.cheaptrip.services;

import android.content.Context;
import android.os.AsyncTask;

import com.example.cheaptrip.database.VehicleDatabase;
import com.example.cheaptrip.handlers.StartupListener;
import com.example.cheaptrip.handlers.rest.vehicle.VehicleDataSetHandler;
import com.example.cheaptrip.models.fueleconomy.VehicleDataSet;

import java.util.List;

public class DatabasePopulationService extends AsyncTask<Void,Void,Void>  {


    private Context mContext;
    private StartupListener mStartupListener;

    public DatabasePopulationService(Context context, StartupListener startupListener) {
        mContext = context;
        mStartupListener = startupListener;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        VehicleDataSetHandler vehicleDataSetHandler  = new VehicleDataSetHandler();

        List<VehicleDataSet> vehicleDataSetList = vehicleDataSetHandler.makeSyncRequest();

        VehicleDatabase vehicleDatabase = VehicleDatabase.getDatabase(mContext);
        vehicleDatabase.vehicleDatabaseClient().insertAll(vehicleDataSetList);

        vehicleDatabase.close();

        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        mStartupListener.onVehiclesLoaded();
    }
}
