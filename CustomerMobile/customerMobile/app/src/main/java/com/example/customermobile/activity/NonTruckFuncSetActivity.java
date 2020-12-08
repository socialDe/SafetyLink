package com.example.customermobile.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.customermobile.R;
import com.example.customermobile.network.HttpConnect;
import com.example.customermobile.vo.CarVO;
import com.example.customermobile.vo.UsersVO;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.customermobile.activity.LoginActivity.ip;

public class NonTruckFuncSetActivity extends AppCompatActivity {
    CarVO nowCar;
    int nowCarId;
    String userId;
    UsersVO user;
    TextView textView_carName, textView_carModel, textView_carNum;
    ImageButton imageButton_fcm, imageButton_acc, imageButton_sleep, imageButton_baby;
    ImageView imageView_car;
    SafetyFuncSetAsync safetyFuncSetAsync;
    SetInfoAsyncTask setInfoAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_none_truck_funcset);

        textView_carName = findViewById(R.id.textView_carName);
        textView_carModel = findViewById(R.id.textView_carModel);
        textView_carNum = findViewById(R.id.textView_carNum);
        imageView_car = findViewById(R.id.imageView_car);
        imageButton_acc = findViewById(R.id.imageButton_acc);
        imageButton_baby = findViewById(R.id.imageButton_baby);
        imageButton_fcm = findViewById(R.id.imageButton_fcm);
        imageButton_sleep = findViewById(R.id.imageButton_sleep);


        Intent intent = getIntent();
        nowCar = (CarVO) intent.getSerializableExtra("nowCar");
        nowCarId = intent.getIntExtra("nowCarId", 0);
        userId = intent.getStringExtra("userId");

        setCarData(nowCar.getCarname(),nowCar.getCarmodel(),nowCar.getCarnum(), nowCar.getCarimg());

        getFuncSetInfo(userId);


        /*
         * ImageButton들의 Event Processing
         */

        imageButton_fcm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UsersVO userRequest = user;
                if(userRequest.getUsersubject().equals("car")){

                    String event = "fcm";
                    String value = "f";
                    changeFuncSet(event, value);
                    user.setUsersubject("f");
                    imageButton_fcm.setImageResource(R.drawable.icon1);

                }else if(userRequest.getUsersubject().equals("f")){
                    String event = "fcm";
                    String value = "car";
                    changeFuncSet(event, value);
                    user.setUsersubject("car");
                    imageButton_fcm.setImageResource(R.drawable.icon1_g);
                }
            }
        });

        imageButton_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UsersVO userRequest = user;
                if(userRequest.getAccpushcheck().equals("o")){
                    String event = "acc";
                    String value = "f";
                    changeFuncSet(event, value);
                    user.setAccpushcheck("f");
                    imageButton_acc.setImageResource(R.drawable.icon3);

                }else if(userRequest.getAccpushcheck().equals("f")){
                    String event = "acc";
                    String value = "o";
                    changeFuncSet(event, value);
                    user.setAccpushcheck("o");
                    imageButton_acc.setImageResource(R.drawable.icon3_g);
                }
            }
        });

        imageButton_sleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UsersVO userRequest = user;
                if(userRequest.getSleeppushcheck().equals("o")){
                    String event = "sleep";
                    String value = "f";
                    changeFuncSet(event, value);
                    user.setSleeppushcheck("f");
                    imageButton_sleep.setImageResource(R.drawable.icon2);

                }else if(userRequest.getSleeppushcheck().equals("f")){
                    String event = "sleep";
                    String value = "o";
                    changeFuncSet(event, value);
                    user.setSleeppushcheck("o");
                    imageButton_sleep.setImageResource(R.drawable.icon2_g);
                }
            }
        });

        imageButton_baby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UsersVO userRequest = user;
                if(userRequest.getBabypushcheck().equals("o")){
                    String event = "baby";
                    String value = "f";
                    changeFuncSet(event, value);
                    user.setBabypushcheck("f");
                    imageButton_baby.setImageResource(R.drawable.icon4);

                }else if(userRequest.getBabypushcheck().equals("f")){
                    String event = "baby";
                    String value = "o";
                    changeFuncSet(event, value);
                    user.setBabypushcheck("o");
                    imageButton_baby.setImageResource(R.drawable.icon4_g);
                }
            }
        });

        /*
         * End ImageButton들의 Event Processing
         */
    }
    /*
     * End onCreate Method
     */

    public int changeFuncSet(String event, String value){
        int result = 9999;
        String funcSetUrl = "http://" + ip + "/webServer/safetyFuncSet.mc?userid="+user.getUserid()+"&func="+event+"&value="+value;
        safetyFuncSetAsync = new SafetyFuncSetAsync();
        safetyFuncSetAsync.execute(funcSetUrl);
        return result;
    }

    /*
     * 사용자의 기능 설정에 따라 ImageButton 화면 처리
     */
    public void getFuncSetInfo(String userId){
        String url = "http://"+ip+"/webServer/getUserInfo.mc";
        url += "?id="+userId;
        setInfoAsyncTask = new SetInfoAsyncTask();
        setInfoAsyncTask.execute(url);
    }

    public void setFuncSetImageButton(String userSubject, String accPushCheck, String sleepPushCheck, String babyPushCheck){

        if(userSubject.equals("car")){
            imageButton_fcm.setImageResource(R.drawable.icon1_g);
        }else if(userSubject.equals("f")){
            imageButton_fcm.setImageResource(R.drawable.icon1);
        }


        if(accPushCheck.equals("o")){
            imageButton_acc.setImageResource(R.drawable.icon3_g);
        }else if(accPushCheck.equals("f")){
            imageButton_acc.setImageResource(R.drawable.icon3);
        }

        if(sleepPushCheck.equals("o")){
            imageButton_sleep.setImageResource(R.drawable.icon2_g);
        }else if(sleepPushCheck.equals("f")){
            imageButton_sleep.setImageResource(R.drawable.icon2);
        }

        if(babyPushCheck.equals("o")){
            imageButton_baby.setImageResource(R.drawable.icon4_g);
        }else if(babyPushCheck.equals("f")){
            imageButton_baby.setImageResource(R.drawable.icon4);
        }
    }

    /*
     * WebServer로 안전 기능 Setting 업데이트 요청
     */

    class SafetyFuncSetAsync extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0];
            String result = HttpConnect.getString(url); //result는 JSON
            Log.d("[SET_TEST]", result);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {

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


        Log.d("[SET_TEST]", "setCarData OK"+" "+carname+" "+carmodel+" "+carnum+" "+carimg);

    }

    /*
     * 사용자의 DB 내 안전기능 세팅 정보를 받아오는 HTTP
     */

    class SetInfoAsyncTask extends AsyncTask<String,String,String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(NonTruckFuncSetActivity.this);
            progressDialog.setTitle("Loding...");
            progressDialog.setCancelable(false);
            //progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
