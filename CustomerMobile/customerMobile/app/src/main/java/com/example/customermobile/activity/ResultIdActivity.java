package com.example.customermobile.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.customermobile.R;

public class ResultIdActivity extends AppCompatActivity {

    EditText edit_resultId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_id);

        edit_resultId = findViewById(R.id.editText_resultId);

        Intent intent = getIntent();
        String userid = intent.getStringExtra("userid");

        edit_resultId.setText(userid);
    }

    public void clickbt(View v){
        if(v.getId() == R.id.button_idToLogin){
            // 액티비티 기록 없이 로그인 화면으로 전환
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }
}