package com.example.cheaptrip.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.cheaptrip.models.rest.nhtsa.VehicleBrand;
import com.example.cheaptrip.dao.DAOVehicleBrand;

@Database(entities = {VehicleBrand.class}, version = 1)
public abstract class VehicleDatabase extends RoomDatabase {

    public abstract DAOVehicleBrand DAOVehicleBrand();
    private static VehicleDatabase INSTANCE;

    static VehicleDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (VehicleDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE =
                            Room.databaseBuilder(context.getApplicationContext(),
                                    VehicleDatabase.class,
                                    "vehicle_database").build();
                }
            }
        }
        return INSTANCE;
    }
}