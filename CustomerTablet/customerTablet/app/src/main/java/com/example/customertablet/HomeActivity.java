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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.example.customertablet.Activity.MapActivity;
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
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import static com.example.customertablet.MainActivity.ip;

public class HomeActivity extends AppCompatActivity {

    ImageButton imageButton_control, imageButton_map, imageButton_setting, imageButton_tempUp, imageButton_tempDown,
            imageButton_startingOn, imageButton_startingOff, imageButton_doorOn, imageButton_doorOff;
    TextView textView_velocity, textView_oil, textView_heartbeat, textView_maxoil, textView_time;
    TextView textView_temp, textView_targetTemp, textView_weatherTemp, textView_address, textView_todayDate, textView_weather;
    ImageView imageView_frtire, imageView_fltire, imageView_rrtire, imageView_rltire, imageView_weather, imageView_moving, imageView_heartbeat, imageView_velocity;

    // TCP/IP Server
    ServerSocket serverSocket;
    Socket socket = null;
    int serverPort = 5558;

    Sender sender;
    HashMap<String, ObjectOutputStream> maps = new HashMap<>();

    // 현재 시간 표시 설정
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat formatDate = new SimpleDateFormat("yyyy년 MM월 dd일");
    SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");

    Date time = new Date();
    String timeNow = format.format(time);
    String timeNowDate = formatDate.format(time);
    String timeNowTime;


    // HTTP
    DataFrame dataF;
    HttpAsyncTask httpAsyncTask, httpAsyncTask2;
    GetStatusAsync getStatusAsync;
    GetPushCheckAsync getPushCheckAsync;

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
    VelocityHandler velocityhandler;
    DownVelocityHandler downvelocityhandler;
    TireHandler tirehandler;

    int targetTemp;
    TemperTimer temperTimer;

    int startingcode, doorcode;

    // moving, tire thread
    MoveStart moveStart;
    MoveStop moveStop;
    Tire tire;

    String accpushcheck = "o";
    String droppushcheck = "o";
    String sleeppushcheck = "o";

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
        textView_time = findViewById(R.id.textView_time);

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
        imageView_velocity = findViewById(R.id.imageView_velocity);
        //gif 추가
        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(imageView_heartbeat);
        Glide.with(this).load(R.drawable.heartbeat).into(gifImage);

//        GlideDrawableImageViewTarget gifVelocity = new GlideDrawableImageViewTarget(imageView_velocity);
//        Glide.with(this).load(R.drawable.velocity).into(gifVelocity);

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

        imageButton_control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CarInfoActivity.class);
                startActivity(intent);
            }
        });
        // db에서 상태 가져옴
        getStatus();

        imageButton_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                startActivity(intent);
            }
        });

        // 심장박동
        hthread = new HeartbeatThread();
//        hthread.start();
        vhandler = new ValueHandler();

        // 주행화면
        movingcar = new MovingCar();
//        movingcar.start();
        mhandler = new MoveHandler();

        moveStart = new MoveStart();
        velocityhandler = new VelocityHandler();
        moveStop = new MoveStop();
        downvelocityhandler = new DownVelocityHandler();

        tire = new Tire();
        tirehandler = new TireHandler();


        // 일정 시간마다 작동
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
//        온도 올리는 버튼 클릭 이벤트
        imageButton_tempUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                targetTemp = Integer.parseInt(textView_targetTemp.getText().toString());
                targetTemp = targetTemp + 1;

                if (targetTemp > 30) {
                    Toast.makeText(HomeActivity.this, "30도 이하로 설정해주세요!", Toast.LENGTH_SHORT).show();
                } else {
                    textView_targetTemp.setText(String.valueOf(targetTemp));
                    temperTimer.cancel();
                    temperTimer.start();
                }
            }
        });

//        온도 내리는 버튼 클릭 이벤트
        imageButton_tempDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                targetTemp = Integer.parseInt(textView_targetTemp.getText().toString());
                targetTemp = targetTemp - 1;

                if (targetTemp < 18) {
                    Toast.makeText(HomeActivity.this, "18도 이상으로 설정해주세요!", Toast.LENGTH_SHORT).show();
                } else {
                    textView_targetTemp.setText(String.valueOf(targetTemp));
                    temperTimer.cancel();
                    temperTimer.start();
                }
            }
        });
