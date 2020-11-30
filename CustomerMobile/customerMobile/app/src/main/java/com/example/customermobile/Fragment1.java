package com.example.customermobile;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

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
    TextView textView_fuel, textView_temper, textView_targetTemper;
    ImageView imageView_car, imageView_weather;
    ImageButton imageButton_carLeft, imageButton_carRight, imageButton_startingOn, imageButton_startingOff;
    ImageButton imageButton_doorOn, imageButton_doorOff, imageButton_downTemper, imageButton_upTemper;

    MyAsynch myAsynch;

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

            }
        }) ;

        imageButton_carRight.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

            }
        }) ;

        imageButton_startingOn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                imageButton_startingOn.setImageResource(R.drawable.startingon1);
                imageButton_startingOff.setImageResource(R.drawable.startingoff);
            }
        }) ;

        imageButton_startingOff.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                imageButton_startingOff.setImageResource(R.drawable.startingoff1);
                imageButton_startingOn.setImageResource(R.drawable.startingon);
            }
        }) ;

        imageButton_doorOn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                imageButton_doorOn.setImageResource(R.drawable.dooropenimgg);
                imageButton_doorOff.setImageResource(R.drawable.doorcloseimg);
            }
        }) ;

        imageButton_doorOff.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                imageButton_doorOn.setImageResource(R.drawable.dooropenimg);
                imageButton_doorOff.setImageResource(R.drawable.doorcloseimgg);
            }
        }) ;


        imageButton_upTemper.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int targetTemper = Integer.parseInt(textView_targetTemper.getText().toString());
                if(targetTemper+1 > 30){
                    Toast.makeText(getActivity(),"30도 이하로 설정해주세요!",Toast.LENGTH_SHORT).show();
                }else{
                    textView_targetTemper.setText(String.valueOf(targetTemper+1));
                }
            }
        }) ;

        imageButton_downTemper.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int targetTemper = Integer.parseInt(textView_targetTemper.getText().toString());
                if(targetTemper-1 < 18){
                    Toast.makeText(getActivity(),"18도 이상으로 설정해주세요!",Toast.LENGTH_SHORT).show();
                }else{
                    textView_targetTemper.setText(String.valueOf(targetTemper-1));
                }
            }
        }) ;

        //setCarData();

        return viewGroup;
    } // end onCreate


    // 차 정보를 세팅하는 함수
    public void setCarData(String carname, String carmodel, String carnum){

        textView_carName.setText(carname);
        textView_carModel.setText(carmodel);
        textView_carNum.setText(carnum);

//        ((MainActivity)getActivity()).getCarData();
//        textView_carName.setText(((MainActivity)getActivity()).getCar().getCarname());
//        textView_carModel.setText(((MainActivity)getActivity()).getCar().getCarmodel());
//        textView_carNum.setText(((MainActivity)getActivity()).getCar().getCarnum());

//        myAsynch = new MyAsynch();
//        myAsynch.execute();

        Log.d("[TAG]", "setCarData OK"+" "+carname+" "+carmodel+" "+carnum);

    }



    class MyAsynch extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            ((MainActivity)getActivity()).getCarData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            textView_carName.setText(((MainActivity)getActivity()).getCar().getCarname());
            textView_carModel.setText(((MainActivity)getActivity()).getCar().getCarmodel());
            textView_carNum.setText(((MainActivity)getActivity()).getCar().getCarnum());
        }
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