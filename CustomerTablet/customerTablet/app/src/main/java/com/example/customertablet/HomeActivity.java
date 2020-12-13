package com.example.customertablet;


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
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.icu.text.SimpleDateFormat;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.df.DataFrame;
import com.example.customertablet.network.HttpConnect;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import static com.example.customertablet.MainActivity.ip;

public class HomeActivity extends AppCompatActivity {

    ImageButton imageButton_control, imageButton_map, imageButton_setting, imageButton_tempUp, imageButton_tempDown,
            imageButton_startingOn, imageButton_startingOff, imageButton_doorOn, imageButton_doorOff;
    TextView textView_velocity, textView_oil, textView_heartbeat, textView_maxoil;
    TextView textView_temp, textView_targetTemp, textView_weatherTemp, textView_address, textView_todayDate, textView_weather;
    ImageView imageView_frtire, imageView_fltire, imageView_rrtire, imageView_rltire, imageView_weather, imageView_moving, imageView_heartbeat;

    // TCP/IP Server
    ServerSocket serverSocket;
    Socket socket = null;
    int serverPort = 5558;

    Sender sender;
    HashMap<String, ObjectOutputStream> maps = new HashMap<>();

    // 현재 시간 표시 설정
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date time = new Date();
    String timeNow = format.format(time);

    // HTTP
    DataFrame dataF;
    HttpAsyncTask httpAsyncTask;
    GetStatusAsync getStatusAsync;

    NotificationManager manager; // FCM을 위한 NotificationManager

    // 무게 관련 변수
    int initialLoad; //초기 무게
    ArrayList<Integer> loadDatas = new ArrayList<>(); // 주행중 데이터 저장 ArrayLis
    double avgLoad; // 평균 무게
    private int dialogLoad = 1; // 적재물 낙하 AlertDialog의 flag

    SharedPreferences sp;
    String carnum;
    Handler handler;

//    Timer timer;
//    TimerTask heartbeatTT;

    ValueHandler vhandler;
    HeartbeatThread hthread;
    MovingCar movingcar;
    MoveHandler mhandler;

    int targetTemp;
    TemperTimer temperTimer;

    int startingcode, doorcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // serverStart
        try {
            startServer();
        } catch (Exception e) {
            e.printStackTrace();
        }

        textView_velocity = findViewById(R.id.textView_velocity);
        textView_temp = findViewById(R.id.textView_temp);
        textView_targetTemp = findViewById(R.id.textView_targetTemp);
        textView_weatherTemp = findViewById(R.id.textView_weatherTemp);
        textView_address = findViewById(R.id.textView_address);
        textView_todayDate = findViewById(R.id.textView_todayDate);
        textView_weather = findViewById(R.id.textView_weather);
        textView_heartbeat = findViewById(R.id.textView_heartbeat);
        textView_oil = findViewById(R.id.textView_oil);
        textView_maxoil = findViewById(R.id.textView_maxoil);

        imageButton_control = findViewById(R.id.imageButton_control);
        imageButton_map = findViewById(R.id.imageButton_map);
        imageButton_setting = findViewById(R.id.imageButton_setting);
        imageButton_tempUp = findViewById(R.id.imageButton_tempUp);
        imageButton_tempDown = findViewById(R.id.imageButton_tempDown);
        imageButton_startingOn = findViewById(R.id.imageButton_startingOn);
        imageButton_startingOff = findViewById(R.id.imageButton_startingOff);
        imageButton_doorOn = findViewById(R.id.imageButton_doorOn);
        imageButton_doorOff = findViewById(R.id.imageButton_doorOff);

        imageView_frtire = findViewById(R.id.imageView_frTire);
        imageView_fltire = findViewById(R.id.imageView_flTire);
        imageView_rrtire = findViewById(R.id.imageView_rrTire);
        imageView_rltire = findViewById(R.id.imageView_rlTire);
        imageView_weather = findViewById(R.id.imageView_weather);
        imageView_moving = findViewById(R.id.imageView_moving);
        imageView_heartbeat = findViewById(R.id.imageView_heartbeat);
        //gif 추가
        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(imageView_heartbeat);
        Glide.with(this).load(R.drawable.heartbeat).into(gifImage);

