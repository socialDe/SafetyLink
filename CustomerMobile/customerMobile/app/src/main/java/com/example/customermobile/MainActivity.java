package com.example.customermobile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.customermobile.df.DataFrame;
import com.example.customermobile.vo.CarSensorVO;
import com.example.customermobile.vo.CarVO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
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
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import www.sanju.motiontoast.MotionToast;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView toolbar_title;

    public CarVO getCar() {
        return car;
    }

    public void setCar(CarVO car) {
        this.car = car;
    }


    Fragment1 fragment1;
    Fragment2 fragment2;
    Fragment3 fragment3;

    FragmentManager fragmentManager;

    FrameLayout container;

    CarVO car;
    CarSensorVO carsensor;

    NotificationManager manager;

    //  네이게이션 드로우어어
    private DrawerLayout mDrawerLayout;
    private Context context = this;

    // TCP/IP 통신
    int port;
    String address;
    String id;
    Socket socket;
    Sender sender;


    // circularProgress구현부분
//    CircularProgressView circularProgressView;
//    ProgressView progressView;
    // circularProgress구현부분 종료


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // tcpip 설정
        port = 5558;
        address = "192.168.0.103";
        id = "MobileJH";

        //new Thread(con).start(); // 풀면 tcpip 사용


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
        getCarSensorData();

    }// end onCreat


    public void getCarData() {
        // URL 설정.
        String carUrl = "http://192.168.0.103/webServer/cardata.mc?carid=1";
        //String carSensorUrl = "http://192.168.0.103/webServer/carsensordata.mc?carid=1";

        // AsyncTask를 통해 HttpURLConnection 수행.
        CarAsync carAsync = new CarAsync();
        carAsync.execute(carUrl);

        //CarSensorAsync carSensorAsync = new CarSensorAsync();
       // carSensorAsync.execute(carSensorUrl);
    }

    public void getCarSensorData() {
        // URL 설정.
        //String carUrl = "http://192.168.0.103/webServer/cardata.mc?carid=1";
        String carSensorUrl = "http://192.168.0.103/webServer/carsensordata.mc?carid=1";

        // AsyncTask를 통해 HttpURLConnection 수행.
       // CarAsync carAsync = new CarAsync();
        //carAsync.execute(carUrl);

        CarSensorAsync carSensorAsync = new CarSensorAsync();
        carSensorAsync.execute(carSensorUrl);
    }

    public void vibrate(int sec,int power){
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
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Get Data ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
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
            progressDialog.dismiss();
            JSONArray ja = null;
            try {
                Log.d("[TAG]","0");
                ja = new JSONArray(s);
                Log.d("[TAG]","00");
                for(int i=0; i<ja.length(); i++){
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

                    car = new CarVO(carid,userid,carnum,carname,cartype,carmodel,caryear,carimg,caroiltype,tablettoken);

                    fragment1.setCarData(car.getCarname(),car.getCarmodel(),car.getCarnum());

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }


    class CarSensorAsync extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Get Data ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
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
            progressDialog.dismiss();
            JSONArray ja = null;
            try {
                Log.d("[TAG]","s:"+s);
                ja = new JSONArray(s);
                Log.d("[TAG]","1");
                for(int i=0; i<ja.length(); i++){
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


                    carsensor = new CarSensorVO(carid,heartbeat,pirfront,pirrear,freight,fuel,fuelmax,temper,starting,moving,movingstarttime,aircon,crash,door,lat,lng);

                    fragment1.setCarSensorData(carsensor.getFuel(),carsensor.getStarting(),carsensor.getDoor(),carsensor.getTemper());

                    Log.d("[TAG]","TEST:"+carsensor.getFuel()+" "+carsensor.getStarting()+" "+carsensor.getDoor()+" "+carsensor.getTemper());
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }

    }



    Runnable con = new Runnable() {
        @Override
        public void run() {
            try {
                connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };


    private void connect() throws IOException {
        // 소켓이 만들어지는 구간
        try {
            socket = new Socket(address, port);
        } catch (Exception e) {
            while (true) {
                try {
                    Thread.sleep(2000);
                    socket = new Socket(address, port);
                    break;
                } catch (Exception e1) {
                    System.out.println("Retry...");
                }
            }
        }

        System.out.println("Connected Server:" + address);

        sender = new Sender(socket);
        new Receiver(socket).start();
        //sendMsg();


    }




    // 뒤로가기 눌렀을 때 q를 보내 tcp/ip 통신 종료
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            DataFrame df = new DataFrame(null, id, "q");
            sender.setDf(df);
            new Thread(sender).start();
            if (socket != null) {
                socket.close();
            }
            finish();
            onDestroy();

        } catch (Exception e) {

        }
    }

    // FCM 수신
    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String title = intent.getStringExtra("title");
                String control = intent.getStringExtra("control");
                String data = intent.getStringExtra("data");

//                if (control.equals("temper")) { // control이 temper면, data(온도값)을 set해라
//                    if (Integer.parseInt(data) > 30) {
//                        Toast.makeText(MainActivity.this,
//                                "30도 이하의 온도로 설정해주세요.", Toast.LENGTH_LONG).show();
//                    } else if (Integer.parseInt(data) < 18) {
//                        Toast.makeText(MainActivity.this,
//                                "18도 이상의 온도로 설정해 주세요.", Toast.LENGTH_LONG).show();
//                    } else if (data.equals(textView_temper.getText())) {
//
//                    } else {
//                        textView_targetTemper.setText(data);
//                        Toast.makeText(MainActivity.this,
//                                "희망 온도가 " + data + "℃로 변경되었습니다." + "\n", Toast.LENGTH_LONG).show();
//                    }
//                    // 반대 핸드폰에서 희망 온도가 바뀌지 않는 경우에도 FCM이 가는걸 막으려면 if문을 밖으로 빼준다.
//                } else if (control.equals("door")) { // 문 제어
//                    if (data.equals("f")) {
//                        imageButton_doorOff.setImageResource(R.drawable.doorcloseimgg);
//
//                    } else if (data.equals("o")) {
//                        imageButton_doorOn.setImageResource(R.drawable.dooropenimgg);
//                    }
//
//                } else if (control.equals("starting")) { // 시동 제어
//                    if (data.equals("o")) {
//                        imageButton_startingOn.setImageResource(R.drawable.startingon);
//                    } else if (data.equals("f")) {
//                        imageButton_startingOff.setImageResource(R.drawable.startingoff);
//                    }
//                } // 추가로 제어할 것이 있으면 이곳에 else if 추가

                vibrate(500,5);

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

                Intent intent1 = new Intent(context, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(
                        context, 101, intent1, PendingIntent.FLAG_UPDATE_CURRENT
                );
                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                builder.setAutoCancel(true);
                builder.setContentIntent(pendingIntent);

                builder.setContentTitle(title);


                // control이 temper면, data(온도값)을 set해라
                if (control.equals("temper")) {
                    builder.setContentText(control + " 이(가)" + data + " ℃로 변경되었습니다.");
                } // 문 제어
                else if (control.equals("door")) {
                    if (data.equals("f")) {
                        builder.setContentText(control + " 이(가) LOCK 상태로 변경되었습니다.");
                    } else if (data.equals("o")) {
                        builder.setContentText(control + " 이(가) UNLOCK 상태로 변경되었습니다.");
                    }

                } // 시동 제어
                else if (control.equals("starting")) {
                    if (data.equals("o")) {
                        builder.setContentText(control + " 이(가) ON 상태로 변경되었습니다.");
                    } else if (data.equals("f")) {
                        builder.setContentText(control + " 이(가) OFF 상태로 변경되었습니다.");
                    }
                }


                builder.setSmallIcon(R.mipmap.saftylink1_logo_round);
                Notification noti = builder.build();
                //manager.notify(1, noti); // 상단 알림을 없애려면 이곳 주석 처리
            }
        }
    };

    class Receiver extends Thread {
        ObjectInputStream oi;

        public Receiver(Socket socket) throws IOException {
            oi = new ObjectInputStream(socket.getInputStream());
        }

        @Override
        public void run() {
            // 수신 inputStream이 비어 있지 않은 경우 실행!
            while (oi != null) {
                DataFrame df = null;
                // 수신 시도
                try {
                    System.out.println("[Client Receiver Thread] 수신 대기");
                    df = (DataFrame) oi.readObject();
                    System.out.println("[Client Receiver Thread] 수신 완료"); // 11/19에 이 부분에 setText 추가하기
                    System.out.println(df.getSender() + ": " + df.getContents());
                } catch (Exception e) {
                    System.out.println("[Client Receiver Thread] 수신 실패");
                    e.printStackTrace();
                    break;
                }


            } // end while
            try {
                if (oi != null) {
                    oi.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (Exception e) {

            }
            // 서버가 끊기면 connect를 한다!
            try {
                Thread.sleep(2000);
                System.out.println("test2");
                connect();
                //sendMsg();
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        }

    }


    class Sender implements Runnable {
        Socket socket;
        ObjectOutputStream outstream;
        DataFrame df;

        public Sender(Socket socket) throws IOException {
            this.socket = socket;
            outstream = new ObjectOutputStream(socket.getOutputStream());
        }

        public void setDf(DataFrame df) {
            this.df = df;
        }

        @Override
        public void run() {
            //전송 outputStream이 비어 있지 않은 경우 실행!
            if (outstream != null) {
                // 전송 시도
                try {
                    System.out.println("[Client Sender Thread] 데이터 전송 시도: " + df.getIp() + "으로 " + df.getContents() + " 전송");
                    outstream.writeObject(df);
                    Log.d("[test]", df.toString());
                    outstream.flush();
                    System.out.println("[Client Sender Thread] 데이터 전송 시도: " + df.getIp() + "으로 " + df.getContents() + " 전송 완료");
                } catch (IOException e) {
                    System.out.println("[Client Sender Thread] 전송 실패");
                    // 전송 실패시 소켓이 열려 있다면 소켓 닫아버리고 다시 서버와 연결을 시도
                    try {
                        if (socket != null) {
                            System.out.println("[Client Sender Thread] 전송 실패, 소켓 닫음");
                            socket.close();
                        }
                        // 소켓을 닫을 수 없음
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    // 다시 서버와 연결 시도
                    try {
                        Thread.sleep(2000);
                        connect();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }
    }


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


//    public void clickImageButton_carLeft(View view){
//
//    }
//
//    public void clickImageButton_carRight(View view){
//
//    }
//
//    public void clickImageButton_startingOn(View view){
//
//    }
//
//    public void clickImageButton_startingOff(View view){
//
//    }
//
//    public void clickImageButton_doorOn(View view){
//
//    }
//
//    public void clickImageButton_doorOff(View view){
//
//    }
//
//    public void clickImageButton_downTemper(View view){
//        //Log.d("[TAG]",textView_targetTemper.getText().toString());
//        int targetTemper = Integer.parseInt(textView_targetTemper.getText().toString());
//        textView_targetTemper.setText(String.valueOf(targetTemper-1));
//    }
//    public void clickImageButton_upTemper(View view){
//        int targetTemper = Integer.parseInt(textView_targetTemper.getText().toString());
//        textView_targetTemper.setText(targetTemper+1);
//    }




    //circularProgress구현부분
//        circularProgressView = findViewById(R.id.circularProgressView);
//        circularProgressView.setProgress(0);
//        circularProgressView.setMaxValue(100);
//
//        progressView = findViewById(R.id.progressView);
//        progressView.setOnProgressChangeListener(new OnProgressChangeListener() {
//            @Override
//            public void onChange(float v) {
//                progressView.setLabelText("Progress: " + v + "%");
//            }
//        });
    // circularProgress구현부분 종료


//    public void clickbt(View v) {
//
    //circularProgress구현부분
//        if(v.getId() == R.id.button1){
//            circularProgressView.setProgress(circularProgressView.getProgress() + 5);
//        }else if(v.getId() == R.id.button2){
//            MotionToast.Companion.createToast(this,
//                    "Hurray success 😍",
//                    "Upload Completed successfully!",
//                    MotionToast.TOAST_SUCCESS,
//                    MotionToast.GRAVITY_BOTTOM,
//                    MotionToast.LONG_DURATION,
//                    ResourcesCompat.getFont(this,R.font.helvetica_regular));
//        }else if(v.getId() == R.id.button3){
//            Random r = new Random();
//            progressView.setProgress(r.nextInt(100));
//        }
    // circularProgress구현부분 종료
//
//    }


}