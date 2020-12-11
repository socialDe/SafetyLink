package com.example.customertablet;


import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.df.DataFrame;
import com.example.customertablet.network.HttpConnect;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.vo.CarSensorVO;
import com.vo.CarVO;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static com.example.customertablet.MainActivity.ip;

public class HomeActivity extends AppCompatActivity {


    ImageButton imageButton_control, imageButton_map, imageButton_setting, imageButton_tempUp, imageButton_tempDown;
    TextView textView_velocity, textView_oil, textView_heartbeat;
    TextView textView_temp, textView_targetTemp, textView_weatherTemp, textView_address, textView_todayDate, textView_weather;
    ImageView imageView_frtire, imageView_fltire, imageView_rrtire, imageView_rltire, imageView_door, imageView_weather, imageView_starting, imageView_moving;

    // TCP/IP Server
    ServerSocket serverSocket;
    Socket socket = null;
    int serverPort = 5558;

    Sender sender;
    HashMap<String, ObjectOutputStream> maps = new HashMap<>();

    // 현재 시간 표시 설정
    SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
    Date time = new Date();
    String timeNow = format.format(time);

    // HTTP
    DataFrame dataF;
    HttpAsyncTask httpAsyncTask;

    NotificationManager manager; // FCM을 위한 NotificationManager

    CarSensorVO carsensor;
    CarVO car;

    // 무게 관련 변수
    int initialLoad; //초기 무게
    ArrayList<Integer> loadDatas = new ArrayList<>(); // 주행중 데이터 저장 ArrayLis
    double avgLoad; // 평균 무게
    private int dialogLoad = 1; // 적재물 낙하 AlertDialog의 flag


    public CarVO getCar() {
        return car;
    }

    public void setCar(CarVO car) {
        this.car = car;
    }
    SharedPreferences sp;
    String carnum;
    Handler handler;


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

        imageButton_control = findViewById(R.id.imageButton_control);
        imageButton_map = findViewById(R.id.imageButton_map);
        imageButton_setting = findViewById(R.id.imageButton_setting);
        imageButton_tempUp = findViewById(R.id.imageButton_tempUp);
        imageButton_tempDown = findViewById(R.id.imageButton_tempDown);

        imageView_frtire = findViewById(R.id.imageView_frTire);
        imageView_fltire = findViewById(R.id.imageView_flTire);
        imageView_rrtire = findViewById(R.id.imageView_rrTire);
        imageView_rltire = findViewById(R.id.imageView_rlTire);
        imageView_door = findViewById(R.id.imageView_door);
        imageView_weather = findViewById(R.id.imageView_weather);
        imageView_starting = findViewById(R.id.imageView_starting);
        imageView_moving = findViewById(R.id.imageView_moving);
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


        sp = getSharedPreferences("token",MODE_PRIVATE);
        carnum = sp.getString("num","");
        Log.d("[Server]","carnum:"+carnum);
} // end OnCreate


    public void sendfcm(String contents) {
        String urlstr = "http://" + ip + "/webServer/sendfcm.mc";
        String conrtolUrl = urlstr + "?carnum=" + carnum +"&contents=" + contents;

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

                    //setUi(input.getContents());


                    if(input.getContents().substring(4,8).equals("0002")
                          ||  input.getContents().substring(4,8).equals("0003")
                            ||  input.getContents().substring(4,8).equals("0004"))  {

                        sendfcm(input.getContents());

                    }

                    // 받은 데이터가 주행 데이터인 경우
                    if(input.getContents().substring(4,8).equals("0032")){
                        Log.d("[Run]", "[Run]: "+input.getContents().substring(8));
                        String runData = input.getContents().substring(8);

                        if(runData.equals("00000000")){
                            // 주행 종료
                            imageView_moving.setBackgroundColor(Color.GRAY);
                        }else if(runData.equals("00000001")){
                            // 주행 시작
                            imageView_moving.setBackgroundColor(Color.GREEN);
                        }
                    }




                    // 받은 데이터가 무게 데이터인 경우 수행
                    if(input.getContents().substring(4,8).equals("0005")){
                        Log.d("[Load]", "[Load]: "+input.getContents().substring(8));
                        String loadData = input.getContents().substring(8);


                        // 주행 시작시 전송하는 무게 데이터
                        if(loadData.substring(0,1).equals("9")){
                            initialLoad = Integer.parseInt(loadData.substring(1));
                            Log.d("[Load]", "[Load]: 초기 무게 데이터 "+initialLoad+" 설정되었습니다.");
                            dialogLoad = 1;
                        }else {
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

                                    if(dialogLoad == 1){
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



                    if(input.getContents().equals("온도데이터 코드")) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textView_temp.setText(input.getContents());
                            }
                        });
                    }

