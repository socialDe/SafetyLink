package com.example.customermobile.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.customermobile.R;
import com.example.customermobile.network.HttpConnect;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class RegisterActivity extends AppCompatActivity {
    HttpAsyncTask httpAsyncTask;
    SharedPreferences sptoken;

    ActionBar actionBar;
    EditText edit_registerid, edit_registerpwd, edit_registerpwdcon, edit_registername, edit_registerphone1, edit_registerphone2, edit_registerphone3, edit_registerbirth;
    TextView textView_registerpwdcheck;
    RadioGroup radioGroup_registersex;

    Boolean idcheck;  // id check 확인

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // ActionBar Setting
        actionBar = getSupportActionBar();
        actionBar.hide();

        // 디바이스 토큰 정보 가져오기
        sptoken = getSharedPreferences("applicaton",MODE_PRIVATE);
        idcheck = false;

        edit_registerid = findViewById(R.id.editText_registerId);
        edit_registerpwd = findViewById(R.id.editText_registerPwd);
        edit_registerpwdcon = findViewById(R.id.editText_registerPwdCon);
        edit_registername = findViewById(R.id.editText_registerName);
        edit_registerphone1 = findViewById(R.id.editText_registerPhone1);
        edit_registerphone2 = findViewById(R.id.editText_registerPhone2);
        edit_registerphone3 = findViewById(R.id.editText_registerPhone3);
        edit_registerbirth = findViewById(R.id.editText_registerBirth);
        radioGroup_registersex = findViewById(R.id.radioGroup_registerSex);

        textView_registerpwdcheck = findViewById(R.id.textView_registerPwdCheck);
        edit_registerpwdcon.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String pwd = edit_registerpwd.getText().toString();
                String pwdcon = edit_registerpwdcon.getText().toString();

                if(pwd.equals(pwdcon)){
                    textView_registerpwdcheck.setText("");
                }else {
                    textView_registerpwdcheck.setText("비밀번호가 일치하지 않습니다");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    public void clickbt(View v) {
        // 버튼 클릭시 키보드 숨김
        InputMethodManager manager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        if(v.getId() == R.id.button_registerOk) {
            // 아이디 중복 확인을 하였을 경우에만 회원가입 가능
            if(idcheck) {
                int id = radioGroup_registersex.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) findViewById(id);

                String uid = edit_registerid.getText().toString();
                String pwd = edit_registerpwd.getText().toString();
                String pwdcon = edit_registerpwdcon.getText().toString();
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

                if (pwd.equals(pwdcon)) {
//                    String url = "http://192.168.219.110/webServer/userregisterimpl.mc";
                    String url = "http://192.168.0.112/webServer/userregisterimpl.mc";
                    url += "?id=" + uid + "&pwd=" + pwd + "&name=" + name + "&sex=" + sex + "&phone=" + phone + "&birth=" + birth + "&token=" + mobiletoken;
                    httpAsyncTask = new HttpAsyncTask();
                    httpAsyncTask.execute(url);
                }else {
                    android.app.AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setTitle("비밀번호가 일치하지 않습니다.");

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            edit_registerpwdcon.requestFocus();
                        }
                    });

                    builder.show();
                }
            }else {
                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle("아이디 중복 확인을 해주십시오.");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Button button_idcheck = findViewById(R.id.button_registerIdCheck);
                        button_idcheck.setFocusableInTouchMode(true);
                        button_idcheck.requestFocus();
                    }
                });

                builder.show();
            }
        }else if (v.getId() == R.id.button_registerCancel){
            // 회원가입 취소
            finish();
        }else if (v.getId() == R.id.button_registerIdCheck){
            // 아이디 중복 확인
            String uid = edit_registerid.getText().toString();

//            String url = "http://192.168.219.110/webServer/useridcheckimpl.mc";
            String url = "http://192.168.0.112/webServer/useridcheckimpl.mc";
            url += "?id=" + uid;
            httpAsyncTask = new HttpAsyncTask();
            httpAsyncTask.execute(url);
        }else if (v.getId() == R.id.button_registerBirth){
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
    class HttpAsyncTask extends AsyncTask<String,String,String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(RegisterActivity.this);
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
            if (result.equals("available")) {
                // id 사용 가능
                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle("아이디 중복 확인");
                builder.setMessage("사용 가능합니다.");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        idcheck = true;
                    }
                });

                builder.show();
            }else if(result.equals("cannot id")){
                // id 사용 불가
                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle("아이디 중복 확인");
                builder.setMessage("이미 사용중인 아이디입니다.");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        idcheck = false;
                        edit_registerid.setText("");
                    }
                });

                builder.show();
            }else if(result.equals("checkfail")){
                // 회원 조회 실패
                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle("아이디 조회에 실패하였습니다.");
                builder.setMessage("다시 시도해 주십시오.");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        idcheck = false;
                    }
                });

                builder.show();

            }else if(result.equals("fail")){
                // 회원가입 실패
                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle("회원가입에 실패하였습니다.");
                builder.setMessage("다시 시도해 주십시오.");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.show();

            }else if(result.equals("success")){
                // 회원가입 성공
                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle("회원가입되었습니다.");
                builder.setMessage("해당 아이디로 로그인해십시오.");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });

                builder.show();

            }else if(result.equals("cannot user")){
                // 이미 가입된 회원(username, userphone으로 판단)
                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle("이미 가입된 회원입니다.");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        edit_registerpwd.setText("");
                        edit_registerpwdcon.setText("");
                        edit_registername.requestFocus();
                    }
                });

                builder.show();
            }
        }
    }
    // End HTTP 통신 Code
}