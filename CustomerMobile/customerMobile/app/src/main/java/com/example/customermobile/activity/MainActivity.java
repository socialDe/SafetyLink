package com.example.customermobile.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.customermobile.R;
<<<<<<< HEAD
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
=======
import com.example.customermobile.network.HttpConnect;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
>>>>>>> feature/mobile_login
import com.owl93.dpb.CircularProgressView;
import com.skydoves.progressview.OnProgressChangeListener;
import com.skydoves.progressview.ProgressView;
import com.vo.UsersVO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import www.sanju.motiontoast.MotionToast;

<<<<<<< HEAD
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences sp;
=======
public class MainActivity extends AppCompatActivity {
    SharedPreferences sp; // 자동 로그인
>>>>>>> feature/mobile_login
    UsersVO user;
    SharedPreferences sptoken; // token
    String token; // token 값 저장

    CircularProgressView circularProgressView;
    ProgressView progressView;

<<<<<<< HEAD
    // 소셜로그인
    private FirebaseAuth mAuth ;
    Button btnRevoke, btnLogout;
=======
    HttpAsyncTask httpAsyncTask;
    NotificationManager manager;
>>>>>>> feature/mobile_login

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 앱 실행시 디바이스 토큰 불러오기
        getToken();

        // 소셜 로그인
        mAuth = FirebaseAuth.getInstance();
        btnLogout = (Button)findViewById(R.id.btn_logout);
        btnRevoke = (Button)findViewById(R.id.btn_revoke);
        btnLogout.setOnClickListener(this);
        btnRevoke.setOnClickListener(this);

        // 회원정보를 intent로 가져오기
        Intent getintent = getIntent();
        user = null;
        user = (UsersVO) getintent.getSerializableExtra("user");

        sp = getSharedPreferences("user", MODE_PRIVATE);

        // intent 정보가 없을 경우, sp로 회원정보 가져오기
        if(user == null) {
            String userid = sp.getString("userid", "");

            // 자동로그인 정보가 없을 경우 메인액티비티 없이 로그인 페이지로 전환
            if (userid == null || userid == "") {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

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

        circularProgressView = findViewById(R.id.circularProgressView);
        circularProgressView.setProgress(0);
        circularProgressView.setMaxValue(100);

        progressView = findViewById(R.id.progressView);
        progressView.setOnProgressChangeListener(new OnProgressChangeListener() {
            @Override
            public void onChange(float v) {
                progressView.setLabelText("Progress: " + v + "%");
            }
        });
    }

    // 소셜 로그아웃 함수
    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        Log.d("[TEST]","로그아웃");
    }
    // 소셜 회원탈퇴 함수
//    private void revokeAccess() {
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//        user.delete()
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Log.d("[TEST]","User account deleted.");
//                        }
//                    }
//                });
//        Log.d("[TEST]","회원탈퇴");
//    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);

        switch (v.getId()) {
            case R.id.btn_logout:
                signOut();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
                break;
//            case R.id.btn_revoke:
//                revokeAccess();
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                startActivity(intent);
//                finish();
//                break;
        }
    }


    public void clickbt(View v){
        if(v.getId() == R.id.button1){
            circularProgressView.setProgress(circularProgressView.getProgress() + 5);
        }else if(v.getId() == R.id.button2){
            MotionToast.Companion.createToast(this,
                    "Hurray success 😍",
                    "Upload Completed successfully!",
                    MotionToast.TOAST_SUCCESS,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this,R.font.helvetica_regular));
        }else if(v.getId() == R.id.button3){
            Random r = new Random();
            progressView.setProgress(r.nextInt(100));
        }else if(v.getId() == R.id.button4){
<<<<<<< HEAD
            // 자동 로그인 정보 삭제
            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            editor.commit();

            Toast.makeText(MainActivity.this, "자동 로그인이 취소되었습니다.", Toast.LENGTH_SHORT).show();


//            // 액티비티 기록 없이 로그인 화면으로 전환
//            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
=======
            // 로그아웃 버튼
//                    String url = "http://192.168.219.110/webServer/userlogoutimpl.mc";
            String url = "http://192.168.0.112/webServer/userlogoutimpl.mc";
            url += "?id=" + user.getUserid();
            httpAsyncTask = new HttpAsyncTask();
            httpAsyncTask.execute(url);
        }
    }

    public void getToken(){
        //토큰값을 받아옵니다.
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        // 토큰이 계속 초기화가 되기때문에 sharedPreferences로 저장하여 초기화 방지
                        token = task.getResult().getToken();
                        sptoken = getSharedPreferences("applicaton",MODE_PRIVATE);
                        SharedPreferences.Editor editor = sptoken.edit();
                        editor.putString("token",token); // key, value를 이용하여 저장하는 형태
                        editor.commit();
                        Log.d("[Log]", token);
                    }
                });
    }


    /*
    HTTP 통신 Code
    */
    class HttpAsyncTask extends AsyncTask<String,String,String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("로그아웃");
            progressDialog.setCancelable(false);
            progressDialog.show();
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
                // 자동 로그인 정보 삭제
                SharedPreferences.Editor editor = sp.edit();
                editor.clear();
                editor.commit();

                // 액티비티 기록 없이 로그인 화면으로 전환
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }else if(result.equals("logoutfail")){
                // 로그아웃 실패: Exception
                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("로그아웃에 실패하였습니다.");
                builder.setMessage("다시 시도해 주십시오.");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

                builder.show();
            }
>>>>>>> feature/mobile_login
        }
    }
    // End HTTP 통신 Code
}