        getSupportActionBar().hide(); // 화면 확보를 위해 ActionBar 제거
//         FCM사용 (앱이 중단되어 있을 때 기본적으로 title,body값으로 푸시!!)
        FirebaseMessaging.getInstance().subscribeToTopic("car"). //구독, 이거랑 토큰으로 원하는 기능 설정하기(파이널 때, db 활용)
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

        handler = new Handler();

        // 여기서 부터는 앱 실행상태에서 상태바 설정!!
        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this); // 브로드캐스트를 받을 준비
        lbm.registerReceiver(receiver, new IntentFilter("notification")); // notification이라는 이름의 정보를 받겠다

        sp = getSharedPreferences("token", MODE_PRIVATE);
        carnum = sp.getString("num", "");
        Log.d("[Server]", "carnum:" + carnum);

        Log.d("[Server]", carnum);

        // 심장박동
        hthread = new HeartbeatThread();
        hthread.start();
        vhandler = new ValueHandler();

        // 주행화면
        movingcar = new MovingCar();
//        movingcar.start();
        mhandler = new MoveHandler();

        // 흑백 사진
//        ColorMatrix matrix = new ColorMatrix();
//        matrix.setSaturation(0);
//        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        //       imageView_moving.setImageResource(R.drawable.stopcar);
        //       imageView_moving.setColorFilter(filter);
//
        // 심장박동