//        전원 ON 버튼 클릭 이벤트
        imageButton_startingOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startingcode == 0) {
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

//        전원 OFF 버튼 클릭 이벤트
        imageButton_startingOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startingcode == 1) {
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
        //        문 ON 버튼 클릭 이벤트
        imageButton_doorOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doorcode == 0) {
                    doorcode = 1;
                    setUi("CA00003300000001");
                    getSensor("CA00003300000001");
                    tabletSendDataFrame("CA00003300000001");
                }
            }
        });
//        문 OFF 버튼 클릭 이벤트
        imageButton_doorOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doorcode == 1) {
                    doorcode = 0;
                    setUi("CA00003300000000");
                    getSensor("CA00003300000000");
                    tabletSendDataFrame("CA00003300000000");

                }
            }
        });

        Thread t1 = new Thread(new PushCheckThread());
        t1.start();

        Log.d("[TAG]", "acc:" + accpushcheck + " drop:" + droppushcheck + " sleep:" + sleeppushcheck);

        textView_todayDate.setText(timeNowDate);
        textView_time.setText(timeNowTime);
        new Thread(timeThread).start();
    }// end OnCreate

    class MovingCar extends Thread {
        boolean moving = true;
        public void run() {
            final Bundle bundle = new Bundle();
            while (moving) {
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
        int value = 90;
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
                    value = value - (r.nextInt(5) + 1);
                }
                Message message = vhandler.obtainMessage();
                bundle.putInt("value", value);
                message.setData(bundle);
                vhandler.sendMessage(message);
                if (value < 50) {
                    value = value + 60;
                    // sleeppush가 "on"일 때만 알람을 받는다
                    if (sleeppushcheck.equals("o")) {
                        final MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.mp);
                        mediaPlayer.start(); //노래 재생
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
                                        }).setCancelable(false);
                                alertDialog.create().show();
                            }
                        });

                    }
                } else if (value > 150) {
                    value = value - 10;
                }

                try {
                    Thread.sleep(500);
                } catch (Exception e) {
                }

            } // while end
            Message message = vhandler.obtainMessage();
            bundle.putInt("value", 0);
            message.setData(bundle);
            vhandler.sendMessage(message);
            // Thread가 끝날 때 다시 0으로 만들어줌
        }
    }

    class ValueHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            final int value = bundle.getInt("value");
            final int end = bundle.getInt("end");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView_heartbeat.setText(value+"");
                    if(end == 1){
                        textView_heartbeat.setText("0");
                    }
                }
            });

        }
    } // 심장박동 end

    // 온도설정을 위한 타이머
    class TemperTimer extends CountDownTimer {
        public TemperTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            setUi("CA0000210000" + String.valueOf(targetTemp) + "00");
            getSensor("CA0000210000" + String.valueOf(targetTemp) + "00");
            tabletSendDataFrame("CA0000210000" + String.valueOf(targetTemp) + "00");

            Toast t = Toast.makeText(HomeActivity.this, "목표온도가 " + targetTemp + "로 변경됩니다!", Toast.LENGTH_SHORT);
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


                    if (input.getContents().substring(4, 8).equals("0004")) {

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
                            moveStart.whilemove = false;
                            moveStart.join();
                            moveStop = new MoveStop();
                            moveStop.start();

                        } else if (runData.equals("00000001")) {
                            // 주행 시작
                            imageView_moving.setColorFilter(null);
                            moveStop.whilestop = false;
                            moveStop.join();
                            moveStart = new MoveStart();
                            moveStart.start();
                        }
                    }


                    // 받은 데이터가 진동 데이터
                    if (input.getContents().substring(4, 8).equals("0003")) {
                        String strvibr = input.getContents().substring(8);
                        int vibrData = Integer.parseInt(strvibr);
                        Log.d("[acc]", "vibrData" + vibrData);
                        // 충돌 푸쉬설정이 "on" 일때만 알람창을 띄운다
                        if (accpushcheck.equals("o")) {
                            if (vibrData > 30) {
                                // 강한 충돌 사고
//                            SmsManager smsManager = SmsManager.getDefault();
//                            smsManager.sendTextMessage("tel:010-9316-3163", null, "충돌 사고 발생", null, null);
                                _runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(HomeActivity.this, "119에 신고 되었습니다!", Toast.LENGTH_LONG).show();
                                    }
                                });
                                input.setContents(input.getContents().substring(0, 8) + "00000003");
                            } else {
                                // 약한 충돌 사고
                                String url = "http://" + ip + "/webServer/getMovingcar.mc";
                                url += "?carnum=" + carnum;
                                httpAsyncTask = new HttpAsyncTask();
                                httpAsyncTask.execute(url);
                                input.setContents(input.getContents().substring(0, 8) + "00000002");
                            }
                        }
                    }


                    // 낙하물 푸쉬설정이 "on" 일때만 알람창을 띄운다
                    if (droppushcheck.equals("o")) {

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

                                                            Toast.makeText(getApplicationContext(), "신고 완료!", Toast.LENGTH_LONG).show();
                                                        }
                                                    });
                                                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {

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

                    }


                    // 받은 DataFrame을 웹서버로 HTTP 전송
                    // call AsynTask to perform network operation on separate thread
                    String url = "http://" + ip + "/webServer/getTabletSensor.mc";
                    url += "?carnum=" + carnum + "&contents=" + input.getContents();
                    // Thread 안에서 thread가 돌아갈 땐 Handler을 사용해야 한다
                    Handler mHandler = new Handler(Looper.getMainLooper());
                    final String finalUrl = url;
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            httpAsyncTask2 = new HttpAsyncTask();
                            httpAsyncTask2.execute(finalUrl);
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
                    textView_temp.setText(contentsData / 100 + "℃"); // 온도값 ex)15
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
                    textView_targetTemp.setText(String.valueOf(contentsData / 100)); //에어컨목표온도값 ex) 25
                }
                // 시동
                else if (contentsSensor.equals("0031")) {
                    if (String.valueOf(contentsData).equals("1")) { // 시동여부 ex)1,0
                        startingcode = 1;
                        ColorMatrix matrix = new ColorMatrix();
                        matrix.setSaturation(0);
                        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                        imageButton_startingOff.setColorFilter(filter);
                        imageButton_startingOn.clearColorFilter();
                    } else if (String.valueOf(contentsData).equals("0")) {
                        startingcode = 0;
                        ColorMatrix matrix = new ColorMatrix();
                        matrix.setSaturation(0);
                        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                        imageButton_startingOn.setColorFilter(filter);
                        imageButton_startingOff.clearColorFilter();
                    }
                }
                // 주행
                else if (contentsSensor.equals("0032")) {
                    if (String.valueOf(contentsData).equals("1")) { // 주행여부 ex)1,0
//                        movingcar = new MovingCar();
//                        movingcar.start();
//                        imageView_moving.setColorFilter(null);
//                        moveStart = new MoveStart();
//                        moveStart.start();
//                        moveStop.whilestop = false;
//
                    } else if (String.valueOf(contentsData).equals("0")) {
//                        imageView_moving.setImageResource(R.drawable.stopcar);
//                        ColorMatrix matrix = new ColorMatrix();
//                        matrix.setSaturation(0);
//                        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
//                        imageView_moving.setColorFilter(filter);
//                        movingcar.moving = false;
//                        moveStop = new MoveStop();
//                        moveStop.start();
//                        moveStart.whilemove = false;
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
                    } else if (String.valueOf(contentsData).equals("0")) {
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


        @Override
        protected void onPreExecute() {

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
            final String result = s.trim();
            Log.d("[TAG]", "result:" + result);
            if (result.equals("crush")) {
                AlertDialog.Builder dailog = new AlertDialog.Builder(HomeActivity.this);
                dailog.setTitle("충돌 사고가 발생하였습니다");
                dailog.setMessage("119에 신고하시겠습니까?");
                dailog.setPositiveButton("신고", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                      SmsManager smsManager = SmsManager.getDefault();
//                      smsManager.sendTextMessage("tel:010-9316-3163", null, "충돌 사고 발생", null, null);
                        Toast.makeText(getApplicationContext(), "사고가 신고되었습니다", Toast.LENGTH_SHORT).show();
                        return;
                    }
                });
                dailog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                });
                dailog.show();
            }

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
    public void tabletSendDataFrame(String contents) {
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
                    // 모바일에서 제어시 UI변경
                    setUi(contents);

                    DataFrame df = new DataFrame();
                    // 연결된 IP로 df를 보낸다
                    try {
                        df.setIp(socket.getInetAddress().toString().substring(1));
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "차량이 연결되지 않아 제어데이터를 보내지 못했습니다.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    df.setSender("Mobile");
                    df.setContents(contents);
                    Log.d("[Server]", df.toString());
                    sendDataFrame(df);
                }


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
    public void getSensor(String contents) {
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

    public void getSensors(String contents, String fuel) {
        String url = "http://" + ip + "/webServer/getTabletSensor.mc";
        url += "?carnum=" + carnum + "&contents=" + contents + "&fuel=" + fuel;
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

    public void getStatus() {
        String url = "http://" + ip + "/webServer/getstatus.mc";
        url += "?carnum=" + carnum;
        getStatusAsync = new GetStatusAsync();
        getStatusAsync.execute(url);
    }

    public void getPushCheck() {
        String url = "http://" + ip + "/webServer/getpush.mc";
        url += "?carnum=" + carnum;
        getPushCheckAsync = new GetPushCheckAsync();
        getPushCheckAsync.execute(url);
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
                    if (starting.equals("1")) {
                        startingcode = 1;
                        ColorMatrix matrix = new ColorMatrix();
                        matrix.setSaturation(0);
                        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                        imageButton_startingOff.setColorFilter(filter);
                    } else if (starting.equals("0")) {
                        startingcode = 0;
                        ColorMatrix matrix = new ColorMatrix();
                        matrix.setSaturation(0);
                        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                        imageButton_startingOn.setColorFilter(filter);
                    }
                    String door = jo.getString("door");
                    if (door.equals("1")) {
                        doorcode = 1;
                        ColorMatrix matrix = new ColorMatrix();
                        matrix.setSaturation(0);
                        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                        imageButton_doorOff.setColorFilter(filter);
                        imageButton_doorOn.setImageResource(R.drawable.dooropenimgg);
                        imageButton_doorOff.setImageResource(R.drawable.doorcloseimg);
                    } else if (door.equals("0")) {
                        doorcode = 0;
                        ColorMatrix matrix = new ColorMatrix();
                        matrix.setSaturation(0);
                        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                        imageButton_doorOn.setColorFilter(filter);
                        imageButton_doorOff.setImageResource(R.drawable.doorcloseimgg);
                        imageButton_doorOn.setImageResource(R.drawable.dooropenimg);
                    }
                    String moving = jo.getString("moving");
                    if (moving.equals("1")) {
                        moveStop.whilestop = false;
                        moveStop.join();
                        moveStart = new MoveStart();
                        moveStart.start();

                    } else if (moving.equals("0")) { // 잘 작동되는지 확인할 것
                        imageView_moving.setImageResource(R.drawable.stopcar);
                        ColorMatrix matrix = new ColorMatrix();
                        matrix.setSaturation(0);
                        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                        imageView_moving.setColorFilter(filter);
                        moveStart = new MoveStart();
                        moveStart.join();
                        moveStop = new MoveStop();
                        moveStop.start();
                    }
                    double oil = jo.getDouble("fuel");
                    oil = oil / 100;
                    textView_oil.setText(oil + "");
                    String maxoil = jo.getString("fuelmax");
                    textView_maxoil.setText(maxoil + "L");
                    String temper = jo.getString("temper");
                    textView_temp.setText(temper + "℃");
                    String aircon = jo.getString("aircon");
                    textView_targetTemp.setText(aircon);

                    tire.start(); // tire 공기압 받아옴
                }
            } catch (JSONException | InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    class MoveStart extends Thread {
        int v = Integer.parseInt(textView_velocity.getText().toString());
        double fuel = Double.parseDouble(textView_oil.getText().toString());
        final Bundle bundle = new Bundle();
        boolean whilemove = true;

        @Override
        public void run() {
            movingcar.moving = false;
            hthread.running = false;
            try {
                movingcar.join();
                hthread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            movingcar = new MovingCar();
            movingcar.start();
            hthread = new HeartbeatThread();
            hthread.start();

            Random r = new Random();
            while (whilemove) {
                v = v + r.nextInt(5);
                Message msg = velocityhandler.obtainMessage();
                bundle.putInt("velocity", v);
                System.out.println(bundle);
                msg.setData(bundle);
                velocityhandler.sendMessage(msg);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (v > 30) {
                    if (fuel >= 0) {
                        msg = velocityhandler.obtainMessage();
                        fuel = fuel - 0.1;
                        bundle.putDouble("fuel", fuel);
                        msg.setData(bundle);
                        doorcode = 0;
                        setUi("CA00003300000000"); // Tablet UI에 속도 30 넘을 때 문 닫기 설정
                        if (fuel >= 10 && fuel < 100) {
                            getSensors("CA00003300000000", "CA0000070000"+String.valueOf(Math.round(fuel*10))+"0"); // DB에 저장
                        } else if (fuel < 10) {
                            getSensors("CA00003300000000", "CA000007000"+String.valueOf(Math.round(fuel*10))+"0"); // DB에 저장
                        }

                        tabletSendDataFrame("CA00003300000000"); // TCP/IP로 전송

                    }
                    break;
                }
            }
            while (whilemove) {
                v = v + r.nextInt(7);
                Message msg = velocityhandler.obtainMessage();
                bundle.putInt("velocity", v);
                msg.setData(bundle);
                velocityhandler.sendMessage(msg);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (v > 80) {
                    if (fuel >= 0) {
                        fuel = fuel - 0.2;
                        bundle.putDouble("fuel", fuel);
                        msg.setData(bundle);
                        getSensor("CA0000070000"+String.valueOf(Math.round(fuel*10))+"0");

                    }
                    msg.setData(bundle);
                    break;
                }
            }
            while (whilemove) {
                v = v + r.nextInt(8);
                Message msg = velocityhandler.obtainMessage();
                bundle.putInt("velocity", v);
                msg.setData(bundle);
                velocityhandler.sendMessage(msg);
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (v > 120) {
                    fuel = fuel - 0.3;
                    v = v - 10;
                    msg = velocityhandler.obtainMessage();
                    bundle.putInt("velocity", v);
                    msg.setData(bundle);
                    if (fuel >= 0.3) {
                        bundle.putDouble("fuel", fuel);
                        msg.setData(bundle);
                    } else if (fuel <= 0.2) {
                        bundle.putDouble("fuel", 0.01);
                        msg.setData(bundle);
                        velocityhandler.sendMessage(msg);
                        whilemove = false;
                        try {
                            moveStart.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        moveStop = new MoveStop();
                        moveStop.start();
                        // moving으로 바꿀 때, imageView도 바꿔주자
                        break;
                    }
                    getSensor("CA0000070000"+String.valueOf(Math.round(fuel*10))+"0");
                    velocityhandler.sendMessage(msg);
                } else if (v < 80) { // 이 부분은 80~120으로 왔다갔다 할까 고민하느라 넣어둠
                    fuel = fuel - 0.3;
                    v = v + 10;
                    msg = velocityhandler.obtainMessage();
                    bundle.putInt("velocity", v);
                    msg.setData(bundle);
                    if (fuel >= 0) {
                        bundle.putDouble("fuel", fuel);
                        msg.setData(bundle);
                    } else if (fuel <= 0.2) {
                        bundle.putDouble("fuel", 0.01);
                        msg.setData(bundle);
                        velocityhandler.sendMessage(msg);
                        whilemove = false;
                        try {
                            moveStart.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        moveStop = new MoveStop();
                        moveStop.start();
                        // moving으로 바꿀 때, imageView도 바꿔주자
                        break;
                    }
                    getSensor("CA0000070000"+String.valueOf(Math.round(fuel*10))+"0");
                    velocityhandler.sendMessage(msg);
                }
            }
        }
    }

    class VelocityHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            final int v = bundle.getInt("velocity");
            double fuel = bundle.getDouble("fuel");
            if (fuel == 0) {
                fuel = Double.parseDouble(textView_oil.getText().toString());
            } else if (fuel == 0.01) {

            }
            final String num = String.format("%.1f", fuel);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    imageView_moving.setColorFilter(null);
                    textView_velocity.setText(v + "");
                    textView_oil.setText(num + "");
                }
            });
        }
    } // 속도 올라가는 것

    // 속도 멈출 때 천천히 내려가도록
    class MoveStop extends Thread {
        int v = Integer.parseInt(textView_velocity.getText().toString());
        final Bundle bundle = new Bundle();
        boolean whilestop = true;

        @Override
        public void run() {
            Random r = new Random();
            while (whilestop) {
                v = v - r.nextInt(8);
                Message msg = downvelocityhandler.obtainMessage();
                bundle.putInt("velocity", v);
                msg.setData(bundle);
                downvelocityhandler.sendMessage(msg);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (v < 80) {
                    break;
                }

            }
            while (whilestop) {
                v = v - r.nextInt(7);
                Message msg = downvelocityhandler.obtainMessage();
                bundle.putInt("velocity", v);
                msg.setData(bundle);
                downvelocityhandler.sendMessage(msg);
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (v < 30) {
                    break;
                }
            }
            while(whilestop) {
                v = v - r.nextInt(4);
                Message msg = downvelocityhandler.obtainMessage();
                Log.d("[Server]", v+"");
                bundle.putInt("velocity", v);
                msg.setData(bundle);
                downvelocityhandler.sendMessage(msg);
                if (v <= 0) {
                    v = 0;
                    Message message = vhandler.obtainMessage();
                    bundle.putInt("end",1);
                    message.setData(bundle);
                    vhandler.sendMessage(message); // 속도가 0이 되면 심박수 0으로 만들어줌

                    movingcar.moving = false;
                    hthread.running = false;
                    try {
                        movingcar.join();
                        hthread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Message msg1 = downvelocityhandler.obtainMessage();
                    bundle.putInt("velocity", v);
                    msg1.setData(bundle);
                    downvelocityhandler.sendMessage(msg1);

                    getSensor("CA00003200000000");
                    tabletSendDataFrame("CA00003200000000");
                    break;
                }


            }
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    class DownVelocityHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            final int v = bundle.getInt("velocity");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(v <= 0){
                        ColorMatrix matrix = new ColorMatrix();
                        matrix.setSaturation(0);
                        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                        imageView_moving.setColorFilter(filter);
                        imageView_moving.setImageResource(R.drawable.stopcar);
                    }
                    textView_velocity.setText(v+"");
                }
            });

        }
    } // 속도 내려가는 것

    // 타이어 공기압
    class Tire extends Thread {
        Random r = new Random();
        int a = r.nextInt(100) + 1;
        int b = r.nextInt(100) + 1;
        int c = r.nextInt(100) + 1;
        int d = r.nextInt(100) + 1;

        @Override
        public void run() {
            final Bundle bundle = new Bundle();
            Message msg = tirehandler.obtainMessage();
            if (a <= 5) {
                bundle.putInt("fltire", 1);
                msg.setData(bundle);
            } else if (a <= 30 && a > 5) {
                bundle.putInt("fltire", 2);
                msg.setData(bundle);
            } else if (a <= 100) {
                bundle.putInt("fltire", 3);
                msg.setData(bundle);
            }
            if (b <= 5) {
                bundle.putInt("frtire", 1);
                msg.setData(bundle);
            } else if (b <= 30 && b > 5) {
                bundle.putInt("frtire", 2);
                msg.setData(bundle);
            } else if (b <= 100) {
                bundle.putInt("frtire", 3);
                msg.setData(bundle);
            }
            if (c <= 5) {
                bundle.putInt("rltire", 1);
                msg.setData(bundle);
            } else if (c <= 30 && c > 5) {
                bundle.putInt("rltire", 2);
                msg.setData(bundle);
            } else if (c <= 100) {
                bundle.putInt("rltire", 3);
                msg.setData(bundle);
            }
            if (d <= 5) {
                bundle.putInt("rrtire", 1);
                msg.setData(bundle);
            } else if (d <= 30 && d > 5) {
                bundle.putInt("rrtire", 2);
                msg.setData(bundle);
            } else if (d <= 100) {
                bundle.putInt("rrtire", 3);
                msg.setData(bundle);
            }
            System.out.println(bundle);
            Log.d("[Server]", String.valueOf(bundle));
            tirehandler.sendMessage(msg);
        }
    }

    class TireHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            final int fl = bundle.getInt("fltire");
            final int fr = bundle.getInt("frtire");
            final int rl = bundle.getInt("rltire");
            final int rr = bundle.getInt("rrtire");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (fl == 1) {
                        imageView_fltire.setImageResource(R.drawable.redcircle);
                    } else if (fl == 2) {
                        imageView_fltire.setImageResource(R.drawable.orangecircle);
                    } else if (fl == 3) {
                        imageView_fltire.setImageResource(R.drawable.greencircle);
                    }
                    if (fr == 1) {
                        imageView_frtire.setImageResource(R.drawable.redcircle);
                    } else if (fr == 2) {
                        imageView_frtire.setImageResource(R.drawable.orangecircle);
                    } else if (fr == 3) {
                        imageView_frtire.setImageResource(R.drawable.greencircle);
                    }
                    if (rl == 1) {
                        imageView_rltire.setImageResource(R.drawable.redcircle);
                    } else if (rl == 2) {
                        imageView_rltire.setImageResource(R.drawable.orangecircle);
                    } else if (rl == 3) {
                        imageView_rltire.setImageResource(R.drawable.greencircle);
                    }
                    if (rr == 1) {
                        imageView_rrtire.setImageResource(R.drawable.redcircle);
                    } else if (rr == 2) {
                        imageView_rrtire.setImageResource(R.drawable.orangecircle);
                    } else if (rr == 3) {
                        imageView_rrtire.setImageResource(R.drawable.greencircle);
                    }
                }
            });

        }
    }




    Runnable timeThread = new Runnable() {
        @Override
        public void run() {

            while (true) {
                try {
                    Thread.sleep(1000);

                } catch (Exception e) {

                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Date time = new Date();
                        timeNowTime = formatTime.format(time);
                        timeNowDate = formatDate.format(time);
                        textView_time.setText(timeNowTime);
                        textView_todayDate.setText(timeNowDate);
                    }
                });
            }
        }
    };


    // 태블릿 켤 때 db에서 푸쉬정보를 가져와 오류를 방지한다
    class GetPushCheckAsync extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0];
            String result = HttpConnect.getString(url); //result는 JSON
            return result;
        }

        @Override
        protected void onPostExecute(String s) {

            Log.d("[TAG]", "pushcheck:" + s);
            JSONArray ja = null;
            try {
                ja = new JSONArray(s);
                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);

                    String accpush = jo.getString("accpushcheck");
                    if (accpush.equals("o")) {
                        accpushcheck = "o";
                    } else if (accpush.equals("f")) {
                        accpushcheck = "f";
                    }
                    String droppush = jo.getString("droppushcheck");
                    if (droppush.equals("o")) {
                        droppushcheck = "o";
                    } else if (droppush.equals("f")) {
                        droppushcheck = "f";
                    }
                    String sleeppush = jo.getString("sleeppushcheck");
                    if (sleeppush.equals("o")) {
                        sleeppushcheck = "o";
                    } else if (sleeppush.equals("f")) {
                        sleeppushcheck = "f";
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    class PushCheckThread implements Runnable {

        @Override
        public void run() {
            getPushCheck();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class DownloadFilesTask extends AsyncTask<String,Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bmp = null;
            try {
                String img_url = strings[0]; //url of the image
                URL url = new URL(img_url);
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmp;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Bitmap result) {
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getSensor("CA00000700005000");
        finish();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        getSensor("CA00000700005000");
        finish();
        System.exit(0);
    }

}

