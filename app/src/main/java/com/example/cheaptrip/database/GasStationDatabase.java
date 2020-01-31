package com.example.cheaptrip.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.cheaptrip.dao.database.GasStationDatabaseClient;
import com.example.cheaptrip.models.tankerkoenig.Station;

/**
 * Database holding all the informations of vehicles
 * This is done by using Library Room.
 *
 * (see https://developer.android.com/topic/libraries/architecture/room)
 */
@Database(entities = {Station.class}, version = 1,exportSchema = false)
public abstract class GasStationDatabase extends RoomDatabase {

    public abstract GasStationDatabaseClient gasStationDatabaseClient();
    private static GasStationDatabase INSTANCE;

    public static GasStationDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (GasStationDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE =
                            Room.databaseBuilder(context.getApplicationContext(),GasStationDatabase.class, "STATIONS")
                                    .allowMainThreadQueries()
                                    .build();
                }
            }
        }
        return INSTANCE;
    }
}