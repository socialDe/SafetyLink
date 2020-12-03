package com.example.customermobile.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.customermobile.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.owl93.dpb.CircularProgressView;
import com.skydoves.progressview.OnProgressChangeListener;
import com.skydoves.progressview.ProgressView;
import com.vo.UsersVO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import www.sanju.motiontoast.MotionToast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences sp;
    UsersVO user;

    CircularProgressView circularProgressView;
    ProgressView progressView;

    // 소셜로그인
    private FirebaseAuth mAuth ;
    Button btnRevoke, btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            // 자동 로그인 정보 삭제
            SharedPreferences.Editor editor = sp.edit();
            editor.clear();
            editor.commit();

            Toast.makeText(MainActivity.this, "자동 로그인이 취소되었습니다.", Toast.LENGTH_SHORT).show();


//            // 액티비티 기록 없이 로그인 화면으로 전환
//            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
        }
    }
}