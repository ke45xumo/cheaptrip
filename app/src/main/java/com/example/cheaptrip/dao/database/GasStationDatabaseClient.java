package com.example.cheaptrip.dao.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.cheaptrip.models.tankerkoenig.Station;

import java.util.List;

@Dao
public interface GasStationDatabaseClient {
    /*=========================================================================================
     * Other Queries
     *=========================================================================================*/
    @Insert
    void insertAll(List<Station> stations);

    @Update
    void updateAll (List<Station> stations);

    @Delete
    void delete(Station station);

    @Query("DELETE FROM STATIONS")
    void deleteAll();
}
