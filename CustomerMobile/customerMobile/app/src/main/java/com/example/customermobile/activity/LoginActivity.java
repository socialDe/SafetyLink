package com.example.customermobile.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.customermobile.R;
import com.example.customermobile.network.HttpConnect;
import com.vo.UsersVO;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LoginActivity extends AppCompatActivity {
    HttpAsyncTask httpAsyncTask; // HTTP 전송 데이터
    SharedPreferences sp; // 자동 로그인
    UsersVO user; // user 객체

    ActionBar actionBar;
    EditText editText_loginid, editText_loginpwd;
    CheckBox checkBox_loginauto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // ActionBar Setting
        actionBar = getSupportActionBar();
        actionBar.hide();

        editText_loginid = findViewById(R.id.editText_loginid);
        editText_loginpwd = findViewById(R.id.editText_loginpwd);
        checkBox_loginauto = findViewById(R.id.checkBox_loginAuto);

        sp = getSharedPreferences("autoLogin", MODE_PRIVATE);

        String userid = sp.getString("userid", "");
        String userpwd = sp.getString("userpwd", "");

        if(userid != null && userid != "" && userpwd != null && userpwd != ""){
            login(userid, userpwd);
        }
    }

    public void clickbt(View v){
        if(v.getId() == R.id.button_login){
            UsersVO user = new UsersVO(editText_loginid.getText().toString(), editText_loginpwd.getText().toString());

            String id = editText_loginid.getText().toString();
            String pwd = editText_loginpwd.getText().toString();
            login(id, pwd);

            // tcp/ip connect
//            new Thread(con).start();

            // tcp/ip 서버로 유저 정보 전송
//            tcpipConnect.login(user);
        }else if(v.getId() == R.id.button_loginNaver){

        }else if(v.getId() == R.id.button_loginFacebook){

        }else if(v.getId() == R.id.button_loginKakaotalk){

        }else if(v.getId() == R.id.button_register){
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
        }else if(v.getId() == R.id.checkBox_loginAuto){

        }else if(v.getId() == R.id.textView_idFind){

        }else if(v.getId() == R.id.textView_pwdFind){

        }
     }


//    Runnable con = new Runnable() {
//        @Override
//        public void run() {
//            try {
//                tcpipConnect.connect();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    };

    public void login(String id, String pwd){
        String url = "http://192.168.0.112/webServer/userloginimpl.mc";
        url += "?id="+id+"&pwd="+pwd;
        httpAsyncTask = new HttpAsyncTask();
        httpAsyncTask.execute(url);
    }

    /*
    HTTP 통신 Code
    */

    class HttpAsyncTask extends AsyncTask<String,String,String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            Log.d("[Log]", "preExecute");
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setTitle("Login");
            progressDialog.setCancelable(false);
            progressDialog.show();
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
            String result = s.trim();

            if (result.equals("fail")) {
                // LOGIN FAIL
                androidx.appcompat.app.AlertDialog.Builder dailog = new AlertDialog.Builder(LoginActivity.this);
                dailog.setTitle("LOGIN FAIL");
                dailog.setMessage("Try Again...");
                dailog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                dailog.show();
            } else {
                // LOGIN SUCCESS
                JSONObject jo = null;
                try {
                    // JSONObject 값 가져오기
                    jo = new JSONObject(s);
                    String userid = jo.getString("userid");
                    String userpwd = jo.getString("userpwd");
                    String username = jo.getString("username");
                    String userphone = jo.getString("userphone");
                    String strbirth = jo.getString("userbirth");
                    String usersex = jo.getString("usersex");
                    String strregdate = jo.getString("userregdate");
                    String userstate = jo.getString("userstate");
                    String usersubject = jo.getString("usersubject");
                    String babypushcheck = jo.getString("babypushcheck");
                    String accpushcheck = jo.getString("accpushcheck");
                    String mobiletoken = jo.getString("mobiletoken");

                    // String 변수를 Date로 변환
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date userbirth = sdf.parse(strbirth);
                    Date userregdate = sdf.parse(strregdate);

                    // user 객체 생성
                    user = new UsersVO(userid, userpwd, username, userphone, userbirth, usersex, userregdate, userstate, usersubject, babypushcheck, accpushcheck, mobiletoken);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 자동 로그인
                if(checkBox_loginauto.isChecked()) {
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString("userid", user.getUserid());
                    edit.putString("userpwd", user.getUserpwd());
                    edit.commit();
                }

                // LOGIN COMPLETE
                // 액티비티 기록 없이 메인 화면으로 전환
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("user", user);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }
    }// End HTTP 통신 Code
}