package com.example.cheaptrip.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cheaptrip.R;


public class CalculationActivity extends AppCompatActivity {

    TextView txt_start;
    TextView txt_end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculation);

        txt_start = findViewById(R.id.txt_start);
        txt_end = findViewById(R.id.txt_end);

        Bundle extras = getIntent().getExtras();

        String start = (String) extras.get("start");
        String end = (String) extras.get("end");

        txt_start.setText(start);
        txt_end.setText(end);

    }
}
