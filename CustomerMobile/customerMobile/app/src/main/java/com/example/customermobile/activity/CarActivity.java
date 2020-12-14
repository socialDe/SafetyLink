package com.example.customermobile.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.customermobile.Fragment1;
import com.example.customermobile.Fragment2;
import com.example.customermobile.Fragment3;
import com.example.customermobile.GpsTracker;
import com.example.customermobile.network.HttpConnect;
import com.example.customermobile.R;
import com.example.customermobile.df.DataFrame;
import com.example.customermobile.vo.CarSensorVO;
import com.example.customermobile.vo.CarVO;
import com.example.customermobile.vo.UsersVO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.owl93.dpb.CircularProgressView;
import com.skydoves.progressview.OnProgressChangeListener;
import com.skydoves.progressview.ProgressView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.customermobile.activity.LoginActivity.ip;

public class CarActivity extends AppCompatActivity {

    SharedPreferences sp;

    UsersVO user;
    CarVO car;
    CarSensorVO carsensor;

    CircularProgressView circularProgressView;
    ProgressView progressView;

    Toolbar toolbar;
    TextView toolbar_title;

    Fragment1 fragment1;
    Fragment2 fragment2;
    Fragment3 fragment3;

    // 소셜로그인
    private FirebaseAuth mAuth;

    HttpAsyncTask httpAsyncTask;

    int carlistnum = 0;
    int nowcarid;
    String nowcarnum = ""; // 현재 차 번호판 번호
    CarVO nowCar = new CarVO();


    ArrayList<CarVO> carlist = null;
    ArrayList<CarSensorVO> carsensorlist = null;

    NotificationManager manager;

    //  네이게이션 드로우어어
    private DrawerLayout mDrawerLayout;

    public CarDataTimer carDataTimer;