//                    // 받은 DataFrame 중 HTTP로 전송해야 하는 데이터
//                    if(input.getContents().equals("전송해야 하는 데이터")) {

                        // 받은 DataFrame을 웹서버로 HTTP 전송
                        // call AsynTask to perform network operation on separate thread

                        String url = "http://"+ip+"/webServer/getTabletSensor.mc";
                        url += "?carnum=" + carnum + "&contents=" + input.getContents() + "";
                        httpAsyncTask = new HttpAsyncTask();
                        // Thread 안에서 thread가 돌아갈 땐 Handler을 사용해야 한다
                        Handler mHandler = new Handler(Looper.getMainLooper());
                        final String finalUrl = url;
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                httpAsyncTask.execute(finalUrl);
                            }
                        }); // 1000으로 해야 돌아간다...

//                    }
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
                if(maps.get("/"+dataFrame.getIp())!= null){
                    maps.get("/"+dataFrame.getIp()).writeObject(dataFrame);
                }
                Log.d("[Server]", "Sender 객체 전송.. "+dataFrame.getIp()+"주소로 "+dataFrame.getContents());
                Log.d("[Server]", "Sender 객체 전송 성공");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    } // End TCP/IP 통신 Code


    public void setUi(final String contents){

        final String contentsSensor = contents.substring(0,4);
        int contentsData = Integer.parseInt(contents.substring(4));

        // contentsData 사용해서 UI 바꾸기!!
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 온도
                if(contentsSensor.equals("0001")) {
                    //contentsData/100  온도값 ex)15
                }
                // 충돌
                else if(contentsSensor.equals("0002")) {
                    //String.valueOf(contentsData) 충돌여부 ex)1,0
                }
                // 진동
                else if(contentsSensor.equals("0003")) {
                    //String.valueOf(contentsData) 충돌세기 ex)2(small),3(big)
                }
                // pir
                else if(contentsSensor.equals("0004")) {
                   //String.valueOf(contentsData) 영유아감지여부 ex)1,0
                }
                // 무게
                else if(contentsSensor.equals("0005")) {
                    //contentsData 무게 ex)30
                }
                // 심박수
                else if(contentsSensor.equals("0006")) {
                    //contentsData 심박수 ex) 80
                }
                // 연료
                else if(contentsSensor.equals("0007")) {
                    //contentsData 현재연료량 ex) 40
                }
                // 에어컨
                else if(contentsSensor.equals("0021")) {
                    //String.valueOf(contentsData) 에어컨목표온도값 ex) 25
                }
                // 시동
                else if(contentsSensor.equals("0031")) {
                    //String.valueOf(contentsData) 시동여부 ex)1,0
                }
                // 주행
                else if(contentsSensor.equals("0032")) {
                    //String.valueOf(contentsData) 주행여부 ex)1,0
                    //time.getTime() 주행시작시간 ex) 시간값형태로 나올듯
                }
                // 문
                else if(contentsSensor.equals("0033")) {
                    //String.valueOf(contentsData) 문  ex)1,0
                }
            }

        });
    }

    private void _runOnUiThread(Runnable runnable){
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

                if(contents.length() != 4){
                    DataFrame df = new DataFrame();
                    // 연결된 IP로 df를 보낸다
                    df.setIp(socket.getInetAddress().toString().substring(1));
                    df.setSender("Mobile");
                    df.setContents(contents);
                    Log.d("[Server]",df.toString());
                    sendDataFrame(df);
                }


                //여기를 풀어서 모바일에서 제어시 UI변경
                //setUi(contents);


                Log.d("[Server]",carid);
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
                builder.setContentText(carid+ " " + contents);


                if (carid.equals("verify")) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);
                    alertDialog.setTitle(Integer.parseInt(contents)+"")
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

}

