package com.example.cheaptrip.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.cheaptrip.CarBrand;
import com.example.cheaptrip.R;

import java.util.List;

public class CarBrandAdapter extends ArrayAdapter<CarBrand> {
    Context context;
    List<CarBrand> carBrandList;

    public CarBrandAdapter(@NonNull Context context, List<CarBrand> carBrandList) {
        super(context, R.layout.selection_list_row);
        this.carBrandList = carBrandList;
        this.context = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (row == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.selection_list_row, parent, false);
        }
        TextView textView = (TextView) row.findViewById(R.id.listText);
        CarBrand list = carBrandList.get(position);
        String brand= list.getMakeName();
        textView.setText(brand);
        return row;
    }
}
