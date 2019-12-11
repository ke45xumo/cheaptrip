package com.example.cheaptrip.dao;

import com.example.cheaptrip.models.tankerkoenig.GasStationResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


/**
 * TODO: Document
 */
public interface GasStationClient {

    /**
     * Enum to determine the Fuel Type for the REST-API Call
     */
    public enum FuelType
    {
        E5("e5"),
        E10("e10"),
        DIESEL("diesel"),
        ALL("all");

        private String fuelType;

        FuelType(String fuelType) {
            this.fuelType = fuelType;
        }

        public String getFuelType() {
            return fuelType;
        }
    }
    /**
     * Enum to determine how to sort the results of the REST-API Call
     */
    public enum Sort
    {
        PRICE("price"),
        DISTANCE("dist");

        private String sort;

        Sort(String sort) {
            this.sort = sort;
        }

        public String getSort() {
            return sort;
        }
    }


    final static String API_KEY = "ad5b5cac-db85-4516-832d-1bc90df23946";

    /**
     * TODO: Document
     * @param lat
     * @param lon
     * @param radius
     * @param type
     * @param sort
     * @return
     */
    @GET("json/list.php?apikey="+API_KEY)
    Call<GasStationResponse> GetAllForRadius(@Query("lat") Double lat,
                                             @Query("lng") Double lon,
                                             @Query("rad") Double radius,
                                             @Query("type") FuelType type,
                                             @Query("sort") Sort sort
                                             );

}
