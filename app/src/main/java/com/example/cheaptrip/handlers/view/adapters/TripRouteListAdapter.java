package com.example.cheaptrip.handlers.view.adapters;


import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.cheaptrip.R;
import com.example.cheaptrip.models.TripRoute;
import com.example.cheaptrip.models.nhtsa.VehicleBrand;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class TripRouteListAdapter extends BaseAdapter {
    private List<TripRoute> tripRouteList;
    private Context context;

    public TripRouteListAdapter(@NonNull Context context){
        this.context = context;
        tripRouteList = new ArrayList<>();
    }

    public TripRouteListAdapter( Context context, List<TripRoute> tripRouteList) {
        this.tripRouteList = sortForCosts(tripRouteList);
        this.context = context;
    }

    @Override
    public int getCount() {
        if(tripRouteList != null) {
            return tripRouteList.size();
        }
        return 0;
    }

    @Override
    public TripRoute getItem(int position) {
        if(tripRouteList != null){
            return tripRouteList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        if(tripRouteList != null) {
            return System.identityHashCode(tripRouteList.get(position));
        }
        return -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.selection_list_row, parent, false);
        }

        TextView textView = (TextView) row.findViewById(R.id.listText);
        TripRoute route = tripRouteList.get(position);

        double costs = route.getCosts();
        double duration = route.getDuration();
        double distance = route.getDistance();

        int hours = (int)duration/3600;
        int mins =  (int)(duration%3600)/60;
        int secs =  (int)(duration%3600)%60;

        String strTime = String.format("%d h %d m %d s",hours, mins, secs);

        String text = String.format(Locale.GERMANY,"Costs %23.2f €\nDuration:%22s\nDistance:%20.2f km",costs, strTime, distance);

        textView.setText(text);

        return row;
    }

    public List<TripRoute> getTripRouteList() {
        return tripRouteList;
    }

    public void setTripRouteList(List<TripRoute> tripRouteList) {
        this.tripRouteList = sortForCosts(tripRouteList);
    }

    public List<TripRoute> sortForCosts(List<TripRoute> routeList){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            routeList.sort(new Comparator<TripRoute>() {
                @Override
                public int compare(TripRoute route1, TripRoute route2) {
                    return Double.compare(route1.getCosts(),route2.getCosts());
                }
            });
        }

        return routeList;
    }

    public List<TripRoute> sortForDistance(List<TripRoute> routeList){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            routeList.sort(new Comparator<TripRoute>() {
                @Override
                public int compare(TripRoute route1, TripRoute route2) {
                    return Double.compare(route1.getDistance(),route2.getDistance());
                }
            });
        }

        return routeList;
    }

    public List<TripRoute> sortForDuration(List<TripRoute> routeList){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            routeList.sort(new Comparator<TripRoute>() {
                @Override
                public int compare(TripRoute route1, TripRoute route2) {
                    return Double.compare(route1.getDuration(),route2.getDuration());
                }
            });
        }

        return routeList;
    }
}
