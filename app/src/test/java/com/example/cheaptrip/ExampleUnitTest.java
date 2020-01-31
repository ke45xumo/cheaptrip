package com.example.cheaptrip;

import com.example.cheaptrip.services.RouteService;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test_calculateCostsForRoute(){
        double result = RouteService.calculateCostsForRoute(20,30,1.0345);
        double expected = 6.207;

        assertEquals(result,expected,0.001);
    }
}

   /* public static double calculateCostsForRoute( double distance , double pricePerLiter, double  avgConsumption){
        return distance * pricePerLiter * avgConsumption/100;
    }
}*/