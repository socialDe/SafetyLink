package com.example.customermobile.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.customermobile.R;
import com.owl93.dpb.CircularProgressView;
import com.skydoves.progressview.OnProgressChangeListener;
import com.skydoves.progressview.ProgressView;
import com.vo.UsersVO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import www.sanju.motiontoast.MotionToast;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sp;
    UsersVO user;

    CircularProgressView circularProgressView;
    ProgressView progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 회원정보를 intent로 가져오기
        Intent getintent = getIntent();
        user = null;
        user = (UsersVO) getintent.getSerializableExtra("user");

        // intent 정보가 없을 경우, sp로 가져오기
        if(user == null) {
            sp = getSharedPreferences("autoLogin", MODE_PRIVATE);
            String userid = sp.getString("userid", "");

            // 자동로그인 정보가 없을 경우 로그인 페이지로 전환
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
            // 자동 로그인 정보 삭제
            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            editor.commit();

            // 액티비티 기록 없이 로그인 화면으로 전환
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}