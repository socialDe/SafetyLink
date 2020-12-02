package com.example.customermobile.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.customermobile.R;
import com.example.customermobile.network.HttpConnect;

public class FindPwdActivity extends AppCompatActivity {
    HttpAsyncTask httpAsyncTask;

    EditText edit_pwdFindId, edit_pwdFIndName, edit_pwdFindphone1, edit_pwdFindphone2, edit_pwdFindphone3, edit_pwdFindNumber;
    TextView textView_pwdFindNumber;

    Boolean findnumber; // pwd check 확인

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pwd);

        findnumber = false;

        edit_pwdFindId = findViewById(R.id.editText_pwdFindId);
        edit_pwdFIndName = findViewById(R.id.editText_pwdFindName);
        edit_pwdFindphone1 = findViewById(R.id.editText_pwdFindPhone1);
        edit_pwdFindphone2 = findViewById(R.id.editText_pwdFindPhone2);
        edit_pwdFindphone3 = findViewById(R.id.editText_pwdFindPhone3);
        edit_pwdFindNumber = findViewById(R.id.editText_pwdFindNumber);
        textView_pwdFindNumber = findViewById(R.id.textView_pwdFindNumber);

        edit_pwdFindId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkNumber();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edit_pwdFIndName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkNumber();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edit_pwdFindphone1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkNumber();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edit_pwdFindphone2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkNumber();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edit_pwdFindphone3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                checkNumber();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void clickbt(View v){
        // 버튼 클릭시 키보드 숨김
        InputMethodManager manager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        if(v.getId() == R.id.button_pwdFindOk){
            if(findnumber){
                // editText -> String
                String id = edit_pwdFindId.getText().toString();
                String name = edit_pwdFIndName.getText().toString();
                String phone = edit_pwdFindphone1.getText().toString() + edit_pwdFindphone2.getText().toString() + edit_pwdFindphone3.getText().toString();

                // HTTP URL Connected
                String url = "http://192.168.219.110/webServer/userpwdfindimpl.mc";
                url += "?id="+ id + "&name="+ name + "&phone=" + phone;
                httpAsyncTask = new HttpAsyncTask();
                httpAsyncTask.execute(url);
            }else {
                // 본인확인 인증을 하지 않았을 경우
                AlertDialog.Builder builder = new AlertDialog.Builder(FindPwdActivity.this);
                builder.setTitle("아이디 찾기");
                builder.setMessage("인증번호를 확인해주십시오");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        edit_pwdFindNumber.requestFocus();
                    }
                });
                builder.show();
            }
        }else if(v.getId() == R.id.button_pwdFindCancel){
            // 비밀번호 찾기 취소
            // 액티비티 종료
            finish();
        }else if(v.getId() == R.id.button_pwdFindNumber){
            findnumber = true;
            textView_pwdFindNumber.setText("인증되었습니다");
        }
    }

    public void checkNumber(){
        if(findnumber) {
            findnumber = false;
            textView_pwdFindNumber.setText("");
        }
    }

    /*
    HTTP 통신 Code
    */
    class HttpAsyncTask extends AsyncTask<String,String,String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(FindPwdActivity.this);
            progressDialog.setTitle("비밀번호 찾기");
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

            if (result.equals("noun")) {
                // 회원정보 없음
                AlertDialog.Builder dailog = new AlertDialog.Builder(FindPwdActivity.this);
                dailog.setTitle("비밀번호 찾기");
                dailog.setMessage("가입된 회원이 아닙니다");
                dailog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                dailog.show();
            } else if (result.equals("findfail")) {
                // 아이디 조회 실패: Exception
                AlertDialog.Builder dailog = new AlertDialog.Builder(FindPwdActivity.this);
                dailog.setTitle("ERROR");
                dailog.setMessage("조회에 실패하였습니다. 다시 시도해 주십시오.");
                dailog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                dailog.show();
            } else if (result.equals("findsuccess")) {
                // 아이디 정보를 가지고 액티비티 전환
                Intent intent = new Intent(getApplicationContext(), ResultPwdActivity.class);
                intent.putExtra("userid", edit_pwdFindId.getText().toString());
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }
    }
    /*
    End HTTP 통신 Code
     */
}