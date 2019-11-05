package com.example.cheaptrip;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Picture;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    Button btn_carBrand;
    Button btn_carModel;
    Button btn_carYear;

    EditText edit_start;
    EditText edit_end;

    String str_Brand;
    String str_Model;
    String str_Year;

    ImageView pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        btn_carBrand = findViewById(R.id.btn_car_brand);
        btn_carModel = findViewById(R.id.btn_car_model);
        btn_carYear = findViewById(R.id.btn_car_year);

        pic = findViewById(R.id.img_gas_pump);

        edit_start = findViewById(R.id.edit_start);
        edit_end = findViewById(R.id.edit_destination);
    }

    public void selectFromList(View view){
        Intent intent;
        int requestCode;

        switch(view.getId()){
            case R.id.btn_car_brand:    intent = new Intent(this, CarBrandActivity.class);
                                        requestCode = 1;
                                        break;

            case R.id.btn_car_model:    intent = new Intent (this, CarModelActivity.class);
                                        requestCode = 2;
                                        break;

            case R.id.btn_car_year:     intent = new Intent(this, CarYearActivity.class);
                                        requestCode = 3;
                                        break;

            case R.id.btn_find:         intent = new Intent(this, CalculationActivity.class);
                                        String txt_start  = edit_start.getText().toString();
                                        String txt_end  = edit_end.getText().toString();

                                        intent.putExtra("start", txt_start);
                                        intent.putExtra("end",txt_end);

                                        requestCode = 4;
                                        break;

            default:                    return;


        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // Apply activity transition
            /*
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation(this, btn_carBrand, "robot");
            startActivityForResult(intent, requestCode,  options.toBundle());
             */
            startActivityForResult(intent, requestCode);
        } else {
            // Swap without transition
            startActivityForResult(intent, requestCode);
        }

        //finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK){
            return;
        }

        String str_listSelection = data.getStringExtra("selection");


        switch (requestCode){
            case 1:     str_Brand = str_listSelection;
                        btn_carBrand.setText(str_Brand);
                        break;


            case 2:     str_Model = str_listSelection;
                        btn_carModel.setText(str_Model);
                        break;

            case 3:     str_Year = str_listSelection;
                        btn_carYear.setText(str_Year);
                        break;

            default:    break;
        }

    }

    /**
     * Starts the Calculation Activity
     *
     * @param view
     */
    protected void startCalculation(View view){
        if (str_Year == null){
            Toast.makeText(this,"Construction Year not set!", Toast.LENGTH_LONG).show();
            return;
        }
        if (str_Brand == null){
            Toast.makeText(this,"Car Brand not set!", Toast.LENGTH_LONG).show();
            return;
        }
        if (str_Model == null){
            Toast.makeText(this,"Car Model not set!", Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = new Intent(this, CalculationActivity.class);
        intent.putExtra("brand",str_Brand);
        intent.putExtra("model",str_Brand);
        intent.putExtra("year",str_Brand);

        startActivity(intent);
    }

}
