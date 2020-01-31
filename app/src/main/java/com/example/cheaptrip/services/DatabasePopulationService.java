package com.example.cheaptrip.services;

import android.content.Context;
import android.os.AsyncTask;

import com.example.cheaptrip.dao.database.GasStationDatabaseClient;
import com.example.cheaptrip.database.GasStationDatabase;
import com.example.cheaptrip.database.VehicleDatabase;
import com.example.cheaptrip.handlers.StartupListener;
import com.example.cheaptrip.handlers.rest.RestListener;
import com.example.cheaptrip.handlers.rest.station.GasStationHistoryPriceHandler;
import com.example.cheaptrip.handlers.rest.vehicle.VehicleDataSetHandler;
import com.example.cheaptrip.models.fueleconomy.VehicleDataSet;
import com.example.cheaptrip.models.tankerkoenig.Station;

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
        VehicleDatabase vehicleDatabase = VehicleDatabase.getDatabase(mContext);

        List<VehicleDataSet> vehicleDataSetList = vehicleDataSetHandler.makeSyncRequest();
        List<VehicleDataSet> fromDataBase = vehicleDatabase.vehicleDatabaseClient().getAll();



        if(!fromDataBase.equals(vehicleDataSetList) && vehicleDataSetList != null && !vehicleDataSetList.isEmpty()){
            vehicleDatabase.vehicleDatabaseClient().deleteAll();
            vehicleDatabase.vehicleDatabaseClient().insertAll(vehicleDataSetList);
        }

        vehicleDatabase.close();



/*        GasStationHistoryHandler gasStationHistoryHandler = new GasStationHistoryHandler(2019,9,1);

       List<Station> stationList = gasStationHistoryHandler.makeSyncRequest();

       GasStationDatabaseClient dbClient = GasStationDatabase.getDatabase(mContext).gasStationDatabaseClient();
       dbClient.insertAll(stationList);*/

        GasStationHistoryPriceHandler gasStationHistoryPriceHandler = new GasStationHistoryPriceHandler(2019,10,1);
        /*gasStationHistoryPriceHandler.makeAsyncRequest(new RestListener<List<Station>>() {
            @Override
            public void OnRestSuccess(List<Station> stations) {

                    GasStationDatabaseClient dbClient = GasStationDatabase.getDatabase(mContext).gasStationDatabaseClient();
                    //dbClient.insertAll(stations);

            }

            @Override
            public void OnRestFail() {

            }
        });*/

/*        List<Station> stationList = gasStationHistoryPriceHandler.makeSyncRequest();
        GasStationDatabaseClient dbClient = GasStationDatabase.getDatabase(mContext).gasStationDatabaseClient();*/
        //dbClient.insertAll(stations);


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        mStartupListener.onVehiclesLoaded();
    }

}
