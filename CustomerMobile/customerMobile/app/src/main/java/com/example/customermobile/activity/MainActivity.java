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
import com.example.customermobile.network.HttpConnect;

import com.example.customermobile.vo.UsersVO;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.owl93.dpb.CircularProgressView;
import com.skydoves.progressview.OnProgressChangeListener;
import com.skydoves.progressview.ProgressView;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import www.sanju.motiontoast.MotionToast;

import static com.example.customermobile.activity.LoginActivity.ip;

public class MainActivity extends AppCompatActivity{
    SharedPreferences sp;
    UsersVO user;
    SharedPreferences sptoken; // token
    String token; // token 값 저장

    CircularProgressView circularProgressView;
    ProgressView progressView;

    // 소셜로그인
    private FirebaseAuth mAuth ;
    Button btnRevoke, btnLogout;

    HttpAsyncTask httpAsyncTask;
    NotificationManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        // 소셜 로그인
//        mAuth = FirebaseAuth.getInstance();
//
//
//        // 회원정보를 intent로 가져오기
//        Intent getintent = getIntent();
//        user = null;
//        user = (UsersVO) getintent.getSerializableExtra("user");
//
//        sp = getSharedPreferences("user", MODE_PRIVATE);
//
//        // intent 정보가 없을 경우, sp로 회원정보 가져오기
//        if(user == null) {
//            String userid = sp.getString("userid", "");
//
//
//            // 자동로그인 정보가 있으면 회원정보 계속 가져오기
//            String userpwd = sp.getString("userpwd", "");
//            String username = sp.getString("username", "");
//            String userphone = sp.getString("userphone", "");
//            String strbirth = sp.getString("userbirth", "");
//            String usersex = sp.getString("usersex", "");
//            String strregdate = sp.getString("userregdate", "");
//            String userstate = sp.getString("userstate", "");
//            String usersubject = sp.getString("usersubject", "");
//            String babypushcheck = sp.getString("babypushcheck", "");
//            String accpushcheck = sp.getString("accpushcheck", "");
//            String mobiletoken = sp.getString("mobiletoken", "");
//
//            // String 변수를 Date로 변환
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Date userbirth = null;
//            Date userregdate = null;
//            try {
//                userbirth = sdf.parse(strbirth);
//                userregdate = sdf.parse(strregdate);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//
//            // sp 정보로 회원 객체 생성
//            user = new UsersVO(userid, userpwd, username, userphone, userbirth, usersex, userregdate, userstate, usersubject, babypushcheck, accpushcheck, mobiletoken);
//
//        }
//
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
            // 로그아웃 버튼

            // 자동 로그인 정보 삭제
            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            editor.commit();


            String url = "http://"+ip+"/webServer/userlogoutimpl.mc";
            url += "?id=" + user.getUserid()+"&destroy=no";
            httpAsyncTask = new HttpAsyncTask();
            httpAsyncTask.execute(url);

        }
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

            if(sp.getString("userid","") == null){
                progressDialog.show();
            }else{

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
            }else if(result.equals("destroy")){
                // destroy
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
        }
    }
    // End HTTP 통신 Code

    @Override
    protected void onDestroy() {
        String url = "http://"+ip+"/webServer/userlogoutimpl.mc";
        String destroy = "yes";

        url += "?id=" + user.getUserid() +"&destroy="+destroy;
        httpAsyncTask = new HttpAsyncTask();
        httpAsyncTask.execute(url);
        super.onDestroy();
    }


}