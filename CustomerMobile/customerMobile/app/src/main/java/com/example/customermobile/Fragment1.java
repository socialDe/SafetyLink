package com.example.customermobile;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class Fragment1 extends Fragment {

    TextView textView_carName, textView_carModel, textView_carNum, textView_weatherTemper;
    TextView textView_todayDate, textView_address, textView_weather, textView_possibleDistance;
    TextView textView_fuel, textView_temper, textView_targetTemper, textView_moving;
    ImageView imageView_car, imageView_weather;
    ImageButton imageButton_carLeft, imageButton_carRight, imageButton_startingOn, imageButton_startingOff;
    ImageButton imageButton_doorOn, imageButton_doorOff, imageButton_downTemper, imageButton_upTemper;

    // 버튼 계속 안눌리게 하기 위한 스위치 변수
    int startingSW = 0;
    int doorSW = 0;

    int targetTemper;
    TemperTimer temperTimer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        ViewGroup viewGroup = null;
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment1,container,false);


        textView_carName = viewGroup.findViewById(R.id.textView_carName);
        textView_carModel = viewGroup.findViewById(R.id.textView_carModel);
        textView_carNum = viewGroup.findViewById(R.id.textView_carNum);
        textView_weatherTemper = viewGroup.findViewById(R.id.textView_weatherTemper);
        textView_todayDate = viewGroup.findViewById(R.id.textView_todayDate);
        textView_address = viewGroup.findViewById(R.id.textView_address);
        textView_weather = viewGroup.findViewById(R.id.textView_weather);
        textView_possibleDistance = viewGroup.findViewById(R.id.textView_possibleDistance);
        textView_fuel = viewGroup.findViewById(R.id.textView_fuel);
        textView_temper = viewGroup.findViewById(R.id.textView_temper);
        textView_targetTemper = viewGroup.findViewById(R.id.textView_targetTemper);
        textView_moving = viewGroup.findViewById(R.id.textView_moving);
        imageView_car = viewGroup.findViewById(R.id.imageView_car);
        imageView_weather = viewGroup.findViewById(R.id.imageView_weather);
        imageButton_carLeft = viewGroup.findViewById(R.id.imageButton_carLeft);
        imageButton_carRight = viewGroup.findViewById(R.id.imageButton_carRight);
        imageButton_startingOn = viewGroup.findViewById(R.id.imageButton_startingOn);
        imageButton_startingOff = viewGroup.findViewById(R.id.imageButton_startingOff);
        imageButton_doorOn = viewGroup.findViewById(R.id.imageButton_doorOn);
        imageButton_doorOff = viewGroup.findViewById(R.id.imageButton_doorOff);
        imageButton_downTemper = viewGroup.findViewById(R.id.imageButton_downTemper);
        imageButton_upTemper = viewGroup.findViewById(R.id.imageButton_upTemper);




        imageButton_carLeft.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
               ((CarActivity)getActivity()).clickcarleft();
            }
        }) ;

        imageButton_carRight.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                ((CarActivity)getActivity()).clickcarright();
            }
        }) ;


        imageButton_startingOn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(startingSW == 0){
                    startingSW = 1;

                    ((CarActivity)getActivity()).control("starting","o");

                    imageButton_startingOn.setImageResource(R.drawable.startingon1);
                    imageButton_startingOff.setImageResource(R.drawable.startingoff);

                    ((CarActivity)getActivity()).vibrate(300,3);
                }

            }
        }) ;

        imageButton_startingOff.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(startingSW == 1){
                    startingSW = 0;

                    ((CarActivity)getActivity()).control("starting","f");

                    imageButton_startingOff.setImageResource(R.drawable.startingoff1);
                    imageButton_startingOn.setImageResource(R.drawable.startingon);

                    ((CarActivity)getActivity()).vibrate(300,3);
                }

            }
        }) ;

        imageButton_doorOn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(doorSW == 0) {
                    doorSW = 1;

                    ((CarActivity) getActivity()).control("door", "o");

                    imageButton_doorOn.setImageResource(R.drawable.dooropenimgg);
                    imageButton_doorOff.setImageResource(R.drawable.doorcloseimg);

                    ((CarActivity) getActivity()).vibrate(300, 3);
                }

            }
        }) ;

        imageButton_doorOff.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(doorSW == 1) {
                    doorSW = 0;

                    ((CarActivity)getActivity()).control("starting","f");

                    imageButton_doorOn.setImageResource(R.drawable.dooropenimg);
                    imageButton_doorOff.setImageResource(R.drawable.doorcloseimgg);

                    ((CarActivity)getActivity()).vibrate(300,3);
                }

            }
        }) ;


        // 현재 타겟온도 가져오기
        targetTemper = Integer.parseInt(textView_targetTemper.getText().toString());
        // 온도 타이머 3초로 세팅하기
        temperTimer = new TemperTimer(3000, 1000);


        imageButton_upTemper.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                targetTemper = targetTemper + 1;

                if(targetTemper > 30){
                    Toast.makeText(getActivity(),"30도 이하로 설정해주세요!",Toast.LENGTH_SHORT).show();
                }else{
                    textView_targetTemper.setText(String.valueOf(targetTemper));

                    temperTimer.cancel();
                    temperTimer.start();
                }
                
            }


        }) ;

        imageButton_downTemper.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                targetTemper = targetTemper - 1;

                if(targetTemper < 18){
                    Toast.makeText(getActivity(),"18도 이상으로 설정해주세요!",Toast.LENGTH_SHORT).show();
                }else{
                    textView_targetTemper.setText(String.valueOf(targetTemper));

                    temperTimer.cancel();
                    temperTimer.start();
                }

            }
        }) ;


        return viewGroup;
    } // end onCreate



    // 온도설정을 위한 타이머
    class TemperTimer extends CountDownTimer
    {
        public TemperTimer(long millisInFuture, long countDownInterval)
        {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            ((CarActivity)getActivity()).control("temper",String.valueOf(targetTemper));

            Toast t = Toast.makeText(getActivity(),"차량온도가 "+targetTemper+"로 세팅합니다!",Toast.LENGTH_SHORT);
            t.show();
            ((CarActivity)getActivity()).vibrate(500,5);

        }
    }



    // 차 정보를 세팅하는 함수
    public void setCarData(String carname, String carmodel, String carnum, String carimg){

        textView_carName.setText(carname);
        textView_carModel.setText(carmodel);
        textView_carNum.setText(carnum);

        int[] imglist = {R.drawable.car1,R.drawable.car2};


        if(carimg.equals("car1.jpg")){
            imageView_car.setImageResource(imglist[0]);
        }else if(carimg.equals("car2.jpg")){
            imageView_car.setImageResource(imglist[1]);
        }


        Log.d("[TAG]", "setCarData OK"+" "+carname+" "+carmodel+" "+carnum+" "+carimg);

    }

    // 차센서 정보를 세팅하는 함수
    public void setCarSensorData(String moving, int fuel, String starting, String door, int temper){

        if(moving.equals("o")){
            textView_moving.setText("주행중");
            textView_moving.setTextColor(Color.GREEN);
        }else{
            textView_moving.setText("정차");
            textView_moving.setTextColor(Color.RED);
        }
        textView_fuel.setText(String.valueOf(fuel));
        textView_possibleDistance.setText(String.valueOf(fuel*12));
        Log.d("[TAG]","Here:"+fuel+" "+starting+" "+door+" "+temper);
        if(starting.equals("o")){
            startingSW = 1;
            imageButton_startingOn.setImageResource(R.drawable.startingon1);
            imageButton_startingOff.setImageResource(R.drawable.startingoff);
        }else{
            startingSW = 0;
            imageButton_startingOff.setImageResource(R.drawable.startingoff1);
            imageButton_startingOn.setImageResource(R.drawable.startingon);
        }
        if(door.equals("o")){
            doorSW = 1;
            imageButton_doorOn.setImageResource(R.drawable.dooropenimgg);
            imageButton_doorOff.setImageResource(R.drawable.doorcloseimg);
        }else{
            doorSW = 0;
            imageButton_doorOn.setImageResource(R.drawable.dooropenimg);
            imageButton_doorOff.setImageResource(R.drawable.doorcloseimgg);
        }
        textView_temper.setText(String.valueOf(temper));


        Log.d("[TAG]", "setCarSensorData OK"+" "+fuel+" "+starting+" "+door+" "+temper);

    }






    // FCM 발신
    // 데이터를 Push Message에 넣어서 보내는 send 함수(제어)
    public void sendFcm(String control, String input) { // String control, String input 으로 변경하기 !
        System.out.println("phone Send Start...");
        URL url = null;
        try {
            url = new URL("https://fcm.googleapis.com/fcm/send");
        } catch (MalformedURLException e) {
            System.out.println("Error while creating Firebase URL | MalformedURLException");
            e.printStackTrace();
        }
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            System.out.println("Error while createing connection with Firebase URL | IOException");
            e.printStackTrace();
        }
        conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");

        // set my firebase server key
        conn.setRequestProperty("Authorization", "key="
// 세미프로젝트 키 + "AAAAK89FyMY:APA91bGxNwkQC6S_QQAKbn3COepWgndhyyjynT8ZvIEarTaGpEfMA1SPFo-ReN8b9uO21R1OfSOpNhfYbQaeohKP_sKzsgVTxu7K5tmzcjEfHzlgXRFrB1r0uqhfxLp4p836lbKw_iaN");
                + "AAAAeDPCqVw:APA91bH08TNojrp8rdBiVAsIcwTeK5k6ITDZ4q8k5t-FRdEEQiRbFb5I46TAt-0NDg7xQsf9MxTZ7muyKtEeK__IygsotH3G4c4_e--VdDXRub-6H_mL9qetJu7fA-1XR9ip0xG-Q-4i");

        // create notification message into JSON format
        JSONObject message = new JSONObject();
        try {
            message.put("to", "/topics/car");
            message.put("priority", "high");

            JSONObject notification = new JSONObject();
            notification.put("title", "HyunDai");
            notification.put("body", "자동차 상태 변경");
            message.put("notification", notification);

            JSONObject data = new JSONObject();
            data.put("control", control); // 이 부분 변경하며 temp, door, power 등 조절
            data.put("data", input);
            message.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            System.out.println("FCM 전송:" + message.toString());
            out.write(message.toString());
            out.flush();
            conn.getInputStream();
            System.out.println("OK...............");

        } catch (IOException e) {
            System.out.println("Error while writing outputstream to firebase sending to ManageApp | IOException");
            e.printStackTrace();
        }

        System.out.println("phone Send End...");
    }



}