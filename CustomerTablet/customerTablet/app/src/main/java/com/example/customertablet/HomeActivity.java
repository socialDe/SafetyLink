package com.example.customertablet;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.df.DataFrame;
import com.example.customertablet.network.HttpConnect;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.vo.CarSensorVO;
import com.vo.CarVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {

    ImageButton imageButton_control, imageButton_map, imageButton_setting, imageButton_tempUp, imageButton_tempDown;
    TextView textView_velocity, textView_oil, textView_heartbeat;
    TextView textView_temp, textView_targetTemp, textView_weatherTemp, textView_address, textView_todayDate, textView_weather;
    ImageView imageView_frtire, imageView_fltire, imageView_rrtire, imageView_rltire, imageView_door, imageView_weather, imageView_starting, imageView_moving;

    // TCP/IP Server
    ServerSocket serverSocket;
    int serverPort = 5558;
    Sender sender;
    HashMap<String, ObjectOutputStream> maps = new HashMap<>();

    // 현재 시간 표시 설정
    SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
    Date time = new Date();
    String timeNow = format.format(time);

    // HTTP
    DataFrame dataF;
    static String strJson = "";

    NotificationManager manager; // FCM을 위한 NotificationManager

    CarSensorVO carsensor;
    CarVO car;

    public CarVO getCar() {
        return car;
    }

    public void setCar(CarVO car) {
        this.car = car;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        textView_velocity = findViewById(R.id.textView_velocity);
        textView_oil = findViewById(R.id.textView_oil);
        textView_heartbeat = findViewById(R.id.textView_heartbeat);
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

        // 여기서 부터는 앱 실행상태에서 상태바 설정!!
//        LocalBroadcastManager lbm = LocalBroadcastManager.getInstance(this); // 브로드캐스트를 받을 준비
//        lbm.registerReceiver(receiver, new IntentFilter("notification")); // notification이라는 이름의 정보를 받겠다

        // serverStart
        try {
            startServer();
        } catch (Exception e) {
            e.printStackTrace();
        }

        getCarData();

} // end OnCreate

    public void startServer() throws Exception {
        serverSocket = new ServerSocket(serverPort);
        Log.d("[Server]", "Start Server...");

        Runnable r = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Socket socket = null;
                        Log.d("[Server]", "Server Ready..");
                        Log.d("[Server]", serverSocket.toString());
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
                    //TCPIP로 받은 값을 다시 TCPIP로 전송할 일은 없으므로 사용 X
//                  sendDataFrame(input);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textView_temp.setText(input.getContents());
                        }
                    });
                    // 받은 DataFrame을 웹서버로 HTTP 전송
                    // call AsynTask to perform network operation on separate thread
                    HttpAsyncTask httpTask = new HttpAsyncTask(HomeActivity.this);
                    httpTask.execute("http://192.168.25.35/webServer/getTabletSensor.mc", car.getCarid()+"", input.getContents());
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

    // TCP/IP Send CODE
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

    /*
            Car Data
    */

    public void getCarData() {
        // URL 설정.
        String carUrl = "http://192.168.25.35/webServer/cardata.mc?carid=1";

        // AsyncTask를 통해 HttpURLConnection 수행.
        CarAsync carAsync = new CarAsync();
        carAsync.execute(carUrl);
    }
    class CarAsync extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
