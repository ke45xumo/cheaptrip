package com.example.cheaptrip.views.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cheaptrip.R;
import com.example.cheaptrip.handlers.view.adapters.RouteSegmentListAdapter;
import com.example.cheaptrip.models.TripRoute;

import java.util.HashMap;
import java.util.List;

public class CalcRouteFragment extends Fragment {
    private TextView mTtextView;

    ExpandableListView expandableListView;
    RouteSegmentListAdapter routeSegmentListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    TripRoute mTripRoute;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_calc_route, container, false);

        //mTtextView = rootView.findViewById(R.id.text);
        expandableListView = (ExpandableListView) rootView.findViewById(R.id.ex_list_view_segment);

        if(mTripRoute != null) {
            routeSegmentListAdapter = new RouteSegmentListAdapter(getContext(), mTripRoute);
            expandableListView.setAdapter(routeSegmentListAdapter);
            routeSegmentListAdapter.notifyDataSetChanged();
        }
        //mTtextView.setText("CalcRoute");

        return rootView;
    }


    public void updateList(TripRoute tripRoute){

        if(tripRoute == null) {
            Log.e("CHEAPTRIP","Cannot update route segement list: tripRoute is null");
            return;
        }

        mTripRoute = tripRoute;
        if(routeSegmentListAdapter != null) {

            routeSegmentListAdapter.updateList(tripRoute);
            routeSegmentListAdapter.notifyDataSetChanged();
        }
    }



}