//        timer = new Timer(true);
//        heartbeatTT = new TimerTask() {
//            @Override
//            public void run() {
//
//            }
//        };
//        timer.schedule(heartbeatTT,1000,3000); // 어떤 함수를 1 초 이후에 시작하고 3 초마다 실행한다
//        필요한 부분에 이 함수 넣으면 됨
        temperTimer = new TemperTimer(3000, 1000);

        imageButton_tempUp.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                targetTemp = Integer.parseInt(textView_targetTemp.getText().toString());
                targetTemp = targetTemp + 1;

                if(targetTemp > 30){
                    Toast.makeText(HomeActivity.this,"30도 이하로 설정해주세요!",Toast.LENGTH_SHORT).show();
                }else{
                    textView_targetTemp.setText(String.valueOf(targetTemp));
                    temperTimer.cancel();
                    temperTimer.start();
            }
        }
    });
        imageButton_tempDown.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                targetTemp = Integer.parseInt(textView_targetTemp.getText().toString());
                targetTemp = targetTemp -1;

                if(targetTemp < 18){
                    Toast.makeText(HomeActivity.this,"18도 이상으로 설정해주세요!",Toast.LENGTH_SHORT).show();
                }else{
                    textView_targetTemp.setText(String.valueOf(targetTemp));
                    temperTimer.cancel();
                    temperTimer.start();
                }
            }
        });

        imageButton_startingOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(startingcode == 0){
                    startingcode = 1;
                    setUi("CA00003100000001");
                    getSensor("CA00003100000001");
                    tabletSendDataFrame("CA00003100000001");
                    ColorMatrix matrix = new ColorMatrix();
                    matrix.setSaturation(0);
                    ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                    imageButton_startingOff.setColorFilter(filter);
                    imageButton_startingOn.setColorFilter(null);
                }
            }
        });
        imageButton_startingOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(startingcode == 1){
                    startingcode = 0;
                    setUi("CA00003100000000");
                    getSensor("CA00003100000000");
                    tabletSendDataFrame("CA00003100000000");
                    ColorMatrix matrix = new ColorMatrix();
                    matrix.setSaturation(0);
                    ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                    imageButton_startingOn.setColorFilter(filter);
                    imageButton_startingOff.setColorFilter(null);
                }
            }
        });
        imageButton_doorOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(doorcode == 0){
                    doorcode = 1;
                    setUi("CA00003300000001");
                    getSensor("CA00003300000001");
                    tabletSendDataFrame("CA00003300000001");
                }
            }
        });

        imageButton_doorOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(doorcode == 1){
                    doorcode = 0;
                    setUi("CA00003300000000");
                    getSensor("CA00003300000000");
                    tabletSendDataFrame("CA00003300000000");

                }
            }
        });

        getStatus();
    }// end OnCreate

    class MovingCar extends Thread {
        public void run() {
            final Bundle bundle = new Bundle();
            while (true) {
                for (int i = 1; i < 4; i++) {
                    Message msg = mhandler.obtainMessage();
                    bundle.putInt("move", i);
                    msg.setData(bundle);
                    mhandler.sendMessage(msg);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    class MoveHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            final int move = bundle.getInt("move");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (move == 1) {
                        imageView_moving.setImageResource(R.drawable.redcar3);
                    } else if (move == 2) {
                        imageView_moving.setImageResource(R.drawable.redcar2);
                    } else if (move == 3) {
                        imageView_moving.setImageResource(R.drawable.redcar1);
                    }
                }
            });

        }
    } // 주행 UI end

    /*
    심장박동
    */
    // 스레드 클래스 생성
    class HeartbeatThread extends Thread {
        int value = 100;
        boolean running = false;
        final Bundle bundle = new Bundle();
        public void run() {
            running = true;
            while (running) {
                Random r = new Random();
                int a = r.nextInt(2);
                if (a == 0) {
                    value = value - (r.nextInt(5) + 1);
                } else if (a == 1) {
                    value = value + (r.nextInt(5) + 1);
                }
                Message message = vhandler.obtainMessage();
                bundle.putInt("value", value);
                message.setData(bundle);
                vhandler.sendMessage(message);
                if (value < 50) {
                    final MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.mp);
                    mediaPlayer.start(); //노래 재생
                    value = value + 60;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);
                            alertDialog.setTitle("졸음운전 감지")
                                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mediaPlayer.stop();
                                            textView_heartbeat.setText(value + "");
                                            // 누르기 전에도 thread가 다시 돌면서 ValueHandler로 값을 보내고,
                                            // 높아진 심박수가 보여진다.
                                        }
                                    });
                            alertDialog.create().show();
                        }
                    });

                }
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                }

            }
        }
    }

    class ValueHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            final int value = bundle.getInt("value");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView_heartbeat.setText(value+"");
                }
            });

        }
    } // 심장박동 end

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
            setUi("CA0000210000"+String.valueOf(targetTemp)+"00");
            getSensor("CA0000210000"+String.valueOf(targetTemp)+"00");
            tabletSendDataFrame("CA0000210000"+String.valueOf(targetTemp)+"00");

            Toast t = Toast.makeText(HomeActivity.this,"목표온도가 "+targetTemp+"로 변경됩니다!",Toast.LENGTH_SHORT);
            t.show();
        }
    }

    public void sendfcm(String contents) {
        String urlstr = "http://" + ip + "/webServer/sendfcm.mc";
        String conrtolUrl = urlstr + "?carnum=" + carnum + "&contents=" + contents;

        Log.d("[TEST]", conrtolUrl);

        // AsyncTask를 통해 HttpURLConnection 수행.
        SendFcmAsync sendFcmAsync = new SendFcmAsync();
        sendFcmAsync.execute(conrtolUrl);
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
            //setUi(input.getContents());
        }
    }

    class Receiver extends Thread {
        Socket socket;
        ObjectInputStream oi;


        public Receiver(Socket socket) throws IOException {
            this.socket = socket;
            ObjectOutputStream oo;
            oi = new ObjectInputStream(this.socket.getInputStream());
            oo = new ObjectOutputStream(this.socket.getOutputStream());

            maps.put(socket.getInetAddress().toString(), oo);
            Log.d("[Server]", "[Server]" + socket.getInetAddress() + "연결되었습니다.");
        }


        @Override
        public void run() {
            while (oi != null) {
                try {
                    final DataFrame input = (DataFrame) oi.readObject();
                    Log.d("[Server]", "[DataFrame 수신] " + input.getSender() + ": " + input.getContents());

                    setUi(input.getContents());

                    if (input.getContents().substring(4, 8).equals("0002")
                            || input.getContents().substring(4, 8).equals("0003")
                            || input.getContents().substring(4, 8).equals("0004")) {

                        sendfcm(input.getContents());
                    }

                    // 받은 데이터가 주행 데이터인 경우
                    if (input.getContents().substring(4, 8).equals("0032")) {
                        Log.d("[Run]", "[Run]: " + input.getContents().substring(8));
                        String runData = input.getContents().substring(8);

                        if (runData.equals("00000000")) {
                            // 주행 종료
                            ColorMatrix matrix = new ColorMatrix();
                            matrix.setSaturation(0);
                            ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                            imageView_moving.setImageResource(R.drawable.stopcar);
                            imageView_moving.setColorFilter(filter);
                            movingcar.stop(); // 오류가 날 수 있음
                        } else if (runData.equals("00000001")) {
                            // 주행 시작
                            movingcar.start();
                        }
                    }

                    // 받은 데이터가 무게 데이터인 경우 수행
                    if (input.getContents().substring(4, 8).equals("0005")) {
                        Log.d("[Load]", "[Load]: " + input.getContents().substring(8));
                        String loadData = input.getContents().substring(8);


                        // 주행 시작시 전송하는 무게 데이터
                        if (loadData.substring(0, 1).equals("9")) {
                            initialLoad = Integer.parseInt(loadData.substring(1));
                            Log.d("[Load]", "[Load]: 초기 무게 데이터 " + initialLoad + " 설정되었습니다.");
                            dialogLoad = 1;
                        } else {
                            if (loadDatas.size() <= 5) {
                                if (loadDatas.size() == 5) {
                                    Log.d("[Load]", "[LoadDatas]: " + loadDatas.get(0) + "삭제");
                                    loadDatas.remove(0);
                                    loadDatas.add(Integer.parseInt(loadData));
                                    Log.d("[Load]", "[LoadDatas]: " + loadData + "추가");
                                } else if (loadDatas.size() <= 4) {
                                    loadDatas.add(Integer.parseInt(loadData));
                                    Log.d("[Load]", "[LoadDatas]: " + loadData + "추가");
                                }

                                Log.d("[Load]", "[LoadDatas]: " + loadDatas.toString());

                            }

                            if (loadDatas.size() == 5) {
                                avgLoad = 0;
                                for (int data : loadDatas) {
                                    avgLoad += data;
                                }
                                avgLoad /= 5;
                                Log.d("[Load]", "[avgLoad]: " + avgLoad + " // " + "[initial Load]: " + initialLoad);

                                if (initialLoad > avgLoad + 500) {
                                    Log.d("[Load]", "[Event]: initial Load: " + initialLoad + " // " + "avg Load" + avgLoad);

                                    if (dialogLoad == 1) {
                                        dialogLoad += 1;
                                        _runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(HomeActivity.this);
                                                builder.setTitle("Alert!!");
                                                builder.setMessage("적재물 낙하 사고가 감지되었습니다.");
                                                builder.setPositiveButton("신고", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialogLoad -= 1;
                                                        Toast.makeText(getApplicationContext(), "신고 완료!", Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialogLoad -= 1;
                                                        Toast.makeText(getApplicationContext(), "취소!", Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                                builder.show();
                                            }
                                        });
                                    }

                                }
                            }
                        }
                    }

                    // 받은 DataFrame을 웹서버로 HTTP 전송
                    // call AsynTask to perform network operation on separate thread

                    String url = "http://" + ip + "/webServer/getTabletSensor.mc";
                    url += "?carnum=" + carnum + "&contents=" + input.getContents();
                    httpAsyncTask = new HttpAsyncTask();
                    // Thread 안에서 thread가 돌아갈 땐 Handler을 사용해야 한다
                    Handler mHandler = new Handler(Looper.getMainLooper());
                    final String finalUrl = url;
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            httpAsyncTask.execute(finalUrl);
                        }
                    });
                } catch (Exception e) {
                    maps.remove(socket.getInetAddress().toString());
                    Log.d("[Server]", socket.getInetAddress() + " Exit..." + timeNow);
                    e.printStackTrace();
                    Log.d("[Server]", socket.getInetAddress() + " :Receiver 객체 수신 실패 ");
                    break;
                }
            } // end while
            try {
                if (oi != null) {
                    Log.d("[Server]", "ObjectInputStream Closed ..");
                    oi.close();
                }
                if (socket != null) {
                    Log.d("[Server]", "Socket Closed ..");
                    socket.close();
                }
            } catch (Exception e) {
                Log.d("[Server]", "객체 수신 실패 후 InputStream, socket 닫기 실패");
            }

        }
    }// End Receiver


    class Sender extends Thread {
        DataFrame dataFrame;
        Socket socket;

        public Sender() {

        }

        public void setDataFrame(DataFrame df) {
            this.dataFrame = df;
            Log.d("[Server]", "setDataFrame 완료");
        }

        @Override
        public void run() {
            try {
                Log.d("[Server]", "Sender Thread 실행");
                // dataFrame.setIp("192.168.35.149");
                // dataFrame.setSender("[TabletServer]");
                // Log.d("[Server]", "테스트 목적 Client로 목적지 재설정");
                if (maps.get("/" + dataFrame.getIp()) != null) {
                    maps.get("/" + dataFrame.getIp()).writeObject(dataFrame);
                }
                Log.d("[Server]", "Sender 객체 전송.. " + dataFrame.getIp() + "주소로 " + dataFrame.getContents());
                Log.d("[Server]", "Sender 객체 전송 성공");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    } // End TCP/IP 통신 Code

    public void setUi(final String contents) {

        final String contentsSensor = contents.substring(4, 8);
        final int contentsData = Integer.parseInt(contents.substring(8));

        // contentsData 사용해서 UI 바꾸기!!
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 온도
                if (contentsSensor.equals("0001")) {
                    textView_temp.setText(contentsData/100 +"℃"); // 온도값 ex)15
                }
                // 충돌
                else if (contentsSensor.equals("0002")) {
//                        String.valueOf(contentsData) 충돌여부 ex)1,0
                }
                // 진동
                else if (contentsSensor.equals("0003")) {
                    //String.valueOf(contentsData) 충돌세기 ex)2(small),3(big)
                }
                // pir
                else if (contentsSensor.equals("0004")) {
                    //String.valueOf(contentsData) 영유아감지여부 ex)1,0
                }
                // 무게
                else if (contentsSensor.equals("0005")) {
                    //contentsData 무게 ex)30 // 트럭일 때만 넣어주자
                }
                // 심박수
                else if (contentsSensor.equals("0006")) {
                    textView_heartbeat.setText(contentsData); // 심박수 ex) 80
                }
                // 연료
                else if (contentsSensor.equals("0007")) {
                    textView_oil.setText(contentsData); // 현재연료량 ex) 40
                }
                // 에어컨
                else if (contentsSensor.equals("0021")) {
                    textView_targetTemp.setText(String.valueOf(contentsData/100)); //에어컨목표온도값 ex) 25
                }
                // 시동
                else if (contentsSensor.equals("0031")) {
                    if (String.valueOf(contentsData).equals("1")) { // 시동여부 ex)1,0
                        startingcode = 1;
                        ColorMatrix matrix = new ColorMatrix();
                        matrix.setSaturation(0);
                        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                        imageButton_startingOff.setColorFilter(filter);
                    } else if(String.valueOf(contentsData).equals("0")){
                        startingcode = 0;
                        ColorMatrix matrix = new ColorMatrix();
                        matrix.setSaturation(0);
                        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                        imageButton_startingOn.setColorFilter(filter);
                    }
                }
                // 주행
                else if (contentsSensor.equals("0032")) {
                    if (String.valueOf(contentsData).equals("1")) { // 주행여부 ex)1,0
                            movingcar.start();
                    } else if(String.valueOf(contentsData).equals("0")){
                        ColorMatrix matrix = new ColorMatrix();
                        matrix.setSaturation(0);
                        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                        imageView_moving.setColorFilter(filter);
                    }

                    //time.getTime() 주행시작시간 ex) 시간값형태로 나올듯
                }
                // 문
                else if (contentsSensor.equals("0033")) {
                    if (String.valueOf(contentsData).equals("1")) { // 시동여부 ex)1,0
                        doorcode = 1;
                        imageButton_doorOn.setImageResource(R.drawable.dooropenimgg);
                        imageButton_doorOff.setImageResource(R.drawable.doorcloseimg);
                        imageButton_doorOn.setColorFilter(null);
                    } else if(String.valueOf(contentsData).equals("0")){
                        doorcode = 0;
                        imageButton_doorOff.setImageResource(R.drawable.doorcloseimgg);
                        imageButton_doorOn.setImageResource(R.drawable.dooropenimg);
                        imageButton_doorOff.setColorFilter(null);
                    }
                    //String.valueOf(contentsData) 문  ex)1,0
                }
            }

        });
    }

    private void _runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }

    /*
    HTTP 통신 Code
    */
    class HttpAsyncTask extends AsyncTask<String, String, String> {
        //ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
//            progressDialog = new ProgressDialog(HomeActivity.this);
//            progressDialog.setTitle("Send Data ...");
//            progressDialog.setCancelable(false);
//            progressDialog.show();
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
            //progressDialog.dismiss();
        }// End HTTP 통신 Code
    }

    public void startServer() throws Exception {
        serverSocket = new ServerSocket(serverPort);
        Log.d("[Server]", "Start Server...");

        Runnable r = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Log.d("[Server]", "Server Ready..");
                        socket = serverSocket.accept();
                        Log.d("[Server]", "Connected:" + socket.getInetAddress() + " " + timeNow); // 연결된 IP표시
                        new Receiver(socket).start();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        new Thread(r).start();
    }

    public void sendDataFrame(DataFrame df) {
        try {
            sender = new Sender();
            Log.d("[Server]", "setDataFrame 실행");
            sender.setDataFrame(df);
            Log.d("[Server]", "객체 송신 Thread 호출");
            sender.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 제어한 내용을 센서 측으로 tcp/ip 통신으로 내려보내줌
    public void tabletSendDataFrame(String contents){
        if (contents.length() != 4) {
            DataFrame df = new DataFrame();
            df.setIp(ip);
            df.setSender("Tablet");
            df.setContents(contents);
            sendDataFrame(df);
        }
    }

  /*
       FCM 통신
                    */

        // FCM 수신
        public BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String title = intent.getStringExtra("title");
                    String carid = intent.getStringExtra("carid");
                    String contents = intent.getStringExtra("contents");
                    Toast.makeText(HomeActivity.this, "차량 상태가 변경되었습니다.", Toast.LENGTH_SHORT).show();

                    if (contents.length() != 4) {
                        DataFrame df = new DataFrame();
                        // 연결된 IP로 df를 보낸다
                        df.setIp(socket.getInetAddress().toString().substring(1));
                        df.setSender("Mobile");
                        df.setContents(contents);
                        Log.d("[Server]", df.toString());
                        sendDataFrame(df);
                    }


                    // 모바일에서 제어시 UI변경
                    setUi(contents);


                    Log.d("[Server]", carid);
                    // 상단알람 사용
                    manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    NotificationCompat.Builder builder = null;
                    if (Build.VERSION.SDK_INT >= 26) {
                        if (manager.getNotificationChannel("ch2") == null) {
                            manager.createNotificationChannel(
                                    new NotificationChannel("ch2", "chname", NotificationManager.IMPORTANCE_HIGH));
                        }
                        builder = new NotificationCompat.Builder(context, "ch2");
                    } else {
                        builder = new NotificationCompat.Builder(context);
                    }

                    Intent intent1 = new Intent(context, HomeActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(
                            context, 101, intent1, PendingIntent.FLAG_UPDATE_CURRENT
                    );
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    builder.setAutoCancel(true);
                    builder.setContentIntent(pendingIntent);

                    builder.setContentTitle(title);
                    builder.setContentText(carid + " " + contents);


                    if (carid.equals("verify")) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);
                        alertDialog.setTitle(Integer.parseInt(contents) + "")
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                        alertDialog.create().show();
                        builder.setSmallIcon(R.mipmap.saftylink1_logo_round);
                        Notification noti = builder.build();
                    } else {
                        builder.setSmallIcon(R.mipmap.saftylink1_logo_round);
                        Notification noti = builder.build();
                        manager.notify(1, noti);
                    }

                }
            }
        };

        // 제어한 센서 정보를 DB에 저장
        public void getSensor(String contents){
            String url = "http://" + ip + "/webServer/getTabletSensor.mc";
            url += "?carnum=" + carnum + "&contents=" + contents;
            httpAsyncTask = new HttpAsyncTask();
            // Thread 안에서 thread가 돌아갈 땐 Handler을 사용해야 한다
            Handler mHandler = new Handler(Looper.getMainLooper());
            final String finalUrl = url;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    httpAsyncTask.execute(finalUrl);
                }
            });
        }


        public void getStatus(){
            String url = "http://" + ip + "/webServer/getstatus.mc";
            url += "?carnum=" + carnum;
            getStatusAsync = new GetStatusAsync();
            getStatusAsync.execute(url);
        }

    // 태블릿 켤 때 db에서 정보를 가져와 오류를 방지한다
    class GetStatusAsync extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(HomeActivity.this);
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

                    String starting = jo.getString("starting");
                    if(starting.equals("1")){
                        startingcode = 1;
                        ColorMatrix matrix = new ColorMatrix();
                        matrix.setSaturation(0);
                        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                        imageButton_startingOff.setColorFilter(filter);
                    } else if(starting.equals("0")) {
                        startingcode = 0;
                        ColorMatrix matrix = new ColorMatrix();
                        matrix.setSaturation(0);
                        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                        imageButton_startingOn.setColorFilter(filter);
                    }
                    String door = jo.getString("door");
                    if(door.equals("1")){
                        doorcode = 1;
                        ColorMatrix matrix = new ColorMatrix();
                        matrix.setSaturation(0);
                        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                        imageButton_doorOff.setColorFilter(filter);
                        imageButton_doorOn.setImageResource(R.drawable.dooropenimgg);
                        imageButton_doorOff.setImageResource(R.drawable.doorcloseimg);
                    } else if(door.equals("0")){
                        doorcode = 0;
                        ColorMatrix matrix = new ColorMatrix();
                        matrix.setSaturation(0);
                        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                        imageButton_doorOn.setColorFilter(filter);
                        imageButton_doorOff.setImageResource(R.drawable.doorcloseimgg);
                        imageButton_doorOn.setImageResource(R.drawable.doorcloseimg);
                    }
                    String moving = jo.getString("moving");
                    if(moving.equals("1")){
                        movingcar.start();
                    }else if(moving.equals("0")){ // 잘 작동되는지 확인할 것
                        ColorMatrix matrix = new ColorMatrix();
                        matrix.setSaturation(0);
                        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                        imageView_moving.setImageResource(R.drawable.stopcar);
                        imageView_moving.setColorFilter(filter);
                    }
                    String oil = jo.getString("fuel");
                    textView_oil.setText(oil+" L");
                    String maxoil = jo.getString("fuelmax");
                    textView_maxoil.setText(maxoil+"L");
                    String temper = jo.getString("temper");
                    textView_temp.setText(temper+"℃");
                    String aircon = jo.getString("aircon");
                    textView_targetTemp.setText(aircon);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    // 속도
    public void getVelocity() throws InterruptedException {
//            주행 시작
        Random r = new Random();
        int v = 0;
        while(true){
            if(Integer.parseInt(textView_velocity.getText().toString())<30){
                v = v + r.nextInt(5);
                textView_velocity.setText(v);
                Thread.sleep(100);
            }
        }

    }
    public void tire(){

    }
    public void fuel(){

    }

    }

