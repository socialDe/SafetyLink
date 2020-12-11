package com.example.customermobile.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Movie;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.customermobile.R;
import com.example.customermobile.df.DataFrame;
import com.example.customermobile.network.HttpConnect;
import com.example.customermobile.vo.CarSensorVO;
import com.example.customermobile.vo.CarVO;
import com.example.customermobile.vo.UsersVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

import static com.example.customermobile.activity.LoginActivity.ip;

public class CarRegisterActivity extends AppCompatActivity {

    SharedPreferences sp;
    Button button_logout, button_carNumCheck, button_registerok, button_registercancel;
    UsersVO user;
    CarVO car;

    HttpAsyncTask httpAsyncTask;
    CarNumCheckAsync carNumCheckAsync;
    UserCarRegisterAsync userCarRegisterAsync;

    EditText editText_carNum, editText_carName;
    TextView textView_carModel, textView_carType, textView_carYear, textView_fuelType;
    LinearLayout layout_carInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_register);

        button_logout = findViewById(R.id.button_logout);
        button_carNumCheck = findViewById(R.id.button_carNumCheck);
        button_registerok = findViewById(R.id.button_register);
        button_registercancel = findViewById(R.id.button_registerx);
        button_registercancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        editText_carNum = findViewById(R.id.editText_carNum);

        textView_carModel = findViewById(R.id.textView_carModel);
        textView_carType = findViewById(R.id.textView_carType);
        textView_carYear = findViewById(R.id.textView_carYear);
        textView_fuelType = findViewById(R.id.textView_fuelType);
        editText_carName = findViewById(R.id.editText_carName);
        layout_carInfo = findViewById(R.id.layout_carInfo);

        // 회원정보를 intent로 가져오기
        Intent getintent = getIntent();
        user = null;
        user = (UsersVO) getintent.getSerializableExtra("user");

        sp = getSharedPreferences("user", MODE_PRIVATE);

        // intent 정보가 없을 경우, sp로 회원정보 가져오기
        if (user == null) {
            String userid = sp.getString("userid", "");

            // 자동로그인 정보가 있으면 회원정보 계속 가져오기
            String userpwd = sp.getString("userpwd", "");
            String username = sp.getString("username", "");
            String userphone = sp.getString("userphone", "");
            String strbirth = sp.getString("userbirth", "");
            String usersex = sp.getString("usersex", "");
            String strregdate = sp.getString("userregdate", "");
            String userstate = sp.getString("userstate", "");
            String usersubject = sp.getString("usersubject", "");
            String babypushcheck = sp.getString("babypushcheck", "");
            String accpushcheck = sp.getString("accpushcheck", "");
            String mobiletoken = sp.getString("mobiletoken", "");

            // String 변수를 Date로 변환
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date userbirth = null;
            Date userregdate = null;
            try {
                userbirth = sdf.parse(strbirth);
                userregdate = sdf.parse(strregdate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            // sp 정보로 회원 객체 생성
            user = new UsersVO(userid, userpwd, username, userphone, userbirth, usersex, userregdate, userstate, usersubject, babypushcheck, accpushcheck, mobiletoken);

        }

        // 로그아웃 버튼
        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();

                String url = "http://" + ip + "/webServer/userlogoutimpl.mc";
                url += "?id=" + user.getUserid() + "&destroy=no";
                httpAsyncTask = new HttpAsyncTask();
                httpAsyncTask.execute(url);
            }
        });

        layout_carInfo.setVisibility(View.INVISIBLE);

    }// end oncreate

    public void bt_checkNum(View v){
        final String carnum = editText_carNum.getText().toString();
        //인증번호 생성
        Random r = new Random();
        final int number = r.nextInt(9000)+1000;
        Log.d("[Server]","인증번호는 : "+number);

        SendNumberFcm(carnum, number);

        final EditText edittext = new EditText(CarRegisterActivity.this);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(CarRegisterActivity.this);
        alertDialog.setTitle("인증번호")
                .setView(edittext)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(edittext.getText().toString().equals(number+"")){
                                String url = "http://" + ip + "/webServer/carnumcheckimpl.mc";
                                url += "?carnum=" + carnum;
                                carNumCheckAsync = new CarNumCheckAsync();
                                carNumCheckAsync.execute(url);
                            } else{
                            Toast.makeText(CarRegisterActivity.this,"번호가 틀렸습니다",Toast.LENGTH_SHORT).show();
                        }
                        }
                })
        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(CarRegisterActivity.this,"취소되었습니다",Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.create().show();

    }

    // 인증번호 보내는 fcm
    public void SendNumberFcm(String carnum, int number) {
        String urlstr = "http://" + ip + "/webServer/sendnumberfcm.mc";
        String conrtolUrl = urlstr + "?carnum=" + carnum +"&number=" + number;

        Log.d("[TEST]", conrtolUrl);

        // AsyncTask를 통해 HttpURLConnection 수행.
        ControlAsync controlAsync = new ControlAsync();
        controlAsync.execute(conrtolUrl);
    }

    class ControlAsync extends AsyncTask<String, Void, Void> {

        public Void result;

        @Override
        protected Void doInBackground(String... strings) {
            String url = strings[0];
            HttpConnect.getString(url); //result는 JSON
            return result;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }

    public void bt_register(View v){
        String carnum = editText_carNum.getText().toString();
        String carmodel = textView_carModel.getText().toString();
        String cartype = textView_carType.getText().toString();
        String caryear = textView_carYear.getText().toString();
        String fueltype = textView_fuelType.getText().toString();
        String carname = editText_carName.getText().toString();

        String url = "http://" + ip + "/webServer/usercarregisterimpl.mc";
        url += "?userid="+user.getUserid()+"&carnum=" + carnum + "&carmodel=" + carmodel + "&cartype=" + cartype +
                "&caryear=" + caryear + "&fueltype=" + fueltype + "&carname=" + carname;
        userCarRegisterAsync = new UserCarRegisterAsync();
        userCarRegisterAsync.execute(url);
    }

    class UserCarRegisterAsync extends AsyncTask<String, Void, Void> {

        public Void result;

        @Override
        protected Void doInBackground(String... strings) {
            String url = strings[0];
            HttpConnect.getString(url); //result는 JSON
            return result;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Intent intent = new Intent(getApplicationContext(), CarActivity.class);
            intent.putExtra("user",user);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }


    /*
     HTTP 통신 Code
     */
    class HttpAsyncTask extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(CarRegisterActivity.this);
            progressDialog.setTitle("로그아웃");
            progressDialog.setCancelable(false);

            if (sp.getString("userid", "") == null) {
                progressDialog.show();
            } else {

            }
        }

        @Override
        protected String doInBackground(String... strings) {
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
            String result = s.trim();
            if (result.equals("logoutsuccess")) {
                // 로그아웃
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            } else if (result.equals("destroy")) {
                // destroy
            } else if (result.equals("logoutfail")) {
                // 로그아웃 실패: Exception
                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(CarRegisterActivity.this);
                builder.setTitle("로그아웃에 실패하였습니다.");
                builder.setMessage("다시 시도해 주십시오.");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

                builder.show();
            }
        }
    }
    // End HTTP 통신 Code

    @Override
    protected void onDestroy() {
        String url = "http://" + ip + "/webServer/userlogoutimpl.mc";
        String destroy = "yes";

        url += "?id=" + user.getUserid() + "&destroy=" + destroy;
        httpAsyncTask = new HttpAsyncTask();
        httpAsyncTask.execute(url);
        super.onDestroy();
    }


    // '등록 확인' 버튼 클릭 시 자동차 정보 가져옴
    class CarNumCheckAsync extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(CarRegisterActivity.this);
            progressDialog.setTitle("자동차 정보 조회 중 ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0];
            String result = HttpConnect.getString(url); //result는 JSON
            return result;
        }

        @Override
        protected void onPostExecute(String s) {

            progressDialog.dismiss();
            JSONArray ja = null;
            try {
                ja = new JSONArray(s);
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);

                    String carmodel = jo.getString("carmodel");
                    textView_carModel.setText(carmodel);
                    String cartype = jo.getString("cartype");
                    if (cartype.equals("p")) {
                        cartype = "승용차";
                    } else if (cartype.equals("v")) {
                        cartype = "승합차";
                    } else if (cartype.equals("t")) {
                        cartype = "트럭";
                    }
                    textView_carType.setText(cartype);
                    int caryear = Integer.parseInt(jo.getString("caryear"));
                    textView_carYear.setText(caryear+"");
                    String fueltype = jo.getString("fueltype");
                    textView_fuelType.setText(fueltype);
                    layout_carInfo.setVisibility(View.VISIBLE);

                }
                textView_carName.setVisibility(View.VISIBLE);
                textView_carModel.setVisibility(View.VISIBLE);
                textView_carYear.setVisibility(View.VISIBLE);
                textView_carType.setVisibility(View.VISIBLE);
                textView_fuelType.setVisibility(View.VISIBLE);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}