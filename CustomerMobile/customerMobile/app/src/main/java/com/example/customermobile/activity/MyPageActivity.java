package com.example.customermobile.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.customermobile.R;
import com.example.customermobile.vo.UsersVO;

public class MyPageActivity extends AppCompatActivity {

    TextView mypage_tx_registerId, mypage_tx_registerPwdCheck;
    EditText mypage_etx_registerPwd, mypage_etx_registerPwdCon, mypage_etx_registerName,
            mypage_etx_registerPhone1, mypage_etx_registerPhone2, mypage_etx_registerPhone3, mypage_etx_registerBirth;

    RadioGroup mypage_rg_registerSex;

    Boolean pwdcheck;

    UsersVO user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        mypage_tx_registerId = findViewById(R.id.mypage_tx_registerId);
        mypage_tx_registerPwdCheck = findViewById(R.id.mypage_tx_registerPwdCheck);

        mypage_etx_registerPwd = findViewById(R.id.mypage_etx_registerPwd);
        mypage_etx_registerPwdCon = findViewById(R.id.mypage_etx_registerPwdCon);
        mypage_etx_registerName = findViewById(R.id.mypage_etx_registerName);
        mypage_etx_registerPhone1 = findViewById(R.id.mypage_etx_registerPhone1);
        mypage_etx_registerPhone2 = findViewById(R.id.mypage_etx_registerPhone2);
        mypage_etx_registerPhone3 = findViewById(R.id.mypage_etx_registerPhone3);
        mypage_etx_registerBirth = findViewById(R.id.mypage_etx_registerBirth);

        mypage_rg_registerSex = findViewById(R.id.mypage_rg_registerSex);

        Intent getintent = getIntent();
        user = null;
        user = (UsersVO) getintent.getSerializableExtra("user");

        mypage_tx_registerId.setText(user.getUserid()); // 수정 불가능하도록 변경(UI 문제로 일단 보류)
        mypage_etx_registerPwd.setText(user.getUserpwd()); // 나중에는 비워놓고 맞게 적어야 수정 가능하게 바꾸기
        mypage_etx_registerPwdCon.setText(user.getUserpwd()); // 나중에는 비워놓고 맞게 적어야 수정 가능하게 바꾸기
        mypage_etx_registerName.setText(user.getUsername());
        mypage_etx_registerPhone1.setText(user.getUserphone().substring(0,3));
        mypage_etx_registerPhone2.setText(user.getUserphone().substring(3,7));
        mypage_etx_registerPhone3.setText(user.getUserphone().substring(7,11));
        mypage_etx_registerBirth.setText(user.getUserbirth().toString()); // 수정 필요
    }
}