//            userData = new UsersVO();
//            userData.setUserid(urls[1]);
//            userData.setUserpwd(urls[2]);
//            Log.d("[Server]", "[AsyncTask Background]" + urls[0] + urls[1] + urls[2] + urls);
//            return POST(urls[0], userData);

            String url = strings[0].toString();
            String result = HttpConnect.getString(url);
            return result;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            final String result = s.trim();

            Log.d("[SET_TEST]","[SET_TEST]:"+result);

            if (result.equals("fail")) {
                // LOGIN FAIL
                AlertDialog.Builder dailog = new AlertDialog.Builder(NonTruckFuncSetActivity.this);
                dailog.setTitle("정보 불러오기를 실패하였습니다.");
                dailog.setMessage("다시 시도해 주십시오.");
                dailog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                dailog.show();
            } else {
                // Load SUCCESS
                JSONObject jo = null;
                try {
                    // JSONObject 값 가져오기
                    jo = new JSONObject(s);
                    String userid = jo.getString("userid");
                    String usersubject = jo.getString("usersubject");
                    String babypushcheck = jo.getString("babypushcheck");
                    String accpushcheck = jo.getString("accpushcheck");
                    String sleeppushcheck = jo.getString("sleeppushcheck");
                    String droppushcheck = jo.getString("droppushcheck");

                    // user 객체 설정
                    user = new UsersVO(userid, usersubject, babypushcheck, accpushcheck, sleeppushcheck, droppushcheck);
                    Log.d("[SET_TEST]","User Setting Data Loading Completed: "+user.toString());
                    setFuncSetImageButton(usersubject, accpushcheck, sleeppushcheck, babypushcheck);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
