package com.example.cheaptrip.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.cheaptrip.models.fueleconomy.VehicleDataSet;

import java.util.List;

@Dao
public interface VehicleDatabaseClient {
    @Query("SELECT * FROM VEHICLES")
    List<VehicleDataSet> getAll();

    @Query("SELECT DISTINCT (BRAND) FROM VEHICLES")
    List<String> getAllBrands();

    @Query("SELECT DISTINCT (YEAR) FROM VEHICLES")
    List<String> getAllYears();

    @Query("SELECT * FROM VEHICLES WHERE YEAR = :year")
    List<VehicleDataSet> getVechiclesForYear(String year);

    @Query("SELECT DISTINCT(BRAND) FROM VEHICLES WHERE YEAR = :year")
    List<String> getBrandsForYear(String year);

    @Query("SELECT DISTINCT (YEAR) FROM VEHICLES WHERE BRAND = :brand")
    List<String> getYearsForBrand(String brand);

    @Query("SELECT DISTINCT (MODEL) FROM VEHICLES WHERE BRAND = :brand")
    List<String> getModelForBrand(String brand);

    @Query("SELECT DISTINCT (YEAR) FROM VEHICLES WHERE BRAND = :brand AND MODEL = :model")
    List<String> getYearForModel(String brand, String model);

    @Query("SELECT * FROM VEHICLES WHERE BRAND = :brand AND MODEL = :model and YEAR = :year LIMIT 1")
    VehicleDataSet findVehicle(String brand, String model,String year);

    @Insert
    void insertAll(List<VehicleDataSet> vehicleDataSets);

    @Delete
    void delete(VehicleDataSet user);
}
