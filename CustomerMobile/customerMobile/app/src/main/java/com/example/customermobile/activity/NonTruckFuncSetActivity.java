package com.example.customermobile.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.customermobile.R;
import com.example.customermobile.network.HttpConnect;
import com.example.customermobile.vo.CarVO;
import com.example.customermobile.vo.UsersVO;

import static com.example.customermobile.activity.LoginActivity.ip;

public class NonTruckFuncSetActivity extends AppCompatActivity {
    CarVO nowCar;
    int nowCarId;
    String userId;
    UsersVO user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_none_truck_funcset);



        Intent intent = getIntent();
        nowCar = (CarVO) intent.getSerializableExtra("nowCar");
        nowCarId = intent.getIntExtra("nowCarId", 0);
        user = (UsersVO) intent.getSerializableExtra("user");
        Log.d("[SET_TEST]", user.toString());
        Log.d("[SET_TEST]", nowCar.toString());
        System.out.println("getExtra Car: "+nowCarId);



        // URL 설정.
        //String carUrl = "http://" + ip + "/webServer/cardata.mc?userid=" + user.getUserid();

        // AsyncTask를 통해 HttpURLConnection 수행.
        NonTruckFuncSetActivity.CarSettingAsync carSettingAsync = new CarSettingAsync();
        //carSettingAsync.execute(carUrl);


    }

    class CarSettingAsync extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0];
            String result = HttpConnect.getString(url); //result는 JSON
            Log.d("[TAG]", result);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {

        }
    }
}
