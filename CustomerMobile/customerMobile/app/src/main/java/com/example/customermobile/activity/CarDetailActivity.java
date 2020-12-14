package com.example.customermobile.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.customermobile.R;
import com.example.customermobile.vo.CarVO;
import com.example.customermobile.vo.UsersVO;

public class CarDetailActivity extends AppCompatActivity {
    CarVO car;

    EditText editText_carName;
    TextView textView_carNum, textView_carModel, textView_carType, textView_carYear, textView_fuelType;
    Button button_modify, button_delete, button_modifyx;
    ImageView imgView_carimg;

    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);
        Intent intent = getIntent();
        car = (CarVO) intent.getSerializableExtra("car");

        button_modify = findViewById(R.id.button_modify_car);

        button_modifyx = findViewById(R.id.button_modifyx_car);
        button_modifyx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        button_delete = findViewById(R.id.button_delete_car);

        editText_carName = findViewById(R.id.d_etx_carName);

        textView_carModel = findViewById(R.id.d_tx_carModel);
        textView_carNum = findViewById(R.id.d_tx_carNum);
        textView_carType = findViewById(R.id.d_tx_carType);
        textView_carYear = findViewById(R.id.d_tx_carYear);
        textView_fuelType = findViewById(R.id.d_tx_fuelType);

        imgView_carimg = findViewById(R.id.imgView_carImg);

//        mHandler = new Handler();
//        Thread t = new Thread(new Runnable(){
//            @Override
//            public void run() { // UI 작업 수행 X
//             mHandler.post(new Runnable(){
//                 @Override
//                 public void run() { // UI 작업 수행 O
                     editText_carName.setText(car.getCarname());
                     textView_carModel.setText(car.getCarmodel());
                     textView_carNum.setText(car.getCarnum());
                     if(car.getCartype().equals("p")){
                         textView_carType.setText("승용차");
                     } else if(car.getCartype().equals("v")){
                         textView_carType.setText("승합차");
                     } else if(car.getCartype().equals("t")){
                         textView_carType.setText("트럭");
                     }
                     textView_carYear.setText(car.getCaryear()+"");

                    if (car.getCarimg().equals("car1.jpg")) {
                     imgView_carimg.setImageResource(R.drawable.car1);
                    } else if (car.getCarimg().equals("car2.jpg")) {
                        imgView_carimg.setImageResource(R.drawable.car2);
                    } else if (car.getCarimg().equals("car3.jpg")) {
                        imgView_carimg.setImageResource(R.drawable.car3);
                    }
                     textView_fuelType.setText(car.getCaroiltype());
//                 }
//             });
//            }
//        });
//        t.start();
    } // onCreate;


    }
