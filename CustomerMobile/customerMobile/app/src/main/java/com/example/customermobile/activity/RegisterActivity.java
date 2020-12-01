package com.example.customermobile.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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

import com.example.customermobile.R;
import com.example.customermobile.network.HttpConnect;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class RegisterActivity extends AppCompatActivity {
    HttpAsyncTask httpAsyncTask;

    ActionBar actionBar;
    EditText edit_id, edit_pwd, edit_pwdcon, edit_name, edit_phone1, edit_phone2, edit_phone3, edit_birth;
    TextView textView_pwdcheck;
    RadioGroup radioGroup_sex;

    // id check 확인
    Boolean idcheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // ActionBar Setting
        actionBar = getSupportActionBar();
        actionBar.hide();

        idcheck = false;

        edit_id = findViewById(R.id.editText_registerId);
        edit_pwd = findViewById(R.id.editText_registerPwd);
        edit_pwdcon = findViewById(R.id.editText_registerPwdCon);
        edit_name = findViewById(R.id.editText_registerName);
        edit_phone1 = findViewById(R.id.editText_registerPhone1);
        edit_phone2 = findViewById(R.id.editText_registerPhone2);
        edit_phone3 = findViewById(R.id.editText_registerPhone3);
        edit_birth = findViewById(R.id.editText_registerBirth);
        radioGroup_sex = findViewById(R.id.radioGroup_registerSex);


        textView_pwdcheck = findViewById(R.id.textView_registerPwdCheck);
        edit_pwdcon.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String pwd = edit_pwd.getText().toString();
                String pwdcon = edit_pwdcon.getText().toString();

                if(pwd.equals(pwdcon)){
                    textView_pwdcheck.setText("");
                }else {
                    textView_pwdcheck.setText("비밀번호가 일치하지 않습니다");
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
                int id = radioGroup_sex.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) findViewById(id);

                String uid = edit_id.getText().toString();
                String pwd = edit_pwd.getText().toString();
                String pwdcon = edit_pwdcon.getText().toString();
                String name = edit_name.getText().toString();
                String phone = edit_phone1.getText().toString() + edit_phone2.getText().toString() + edit_phone3.getText().toString();
                String birth = edit_birth.getText().toString();
                // 토큰 임시 데이터
                String token = "token1";
                // 성별데이터 DB 규약에 맞게 변환
                String sex = "";
                if (rb.getText().toString().equals("여")) {
                    sex = "f";
                } else if (rb.getText().toString().equals("남")) {
                    sex = "m";
                }

                if (pwd.equals(pwdcon)) {
                    String url = "http://192.168.0.112/webServer/userregisterimpl.mc";
                    url += "?id=" + uid + "&pwd=" + pwd + "&name=" + name + "&sex=" + sex + "&phone=" + phone + "&birth=" + birth + "&token=" + token;
                    httpAsyncTask = new HttpAsyncTask();
                    httpAsyncTask.execute(url);
                }else {
                    android.app.AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setTitle("회원가입");
                    builder.setMessage("비밀번호가 일치하지 않습니다");

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            edit_pwdcon.requestFocus();
                        }
                    });

                    builder.show();
                }
            }else {
                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle("회원가입");
                builder.setMessage("아이디 중복 확인을 해주십시오");

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
            String uid = edit_id.getText().toString();

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
                            edit_birth.setText(String.format("%d/%d/%d", year, monthOfYear + 1, dayOfMonth));
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
            }else if(result.equals("cannot")){
                // id 사용 불가
                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle("아이디 중복 확인");
                builder.setMessage("이미 사용중인 아이디입니다.");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        idcheck = false;
                        edit_id.setText("");
                    }
                });

                builder.show();
            }else if(result.equals("checkfail")){
                // 회원 조회 실패
                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle("ERROR");
                builder.setMessage("아이디 조회에 실패하였습니다. 다시 시도해주십시오.");

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
                builder.setTitle("ERROR");
                builder.setMessage("회원가입에 실패하였습니다. 다시 시도해주십시오.");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.show();

            }else if(result.equals("success")){
                // 회원가입 성공
                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                builder.setTitle("회원가입");
                builder.setMessage("회원가입되었습니다. 해당 아이디로 로그인해주세요.");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });

                builder.show();

            }
        }
    }
    // End HTTP 통신 Code
}