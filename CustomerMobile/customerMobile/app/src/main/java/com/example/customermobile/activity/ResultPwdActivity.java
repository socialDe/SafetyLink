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
import android.widget.EditText;
import android.widget.TextView;

import com.example.customermobile.R;
import com.example.customermobile.network.HttpConnect;

import static com.example.customermobile.activity.LoginActivity.ip;

public class ResultPwdActivity extends AppCompatActivity {
    HttpAsyncTask httpAsyncTask;

    EditText edit_pwdResultId, edit_pwdResultPwd, edit_pwdResultPwdCon;
    TextView textView_pwdResultPwdcheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_pwd);

        Intent intent = getIntent();
        String userid = intent.getStringExtra("userid");

        edit_pwdResultId = findViewById(R.id.editText_pwdResultId);
        edit_pwdResultPwd = findViewById(R.id.editText_pwdResultPwd);
        edit_pwdResultPwdCon = findViewById(R.id.editText_pwdResultPwdCon);
        textView_pwdResultPwdcheck = findViewById(R.id.textView_pwdResultPwdCheck);
        edit_pwdResultId.setText(userid);
        edit_pwdResultId.setText(userid);

        edit_pwdResultPwdCon.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String pwd = edit_pwdResultPwd.getText().toString();
                String pwdcon = edit_pwdResultPwdCon.getText().toString();

                if(pwdcon.equals(pwd)){
                    textView_pwdResultPwdcheck.setText("");
                }else {
                    textView_pwdResultPwdcheck.setText("비밀번호가 일치하지 않습니다");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void clickbt(View v){
        if(v.getId() == R.id.button_pwdResultOk){
            String id = edit_pwdResultId.getText().toString();
            String pwd = edit_pwdResultPwd.getText().toString();
            String pwdcon = edit_pwdResultPwdCon.getText().toString();
            if(pwdcon.equals(pwd)){
                // HTTP URL Connected
<<<<<<< HEAD
                String url = "http://"+ip+"/webServer/userpwdchangeimpl.mc";
=======
//                String url = "http://192.168.219.110/webServer/userpwdchangeimpl.mc";
                String url = "http://192.168.0.112/webServer/userpwdchangeimpl.mc";
>>>>>>> feature/mobile_login
                url += "?id="+ id + "&pwd="+ pwd + "&pwdcon=" + pwdcon;
                httpAsyncTask = new HttpAsyncTask();
                httpAsyncTask.execute(url);
            }else {
                // 비밀번호가 다를 경우
                AlertDialog.Builder builder = new AlertDialog.Builder(ResultPwdActivity.this);
                builder.setTitle("비밀번호 찾기");
                builder.setMessage("비밀번호가 일치하지 않습니다");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        edit_pwdResultPwdCon.requestFocus();
                    }
                });
                builder.show();
            }
        }else if(v.getId() == R.id.button_pwdResultCancel){
            // 비밀번호 변경 취소
            // 기록 없이 로그인 액티비티로 전환
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }


    /*
    HTTP 통신 Code
    */
    class HttpAsyncTask extends AsyncTask<String,String,String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ResultPwdActivity.this);
            progressDialog.setTitle("비밀번호 변경");
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

            if (result.equals("changesuccess")) {
                // 비밀번호 변경 완료
                AlertDialog.Builder dailog = new AlertDialog.Builder(ResultPwdActivity.this);
                dailog.setTitle("비밀번호 찾기");
                dailog.setMessage("비밀번호가 변경되었습니다");
                dailog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        return;
                    }
                });
                dailog.show();
            } else if (result.equals("changefail")) {
                // 비밀번호 변경 실패: Exception
                AlertDialog.Builder dailog = new AlertDialog.Builder(ResultPwdActivity.this);
                dailog.setTitle("ERROR");
                dailog.setMessage("변경에 실패하였습니다. 다시 시도해 주십시오.");
                dailog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                dailog.show();
            }
        }
    }
    /*
    End HTTP 통신 Code
     */
}