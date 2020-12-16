package com.example.customertablet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.vo.CarVO;

public class CarInfoActivity extends AppCompatActivity {

    CarVO car;

    ImageButton imageButton_back;
    TextView textView_year,textView_model,textView_type,textView_carNum,textView_oilType;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_info);
        getSupportActionBar().hide();

        imageButton_back = findViewById(R.id.imageButton_back);
        textView_year = findViewById(R.id.textView_year);
        textView_model = findViewById(R.id.textView_model);
        textView_type = findViewById(R.id.textView_type);
        textView_carNum = findViewById(R.id.textView_carNum);
        textView_oilType = findViewById(R.id.textView_oilType);

        sp = getSharedPreferences("car",MODE_PRIVATE);
        String caryear = sp.getString("caryear","");
        String carmodel = sp.getString("carmodel","");
        String cartype = sp.getString("cartype","");
        if (cartype.equals("p")) {
            cartype = "승용차";
        } else if (cartype.equals("v")) {
            cartype = "승합차";
        } else if (cartype.equals("t")) {
            cartype = "트럭";
        }
        String carnum = sp.getString("carnum","");
        String caroiltype = sp.getString("caroiltype","");

        textView_year.setText(caryear);
        textView_model.setText(carmodel);
        textView_type.setText(cartype);
        textView_carNum.setText(carnum);
        textView_oilType.setText(caroiltype);


        imageButton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }// end onCreate




}