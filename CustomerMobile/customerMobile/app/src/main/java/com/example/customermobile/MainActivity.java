package com.example.customermobile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.owl93.dpb.CircularProgressView;
import com.skydoves.progressview.OnProgressChangeListener;
import com.skydoves.progressview.ProgressView;

import java.util.Random;

import www.sanju.motiontoast.MotionToast;

public class MainActivity extends AppCompatActivity {

    CircularProgressView circularProgressView;
    ProgressView progressView;
    EditText etx_id;
    EditText etx_pwd;
    Button loginButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        circularProgressView = findViewById(R.id.circularProgressView);
        circularProgressView.setProgress(0);
        circularProgressView.setMaxValue(100);

        //id, pwd for test
        etx_id = findViewById(R.id.etx_id);
        etx_pwd = findViewById(R.id.etx_pwd);
        loginButton = findViewById(R.id.loginButton);

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
        }
    } // end clickbt



}