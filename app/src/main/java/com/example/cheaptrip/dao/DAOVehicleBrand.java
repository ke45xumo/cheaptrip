package com.example.cheaptrip.dao;



import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.cheaptrip.models.rest.nhtsa.VehicleBrand;

import java.util.List;


@Dao
public interface DAOVehicleBrand {

    @Query("SELECT * FROM VehicleBrand")
    List<VehicleBrand> getAll();

    @Query("SELECT * FROM VehicleBrand WHERE makeId IN (:userIds)")
    List<VehicleBrand> loadAllByIds(int[] userIds);

    @Query("SELECT * FROM VehicleBrand WHERE makeName LIKE :first AND " +
            "makeName LIKE :last LIMIT 1")
    VehicleBrand findByName(String first, String last);

    @Insert
    void insert(VehicleBrand brand);

    @Insert
    void insertAll(VehicleBrand... brands);

    @Delete
    void delete(VehicleBrand user);
}