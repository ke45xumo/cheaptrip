package com.example.cheaptrip.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.cheaptrip.models.retfrofit.nhtsa.VehicleBrand;
import com.example.cheaptrip.R;

import java.util.List;

public class CarBrandAdapter extends BaseAdapter {
    Context context;
    List<VehicleBrand> vehicleBrandList;

    public CarBrandAdapter(@NonNull Context context, List<VehicleBrand> vehicleBrandList) {
        //super(context, R.layout.selection_list_row);
        this.vehicleBrandList = vehicleBrandList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return vehicleBrandList.size();
    }

    @Override
    public Object getItem(int position) {
        return vehicleBrandList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return vehicleBrandList.get(position).getMakeId();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.selection_list_row, parent, false);
        }
        TextView textView = (TextView) row.findViewById(R.id.listText);
        VehicleBrand list = vehicleBrandList.get(position);
        String brand= list.getMakeName();
        textView.setText(brand);
        return row;
    }

}
