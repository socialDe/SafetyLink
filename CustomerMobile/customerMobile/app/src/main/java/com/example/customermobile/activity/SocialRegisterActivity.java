package com.example.customermobile.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.customermobile.R;
import com.example.customermobile.network.HttpConnect;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.example.customermobile.activity.LoginActivity.ip;

public class SocialRegisterActivity extends AppCompatActivity {

    HttpAsyncTask httpAsyncTask;

    SharedPreferences sptoken;

    ActionBar actionBar;
    EditText edit_registername, edit_registerphone1, edit_registerphone2, edit_registerphone3, edit_registerbirth;
    TextView textView_registerpwdcheck;
    RadioGroup radioGroup_registersex;

    Boolean idcheck;  // id check 확인

    String googleEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_register);

        Toast.makeText(SocialRegisterActivity.this, "구글정보를 받았습니다!\n간단 회원가입을 해주세요!", Toast.LENGTH_LONG).show();

        Intent intent = getIntent();
        googleEmail = intent.getStringExtra("email");

//        // ActionBar Setting
//        actionBar = getSupportActionBar();
//        actionBar.hide();

        // 디바이스 토큰 정보 가져오기
        sptoken = getSharedPreferences("applicaton",MODE_PRIVATE);

        idcheck = false;

        edit_registername = findViewById(R.id.editText_registerName);
        edit_registerphone1 = findViewById(R.id.editText_registerPhone1);
        edit_registerphone2 = findViewById(R.id.editText_registerPhone2);
        edit_registerphone3 = findViewById(R.id.editText_registerPhone3);
        edit_registerbirth = findViewById(R.id.editText_registerBirth);
        radioGroup_registersex = findViewById(R.id.radioGroup_registerSex);

    }

    public void clickbt(View v) {
        // 버튼 클릭시 키보드 숨김
        //InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        //manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        if (v.getId() == R.id.button_registerOk) {

            int id = radioGroup_registersex.getCheckedRadioButtonId();
            RadioButton rb = (RadioButton) findViewById(id);

            String uid = googleEmail;
            String pwd = "google";
            String pwdcon = "google";
            String name = edit_registername.getText().toString();
            String phone = edit_registerphone1.getText().toString() + edit_registerphone2.getText().toString() + edit_registerphone3.getText().toString();
            String birth = edit_registerbirth.getText().toString();
            String mobiletoken = sptoken.getString("token", "");
            // 성별데이터 DB 규약에 맞게 변환
            String sex = "";
            if (rb.getText().toString().equals("여")) {
                sex = "f";
            } else if (rb.getText().toString().equals("남")) {
                sex = "m";
            }

            String url = "http://"+ip+"/webServer/userregisterimpl.mc";
            url += "?id=" + uid + "&pwd=" + pwd + "&name=" + name + "&sex=" + sex + "&phone=" + phone + "&birth=" + birth + "&token=" + mobiletoken;
            httpAsyncTask = new HttpAsyncTask();
            httpAsyncTask.execute(url);

        } else if (v.getId() == R.id.button_registerCancel) {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else if (v.getId() == R.id.button_registerBirth) {
            //현재 날짜 출력
            Calendar cal = new GregorianCalendar();
            int mYear = cal.get(Calendar.YEAR);
            int mMonth = cal.get(Calendar.MONTH);
            int mDay = cal.get(Calendar.DAY_OF_MONTH);

            //DatePiker를 통해 날짜 받아오기
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                            edit_registerbirth.setText(String.format("%d/%d/%d", year, monthOfYear + 1, dayOfMonth));
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
    }

    /*
    HTTP 통신 Code
    */
    class HttpAsyncTask extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(SocialRegisterActivity.this);
            progressDialog.setTitle("Register ...");
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
            if (result.equals("fail")) {
                // 회원가입 실패
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("toast", "loginfail");
                startActivity(intent);

            } else if (result.equals("success")) {
                // 회원가입 성공
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("toast", "loginok");
                startActivity(intent);

            }
        }
    }
    // End HTTP 통신 Code
}