package com.example.cheaptrip.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.cheaptrip.database.VehicleDatabase;
import com.example.cheaptrip.models.fueleconomy.VehicleDataSet;

import java.util.List;

@Dao
public interface VehicleDatabaseClient {
    @Query("SELECT * FROM VEHICLES")
    List<VehicleDataSet> getAll();

    @Query("SELECT DISTINCT (BRAND) FROM VEHICLES WHERE NOT(PREMIUM is null AND REGULAR is null AND DIESEL is null)")
    List<String> getAllBrands();

    @Query("SELECT DISTINCT (YEAR) FROM VEHICLES WHERE NOT(PREMIUM is null AND REGULAR is null AND DIESEL is null)")
    List<String> getAllYears();

    @Query("SELECT * FROM VEHICLES WHERE YEAR = :year AND NOT(PREMIUM is null AND REGULAR is null AND DIESEL is null)")
    List<VehicleDataSet> getVechiclesForYear(String year);

    @Query("SELECT DISTINCT(BRAND) FROM VEHICLES WHERE YEAR = :year AND NOT(PREMIUM is null AND REGULAR is null AND DIESEL is null)")
    List<String> getBrandsForYear(String year);

    @Query("SELECT DISTINCT (YEAR) FROM VEHICLES WHERE BRAND = :brand AND NOT(PREMIUM is null AND REGULAR is null AND DIESEL is null)")
    List<String> getYearsForBrand(String brand);

    @Query("SELECT DISTINCT (MODEL) FROM VEHICLES WHERE BRAND = :brand AND NOT(PREMIUM is null AND REGULAR is null AND DIESEL is null)")
    List<String> getModelForBrand(String brand);

    @Query("SELECT DISTINCT (YEAR) FROM VEHICLES WHERE BRAND = :brand AND MODEL = :model AND NOT(PREMIUM is null AND REGULAR is null AND DIESEL is null)")
    List<String> getYearForModel(String brand, String model);

    @Query("SELECT * FROM VEHICLES WHERE BRAND = :brand AND MODEL = :model and YEAR = :year AND NOT(PREMIUM is null AND REGULAR is null AND DIESEL is null)  LIMIT 1")
    VehicleDataSet findVehicle(String brand, String model,String year);

    @Insert
    void insertAll(List<VehicleDataSet> vehicleDataSets);

    @Update
    void updateAll (List<VehicleDataSet> vehicleDatabases);

    @Delete
    void delete(VehicleDataSet user);

    @Query("DELETE FROM VEHICLES")
    void deleteAll();
}
