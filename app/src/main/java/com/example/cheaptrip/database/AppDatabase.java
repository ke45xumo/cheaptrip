package com.example.cheaptrip.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;


import com.example.cheaptrip.dao.DAOVehicleBrand;
import com.example.cheaptrip.models.retfrofit.nhtsa.VehicleBrand;

@Database(entities = {VehicleBrand.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DAOVehicleBrand DAOVehicleBrand();
}