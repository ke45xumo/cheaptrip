package com.example.cheaptrip.handlers;

import com.example.cheaptrip.models.TripRoute;

import java.util.List;



public interface CalculationListener {
    void onCalculationDone(List<TripRoute> tripRouteList);
}
