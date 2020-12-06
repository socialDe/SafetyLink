package com.example.customertablet;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.customertablet.df.DataFrame;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    HttpAsyncTask httpAsyncTask;
    ActionBar actionBar;
    EditText editText_carNum, editText_carYear, editText_carModel;
    Button button_carRegister;
    Spinner spinner_carType, spinner_oilType;
    TextView textView_carType, textView_oilType;
    // dropdown 목록을 위해 정의
    String[] carType = {"승용차", "승합차", "트럭"};
    String[] oilType = {"휘발유", "경유", "전기차", "LPG", "수소차", "하이브리드"};

    // TCP/IP 통신
    int port;
    String address;
    String id;
    Socket socket;

    // TCP/IP Server
    ServerSocket serverSocket;
    int serverPort = 5558;
    Sender sender;
    HashMap<String, ObjectOutputStream> maps = new HashMap<>();

    // 현재 시간 표시 설정
    SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
    Date time = new Date();
    String timeNow = format.format(time);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // serverStart
        //Server server = new Server(5558);
        try {
            startServer();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // token 비교를 위한 SharedPreferences 사용 준비
        SharedPreferences pref = getSharedPreferences("token", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        // token이 그대로면 바로 조회/제어 화면으로 이동
        if (pref.getString("token", "").equals(FirebaseInstanceId.getInstance().getToken())) {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
            // 다시 들어가지 못하게 종료
            finish();
        } else if (pref.getString("token", "").equals("") || pref.getString("token", "") == null) {
            // token이 null이면 그대로 차량 등록을 진행한다
        } else {
            // 토큰이 바꼈다면 url 연결해서 token값을 update 해줘야 함
            String num = pref.getString("num", "");
            String token = FirebaseInstanceId.getInstance().getToken();
            editor.putString("token", token);// 새로운 토큰 받아와서 SharedPreference에 저장
            editor.commit();

            String url = "http://192.168.0.103/webServer/tokenupdateimpl.mc";
            url += "?num=" + num + "&token=" + token;
            httpAsyncTask = new HttpAsyncTask();
            httpAsyncTask.execute(url);
        }

        editText_carNum = findViewById(R.id.editText_carNum);
        editText_carYear = findViewById(R.id.editText_carYear);
        editText_carModel = findViewById(R.id.editText_carModel);
        spinner_carType = findViewById(R.id.spinner_carType);
        spinner_oilType = findViewById(R.id.spinner_oilType);
        button_carRegister = findViewById(R.id.button_carRegister);
        textView_carType = findViewById(R.id.textView_carType);
        textView_oilType = findViewById(R.id.textView_oilType);

        // carType Dropdown 메뉴
        ArrayAdapter<String> adapter_car = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,
                carType
        );
        adapter_car.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_carType.setAdapter(adapter_car);
        spinner_carType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textView_carType.setText(carType[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // oilType Dropdown 메뉴
        ArrayAdapter<String> adapter_oil = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item,
                oilType
        );
        adapter_oil.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_oilType.setAdapter(adapter_oil);
        spinner_oilType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                textView_oilType.setText(oilType[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


//        // 현재 시간 표시 설정
//        SimpleDateFormat format = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
//        Date time = new Date();
//        String timeNow = format.format(time);


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
                    DataFrame input = (DataFrame) oi.readObject();
                    Log.d("[Server]", "[DataFrame 수신] " + input.getSender() + ": " + input.getContents());

                    sendDataFrame(input);


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

                maps.get("/" + dataFrame.getIp()).writeObject(dataFrame);
                Log.d("[Server]", "Sender 객체 전송.. " + dataFrame.getIp() + "주소로 " + dataFrame.getContents());
                Log.d("[Server]", "Sender 객체 전송 성공");

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


    // Car Register
    public void clickbt(View v) {
        String userid = "unassigned";
        String num = editText_carNum.getText().toString();
        String carType = textView_carType.getText().toString();
        if (carType.equals("승용차")) {
            carType = "1";
        } else if (carType.equals("승합차")) {
            carType = "2";
        } else if (carType.equals("트럭")) {
            carType = "3";
        }
        String model = editText_carModel.getText().toString();
        String year = editText_carYear.getText().toString();
        String img = "unassigned";
        String oilType = textView_oilType.getText().toString();
        String token = FirebaseInstanceId.getInstance().getToken();
//        String img = "";
//        if(model.equals("소나타")){
//            img = sonata;
//        }

        // token 비교를 위한 SharedPreferences 사용 준비
        SharedPreferences pref = getSharedPreferences("token", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("num", num);
        editor.putString("token", token);
        editor.commit();

        String url = "http://192.168.0.103/webServer/carregisterimpl.mc";
        url += "?userid=" + userid + "&num=" + num + "&cartype=" + carType + "&model=" + model + "&year=" + year + "&img=" + img + "&oilType=" + oilType + "&token=" + token;
        httpAsyncTask = new HttpAsyncTask();
        httpAsyncTask.execute(url);
    }

    /*
    HTTP 통신 Code
    */
    class HttpAsyncTask extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Register ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0].toString();
            String result = com.example.customertablet.network.HttpConnect.getString(url);
            return result;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            //String result = s.trim();
            if (s.equals("fail")) {
                // 차량 등록 실패
                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("ERROR");
                builder.setMessage("차량 등록에 실패하였습니다. 다시 시도해주십시오.");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();

            } else if (s.equals("success")) {
                // 차량 등록 성공
                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("차량 등록");
                builder.setMessage("차량이 등록되었습니다.");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);
                        // 다시 들어가지 못하게 종료
                        finish();
                    }
                });

                builder.show();

            } else if (s.equals("updatesuccess")) {
                // 토큰 업데이트 성공 / 나중에 좀 더 자연스럽게 넘어가도록 builder을 없애자
                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("차량 토큰 최신화");
                builder.setMessage("차량 토큰이 최신화되었습니다.");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        startActivity(intent);
                        // 다시 들어가지 못하게 종료
                        finish();
                    }
                });

                builder.show();
            } else if (s.equals("updatefail")) {
                // 토큰 업데이트 실패
                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("ERROR");
                builder.setMessage("차량 토큰 최신화에 실패하였습니다. 다시 실행해주십시오.");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                builder.show();
            }
        }
        // End HTTP 통신 Code
    }
}