    // GPS 위치 정보 수신 권한
    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car);

        if (!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }

        // 소셜 로그인
        mAuth = FirebaseAuth.getInstance();


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
            String sleeppushcheck = sp.getString("sleeppushcheck","");
            String droppushcheck = sp.getString("droppushcheck","");
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
            user = new UsersVO(userid, userpwd, username, userphone, userbirth, usersex, userregdate, userstate, usersubject, babypushcheck, accpushcheck, sleeppushcheck, droppushcheck, mobiletoken);


        }

        // 상단 바 설정
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title = findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.mipmap.menuicon); //뒤로가기 버튼 이미지 지정


        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        // 네비게이션 화면 설정
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                int id = menuItem.getItemId();
                String title = menuItem.getTitle().toString();

                if (id == R.id.home) {
                    onChangedFragment(1, null);
                } else if (id == R.id.mypage) {
                    onChangedFragment(2, null);
                } else if (id == R.id.map) {
                    onChangedFragment(3, null);
                } else if (id == R.id.logout) {
                    // 자동 로그인 정보 삭제
                    SharedPreferences.Editor editor = sp.edit();
                    editor.clear();
                    editor.commit();


                    String url = "http://" + ip + "/webServer/userlogoutimpl.mc";
                    url += "?id=" + user.getUserid() + "&destroy=no";
                    httpAsyncTask = new HttpAsyncTask();
                    httpAsyncTask.execute(url);
                }

                return true;
            }
        });


        // 프래그먼트 화면 설정
        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        fragment3 = new Fragment3();

        // 시작 프래그먼트 지정
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment1).commit();


        // FCM사용 (앱이 중단되어 있을 때 기본적으로 title,body값으로 푸시!!)
        FirebaseMessaging.getInstance().subscribeToTopic("car"). //구독, 이걸로 원하는 기능 설정하기(파이널 때, db 활용)
                addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                String msg = "FCM Complete...";
                if (!task.isSuccessful()) {
                    msg = "FCM Fail";
                }
                Log.d("[TAG]", msg);
            }
        });


        // 여기서 부터는 앱 실행상태에서 상태바 설정!!
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this); // 브로드캐스트를 받을 준비
        lbm.registerReceiver(receiver, new IntentFilter("notification")); // notification이라는 이름의 정보를 받겠다

        getCarData();

        carDataTimer = new CarDataTimer(3000, 1000);
        carDataTimer.start();


        // GPS 수신 활용 동의 여부 Check
        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        }else {
            checkRunTimePermission();
        }

    }// end onCreat


    public UsersVO getNowUser(){
        return user;
    }

    // 현재 선택된 차량ID를 Fragment로 제공
    public int getNowCarId(){
        System.out.println("nowcarID: "+nowcarid);
        return nowcarid;
    }

    // 현재 선택된 차량데이터를 Fragment로 제공
    public CarVO getNowCar(){
        System.out.println("nowcar: "+nowCar);
        return nowCar;
    }


    // 차정보를 가져오는 설정을 위한 타이머
    public class CarDataTimer extends CountDownTimer {
        public CarDataTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            getCarData();
            carDataTimer.start();
        }
    }


    public void getCarData() {
        // URL 설정.
        String carUrl = "http://" + ip + "/webServer/cardata.mc?userid=" + user.getUserid();

        // AsyncTask를 통해 HttpURLConnection 수행.
        CarAsync carAsync = new CarAsync();
        carAsync.execute(carUrl);
    }

    public void getCarSensorData() {
        // URL 설정
        String carSensorUrl = "http://" + ip + "/webServer/carsensordata.mc?userid=" + user.getUserid();


        // AsyncTask를 통해 HttpURLConnection 수행.
        CarSensorAsync carSensorAsync = new CarSensorAsync();
        carSensorAsync.execute(carSensorUrl);
    }

    public void sendfcm(String contents) {
        String urlstr = "http://" + ip + "/webServer/sendfcm.mc";
        String conrtolUrl = urlstr + "?carnum=" + nowcarnum + "&contents=" + contents;

        Log.d("[TEST]", conrtolUrl);

        // AsyncTask를 통해 HttpURLConnection 수행.
        SendFcmAsync sendFcmAsync = new SendFcmAsync();
        sendFcmAsync.execute(conrtolUrl);
    }


    public void vibrate(int sec, int power) {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE); // 진동 없애려면 삭제
        if (Build.VERSION.SDK_INT >= 26) { //버전 체크를 해줘야 작동하도록 한다
            vibrator.vibrate(VibrationEffect.createOneShot(sec, power));
        } else {
            vibrator.vibrate(sec);
        }
    }

    class CarAsync extends AsyncTask<String, Void, String> {

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

            Log.d("[TEST]", "s:" + s);

            // userid가 car를 갖고있지 않으면 차량을 등록하는 화면으로 넘긴다
            if (s == null || s.equals("[]")) {
                Intent intent = new Intent(getApplicationContext(), CarRegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("userid", user.getUserid());
                startActivity(intent);
            }
            // 차별 차정보를 저장한다
            else {
                JSONArray ja = null;
                try {
                    ja = new JSONArray(s);
                    carlist = new ArrayList<>();

                    for (int i = 0; i < ja.length(); i++) {
                        JSONObject jo = ja.getJSONObject(i);

                        int carid = jo.getInt("carid");
                        String userid = jo.getString("userid");
                        String carnum = jo.getString("carnum");
                        String carname = jo.getString("carname");
                        String cartype = jo.getString("cartype");
                        String carmodel = jo.getString("carmodel");
                        int caryear = jo.getInt("caryear");
                        String carimg = jo.getString("carimg");
                        String caroiltype = jo.getString("caroiltype");
                        String tablettoken = jo.getString("tablettoken");

                        car = new CarVO(carid, userid, carnum, carname, cartype, carmodel, caryear, carimg, caroiltype, tablettoken);

                        carlist.add(car);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                fragment1.setCarData(carlist.get(carlistnum).getCarname(), carlist.get(carlistnum).getCarmodel(), carlist.get(carlistnum).getCarnum(), carlist.get(carlistnum).getCarimg());


                nowcarid = carlist.get(carlistnum).getCarid();
                nowcarnum = carlist.get(carlistnum).getCarnum();

                nowCar = carlist.get(carlistnum);

                //차 정보를 가져온 이후 차센서 정보를 가져온다
                getCarSensorData();
            }


        }
    }


    class CarSensorAsync extends AsyncTask<String, Void, String> {

        //ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
//            progressDialog = new ProgressDialog(CarActivity.this);
//            progressDialog.setTitle("Get Data ...");
//            progressDialog.setCancelable(false);
//            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0];
            String result = HttpConnect.getString(url); //result는 JSON
            return result;
        }

        @Override
        protected void onPostExecute(String s) {

            //progressDialog.dismiss();
            JSONArray ja = null;
            try {
                ja = new JSONArray(s);
                carsensorlist = new ArrayList<>();
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);

                    int carid = jo.getInt("carid");
                    int heartbeat = jo.getInt("heartbeat");
                    String pirfront = jo.getString("pirfront");
                    String pirrear = jo.getString("pirrear");
                    int freight = jo.getInt("freight");
                    int fuel = jo.getInt("fuel");
                    int fuelmax = 50;
                    int temper = jo.getInt("temper");
                    String starting = jo.getString("starting");
                    String moving = jo.getString("moving");

                    //Date movingstarttime = new Date();

                    //날자 문자열에서 날자형식으로 변환
                    Date movingstarttime = null;

                    //                    String movingstarttimeString = jo.getString("movingstarttime");
                    //                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    //                    try {
                    //                        movingstarttime = sdf.parse(movingstarttimeString);
                    //                    }
                    //                    catch(ParseException e){
                    //                        e.printStackTrace();
                    //                    }

                    String aircon = jo.getString("aircon");
                    String crash = jo.getString("crash");
                    String door = jo.getString("door");
                    double lat = jo.getDouble("lat");
                    double lng = jo.getDouble("lng");


                    carsensor = new CarSensorVO(carid, heartbeat, pirfront, pirrear, freight, fuel, fuelmax, temper, starting, moving, movingstarttime, aircon, crash, door, lat, lng);

                    carsensorlist.add(carsensor);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            fragment1.setCarSensorData(carsensorlist.get(carlistnum).getMoving(), carsensorlist.get(carlistnum).getFuel(), carsensorlist.get(carlistnum).getStarting(), carsensorlist.get(carlistnum).getDoor(), carsensorlist.get(carlistnum).getTemper(),carsensorlist.get(carlistnum).getAircon());

        }
    }


    class SendFcmAsync extends AsyncTask<String, Void, Void> {

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


    public void clickcarleft() {
        int maxnum = carlist.size() - 1;

        if (carlistnum - 1 >= 0) {
            carlistnum = carlistnum - 1;
            fragment1.setCarData(carlist.get(carlistnum).getCarname(), carlist.get(carlistnum).getCarmodel(), carlist.get(carlistnum).getCarnum(), carlist.get(carlistnum).getCarimg());
            fragment1.setCarSensorData(carsensorlist.get(carlistnum).getMoving(), carsensorlist.get(carlistnum).getFuel(), carsensorlist.get(carlistnum).getStarting(), carsensorlist.get(carlistnum).getDoor(), carsensorlist.get(carlistnum).getTemper(),carsensorlist.get(carlistnum).getAircon());
            nowcarid = carlist.get(carlistnum).getCarid();
            nowCar = carlist.get(carlistnum);
        } else {
            carlistnum = maxnum;
            fragment1.setCarData(carlist.get(maxnum).getCarname(), carlist.get(maxnum).getCarmodel(), carlist.get(maxnum).getCarnum(), carlist.get(maxnum).getCarimg());
            fragment1.setCarSensorData(carsensorlist.get(maxnum).getMoving(), carsensorlist.get(maxnum).getFuel(), carsensorlist.get(maxnum).getStarting(), carsensorlist.get(maxnum).getDoor(), carsensorlist.get(maxnum).getTemper(),carsensorlist.get(carlistnum).getAircon());
            nowcarid = carlist.get(maxnum).getCarid();
            nowCar = carlist.get(maxnum);

        }


    }



    public void clickcarright() {
        int maxnum = carlist.size() - 1;

        if (carlistnum + 1 <= maxnum) {
            carlistnum = carlistnum + 1;
            fragment1.setCarData(carlist.get(carlistnum).getCarname(), carlist.get(carlistnum).getCarmodel(), carlist.get(carlistnum).getCarnum(), carlist.get(carlistnum).getCarimg());
            fragment1.setCarSensorData(carsensorlist.get(carlistnum).getMoving(), carsensorlist.get(carlistnum).getFuel(), carsensorlist.get(carlistnum).getStarting(), carsensorlist.get(carlistnum).getDoor(), carsensorlist.get(carlistnum).getTemper(),carsensorlist.get(carlistnum).getAircon());
            nowcarid = carlist.get(carlistnum).getCarid();
            nowCar = carlist.get(carlistnum);

        } else {
            carlistnum = 0;
            fragment1.setCarData(carlist.get(0).getCarname(), carlist.get(0).getCarmodel(), carlist.get(0).getCarnum(), carlist.get(0).getCarimg());
            fragment1.setCarSensorData(carsensorlist.get(0).getMoving(), carsensorlist.get(0).getFuel(), carsensorlist.get(0).getStarting(), carsensorlist.get(0).getDoor(), carsensorlist.get(0).getTemper(),carsensorlist.get(carlistnum).getAircon());
            nowcarid = carlist.get(0).getCarid();
            nowCar = carlist.get(0);

        }


    }

    // FCM 수신
    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String carid = intent.getStringExtra("carid");
                String contents = intent.getStringExtra("contents");

                Log.d("[FCM]","carid:"+carid+" contents:"+ contents);

                vibrate(300, 5);

                // 상단알람 사용
                manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                NotificationCompat.Builder builder = null;
                if (Build.VERSION.SDK_INT >= 26) {
                    if (manager.getNotificationChannel("ch1") == null) {
                        manager.createNotificationChannel(
                                new NotificationChannel("ch1", "chname", NotificationManager.IMPORTANCE_DEFAULT));
                    }
                    builder = new NotificationCompat.Builder(context, "ch1");
                } else {
                    builder = new NotificationCompat.Builder(context);
                }

                Intent intent1 = new Intent(context, CarActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(
                        context, 101, intent1, PendingIntent.FLAG_UPDATE_CURRENT
                );
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                builder.setAutoCancel(true);
                builder.setContentIntent(pendingIntent);

                String whereFcmCarName = "";
                for(CarVO car: carlist){
                    if(car.getCarid() == Integer.parseInt(carid)){
                        whereFcmCarName = car.getCarname();
                    }
                }

                // FCM 분기
                if(contents.substring(4,8).equals("0004")){
                    if(contents.substring(contents.length()-1,contents.length()).equals("1")){
                        builder.setContentTitle("\'"+whereFcmCarName + "\'" + "에서 " + "영유아가 감지되었습니다");
                    }
                } else if(contents.substring(4,8).equals("0002") || contents.equals("0003")){
                    if(contents.substring(contents.length()-1,contents.length()).equals("1") ||
                            contents.substring(contents.length()-1,contents.length()).equals("3")){
                        builder.setContentTitle("\'"+ whereFcmCarName + "\'" + "에서 " + "충돌이 발생했습니다");
                    }
                }



//                // control이 temper면, data(온도값)을 set해라
//                if (control.equals("temper")) {
//                    builder.setContentText(control + " 이(가)" + data + " ℃로 변경되었습니다.");
//                } // 문 제어
//                else if (control.equals("door")) {
//                    if (data.equals("f")) {
//                        builder.setContentText(control + " 이(가) LOCK 상태로 변경되었습니다.");
//                    } else if (data.equals("o")) {
//                        builder.setContentText(control + " 이(가) UNLOCK 상태로 변경되었습니다.");
//                    }
//
//                } // 시동 제어
//                else if (control.equals("starting")) {
//                    if (data.equals("o")) {
//                        builder.setContentText(control + " 이(가) ON 상태로 변경되었습니다.");
//                    } else if (data.equals("f")) {
//                        builder.setContentText(control + " 이(가) OFF 상태로 변경되었습니다.");
//                    }
//                }


                builder.setSmallIcon(R.mipmap.saftylink1_logo_round);
                Notification noti = builder.build();

                manager.notify(1, noti);

            }
        }
    };

    /*
    HTTP 통신 Code
    */
    class HttpAsyncTask extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(CarActivity.this);
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
                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(CarActivity.this);
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


    // 네이게이션 드로우어 메뉴 선택
    public void onChangedFragment(int position, Bundle bundle) {
        Fragment fragment = null;

        switch (position) {
            case 1:
                getCarData();
                fragment = fragment1;
                toolbar_title.setText("Home");
                break;
            case 2:
                fragment = fragment2;
                toolbar_title.setText("My Page");
                break;
            case 3:
                Log.d("[TEST]", "qqq");
                fragment = fragment3;
                toolbar_title.setText("Map");
                break;
            default:
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }


    // 메뉴 눌렀을 때
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { // 왼쪽 상단 버튼 눌렀을 때
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        String url = "http://" + ip + "/webServer/userlogoutimpl.mc";
        String destroy = "yes";

        url += "?id=" + user.getUserid() + "&destroy=" + destroy;
        httpAsyncTask = new HttpAsyncTask();
        httpAsyncTask.execute(url);
        super.onDestroy();
    }

    // GPS 위치 정보 수신

    /*
     * ActivityCompat.requestPermissions를 사용한 퍼미션 요청의 결과를 리턴받는 메소드입니다.
     */
    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if ( check_result ) {

                //위치 값을 가져올 수 있음
                ;
            }
            else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                    Toast.makeText(CarActivity.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();


                }else {

                    Toast.makeText(CarActivity.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();

                }
            }

        }
    }

    void checkRunTimePermission(){

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(CarActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(CarActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음



        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(CarActivity.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(CarActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(CarActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(CarActivity.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }




    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(CarActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하시겠습니까?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }

                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

}