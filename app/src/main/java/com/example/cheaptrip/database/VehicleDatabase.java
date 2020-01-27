package com.example.cheaptrip.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.cheaptrip.dao.VehicleDataClient;
import com.example.cheaptrip.dao.VehicleDatabaseClient;
import com.example.cheaptrip.models.fueleconomy.VehicleDataSet;
import com.example.cheaptrip.models.nhtsa.VehicleBrand;
import com.example.cheaptrip.dao.DAOVehicleBrand;

@Database(entities = {VehicleDataSet.class}, version = 1,exportSchema = false)
public abstract class VehicleDatabase extends RoomDatabase {

    public abstract VehicleDatabaseClient vehicleDatabaseClient();
    private static VehicleDatabase INSTANCE;

    public static VehicleDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (VehicleDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE =
                            Room.databaseBuilder(context.getApplicationContext(),VehicleDatabase.class, "VEHICLES")
                                    .allowMainThreadQueries()
                                    .build();
                }
            }
        }
        return INSTANCE;
    }
}