//            progressDialog = new ProgressDialog(MainActivity.this);
//            progressDialog.setTitle("Get Data ...");
//            progressDialog.setCancelable(false);
//            progressDialog.show();
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
//            progressDialog.dismiss();
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

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    } // Car Data End

    /*
        HTTP 통신 Code
     */
    public String GET(String webUrl, String carid, String contents) {
        URL url = null;
        StringBuilder html = new StringBuilder();
        webUrl += "?carid=" + carid + "&contents=" + contents;
        try {
            url = new URL(webUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            if (con != null) {
                con.setConnectTimeout(10000);
                con.setUseCaches(false);
                if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(con.getInputStream()));
                    for (; ; ) {
                        String line = br.readLine();
                        if (line == null) break;
                        html.append(line);
                        html.append('\n');
                    }
                    br.close();
                }
                con.disconnect();
            }
        } catch (Exception ex) {
            ;
        }

        return html.toString();
    }

    public static String POST(String url, DataFrame df, CarVO car) {
        InputStream is = null;
        String result = "";
        try {
            URL urlCon = new URL(url);
            HttpURLConnection httpCon = (HttpURLConnection) urlCon.openConnection();

            String json = "";

            // build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("carid", car.getCarid()+"");
            jsonObject.accumulate("contents", df.getContents());

            // convert JSONObject to JSON to String
            json = jsonObject.toString();
            Log.d("[Server]", "HTTP JSON 생성 후 전송 " + json);

            // Set some headers to inform server about the type of the content
            httpCon.setRequestProperty("Accept", "application/json");
            httpCon.setRequestProperty("Content-type", "application/json");
            httpCon.setRequestMethod("POST");

            // OutputStream으로 POST 데이터를 넘겨주겠다는 옵션.
            httpCon.setDoOutput(true);
            // InputStream으로 서버로 부터 응답을 받겠다는 옵션.
            httpCon.setDoInput(true);

            // JSON 전송
            OutputStream os = httpCon.getOutputStream();
            os.write(json.getBytes("utf-8"));
            os.flush();
            Log.d("[Server]", "HTTP JSON 전송");


            // receive response as inputStream
            try {
                is = httpCon.getInputStream();
                // convert inputstream to string
                if (is != null) {
                    result = convertInputStreamToString(is);
                    Log.d("[Server]", "HTTP 통신 수신: " + result);
                } else
                    result = "Did not work!";
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                httpCon.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            Log.d("[Server]", e.getLocalizedMessage());
        }

        return result;
    }

    public boolean isConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        private HomeActivity homeAct;

        HttpAsyncTask(HomeActivity homeActivity) {
            this.homeAct = homeActivity;
        }

        @Override
        protected String doInBackground(String... urls) {

            dataF = new DataFrame();
            car.getCarid(urls[1]); // CarVO에 public void get Carid(String url) 추가
            dataF.setContents(urls[2]);
            Log.d("[Server]", "[AsyncTask Background]" + urls[0] + urls[1] + urls);

//            return POST(urls[0], dataF);
            return GET(urls[0], urls[1], urls[2]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            strJson = result;
            homeAct.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(homeAct, "Received!", Toast.LENGTH_LONG).show();
                    try {
                        JSONArray json = new JSONArray(strJson);
//                        homeAct.tx_logCtl2.setText(json.toString(1)); // Semi 때, 제어한 것 log로 찍어줬던 기능
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }


    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }// End HTTP 통신 Code


  /*
       FCM 통신
                    */
//    public final BroadcastReceiver receiver = new BroadcastReceiver() { // FCM 받는 부분
//        public void onReceive(Context context, Intent intent) {
//            if (intent != null) {
//                String title = intent.getStringExtra("title");
//                final String control = intent.getStringExtra("control");
//                final String data = intent.getStringExtra("data");
//                // 데이터를 보낼 때마다 새로운 값을 getString하므로 final을 써도 된다
//
//                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE); // 진동 없애려면 삭제
//                if (Build.VERSION.SDK_INT >= 26) { //버전 체크를 해줘야 작동하도록 한다
//                    vibrator.vibrate(VibrationEffect.createOneShot(1000, 10));
//                } else {
//                    vibrator.vibrate(1000);
//                }
//
//                if (control.equals("temp")) { // 받은 FCM 데이터를 화면에 seekBar와 Switch로 보여줌
//                    seekBar.setProgress(Integer.parseInt(data.toString()), true);
//                    tx_logTemp2.append("목표 온도가 " + data + "℃로 변경되었습니다." + "\n");
//                    // sendDataFrame을 이용해 FCM으로 받은 데이터를 Car Head로 전송
//                    final Set<String> set = maps.keySet(); // maps.keySet()를 통해 키<>를 받아옴
//                    DataFrame df = new DataFrame(); // 받은 데이터를 TCP/IP로 전송하는 부분
//                    String ipp = "";
//                    for (String key : set) { // key값이 1개이므로 for문으로 받아온다
//                        ipp = key.substring(1); // 앞에 들어오는 / 를 지운다
//                        Log.d("[Server]", ipp);
//                    }
//                    df.setIp(ipp);
//                    df.setSender(control);
//                    df.setContents(data);
//                    sendDataFrame(df); // 데이터 프레임 send 함수를 통해 전송
//                } else if (control.equals("door")) {
//                    if (data.equals("0")) {
//                        sw_door.setChecked(true);
//                        tx_logCtl2.append("문이 잠겼습니다." + "\n");
//                    } else if (data.equals("1")) {
//                        sw_door.setChecked(false);
//                        tx_logCtl2.append("문이 열렸습니다." + "\n");
//                    }
//                } else if (control.equals("power")) {
//                    if (data.equals("s")) {
//                        sw_power.setChecked(true);
//                        tx_logCtl2.append("시동이 켜졌습니다." + "\n");
//                    } else if (data.equals("t")) {
//                        sw_power.setChecked(false);
//                        tx_logCtl2.append("시동이 꺼졌습니다." + "\n");
//                    }
//                }
//
//                manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//                NotificationCompat.Builder builder = null;
//                if (Build.VERSION.SDK_INT >= 26) {
//                    if (manager.getNotificationChannel("ch1") == null) {
//                        manager.createNotificationChannel(
//                                new NotificationChannel("ch1", "chname", NotificationManager.IMPORTANCE_DEFAULT));
//                    }
//                    builder = new NotificationCompat.Builder(context, "ch1");
//                } else {
//                    builder = new NotificationCompat.Builder(context);
//                }
//
//                Intent intent1 = new Intent(context, MainActivity.class);
//                PendingIntent pendingIntent = PendingIntent.getActivity(
//                        context, 101, intent1, PendingIntent.FLAG_UPDATE_CURRENT
//                );
//                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                builder.setAutoCancel(true);
//                builder.setContentIntent(pendingIntent);
//
//                builder.setContentTitle(title);
//                if (data.equals("0")) {
//                    builder.setContentText(control + " 이(가) ON/LOCK 상태로 변경되었습니다.");
//                } else if (data.equals("1")) {
//                    builder.setContentText(control + " 이(가) OFF/UNLOCK 상태로 변경되었습니다.");
//                } else if (data.equals("s")){
//                    builder.setContentText(control + " 이(가) ON 상태로 변경되었습니다.");
//                } else if (data.equals("t")){
//                    builder.setContentText(control + " 이(가) OFF 상태로 변경되었습니다.");
//                } else {
//                    builder.setContentText(control + " 이(가)" + data + " ℃로 변경되었습니다.");
//                }
//                builder.setSmallIcon(R.drawable.car);
//                Notification noti = builder.build();
//                manager.notify(1, noti); // 상단 알림을 없애려면 이곳 주석 처리
//            }
//        }
//    };
}