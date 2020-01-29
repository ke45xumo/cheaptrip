package com.example.cheaptrip.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cheaptrip.R;
import com.example.cheaptrip.models.TripGasStation;

public class CalcGasStationFragment extends Fragment {
    private TripGasStation mTripGasStation;

    TextView mTextView;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_calc_gas, container, false);


        mTextView = rootView.findViewById(R.id.tv_gasstation_info);


        if(mTripGasStation != null){
            String textToView = generateText(mTripGasStation);
            mTextView.setText(textToView);
        }else{
            mTextView.setText("Chose a Route to display a Gas Station");
        }

        mTextView.invalidate();
        rootView.invalidate();

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();

        if(mTextView != null && mTripGasStation != null){
            String textToView = generateText(mTripGasStation);
            mTextView.setText(textToView);
            mTextView.invalidate();
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTextView = view.findViewById(R.id.tv_gasstation_info);


        if(mTripGasStation != null){
            String textToView = generateText(mTripGasStation);
            mTextView.setText(textToView);
        }else{
            mTextView.setText("Chose a Route to display a Gas Station");
        }

        mTextView.invalidate();
        view.invalidate();
    }

    public void setGasStationInfo(TripGasStation tripGasStation){
        mTripGasStation = tripGasStation;

        String textToView = generateText(tripGasStation);

        if(mTextView != null) {
            mTextView.setText(textToView);
            mTextView.invalidate();
        }
    }


    private String generateText(TripGasStation tripGasStation){
        String city = tripGasStation.getName();
        String street = tripGasStation.getStreet();
        String housNumber = tripGasStation.getHousenumber();
        String postCode = tripGasStation.getPostcode();

        String brand = tripGasStation.getBrand();
        String locationName = tripGasStation.getLocationName();

        double e5 = tripGasStation.getPriceE5();
        double e10 = tripGasStation.getPriceE10();
        double diesel = tripGasStation.getPriceDiesel();

        String result = String.format("%s\n\nPrices:\n\tE5: %f\n\tE10: %f\n\tDiesel %f\n\nLocationInfo:\n\t%s %s\n\t%s %s\n\t%s",brand,e5,e10,diesel,postCode,city,street,housNumber,locationName);

        return result;
    }
}
