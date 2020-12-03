package com.example.customermobile.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.customermobile.R;
import com.example.customermobile.network.HttpConnect;
import com.vo.UsersVO;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FindIdActivity extends AppCompatActivity {
    HttpAsyncTask httpAsyncTask;

    EditText edit_idFindName, edit_idFindPhone1, edit_idFindPhone2, edit_idFindPhone3, edit_idFindNumber;
    TextView textView_idFindNumber;

    Boolean findnumber; // id check 확인

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_id);

        findnumber = false;

        edit_idFindName = findViewById(R.id.editText_idFindName);
        edit_idFindPhone1 = findViewById(R.id.editText_idFindPhone1);
        edit_idFindPhone2 = findViewById(R.id.editText_idFindPhone2);
        edit_idFindPhone3 = findViewById(R.id.editText_idFindPhone3);
        edit_idFindNumber = findViewById(R.id.editText_idFindNumber);
        textView_idFindNumber = findViewById(R.id.textView_idFindNumber);

        // editText의 값이 변경될 경우 인증 확인 변경
        edit_idFindName.addTextChangedListener(new TextWatcher() {
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

        edit_idFindPhone1.addTextChangedListener(new TextWatcher() {
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
        edit_idFindPhone2.addTextChangedListener(new TextWatcher() {
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
        edit_idFindPhone3.addTextChangedListener(new TextWatcher() {
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

        if(v.getId() == R.id.button_idFindOk){
            if(findnumber){
                // editText -> String
                String name = edit_idFindName.getText().toString();
                String phone = edit_idFindPhone1.getText().toString() + edit_idFindPhone2.getText().toString() + edit_idFindPhone3.getText().toString();

                // HTTP URL Connected
//                String url = "http://192.168.219.110/webServer/useridfindimpl.mc";
                String url = "http://192.168.0.112/webServer/useridfindimpl.mc";
                url += "?name="+ name +"&phone=" + phone;
                httpAsyncTask = new HttpAsyncTask();
                httpAsyncTask.execute(url);
            }else {
                // 본인확인 인증을 하지 않았을 경우
                AlertDialog.Builder builder = new AlertDialog.Builder(FindIdActivity.this);
                builder.setTitle("아이디 찾기");
                builder.setMessage("인증번호를 확인해주십시오");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        edit_idFindNumber.requestFocus();
                    }
                });
                builder.show();
            }
        }else if(v.getId() == R.id.button_idFindCancel){
            // 아이디 찾기 취소
            // 액티비티 종료
            finish();
        }else if(v.getId() == R.id.button_idFindNumber){
            findnumber = true;
            textView_idFindNumber.setText("인증되었습니다");
        }
    }

    public void checkNumber(){
        if(findnumber) {
            findnumber = false;
            textView_idFindNumber.setText("");
        }
    }

    /*
    HTTP 통신 Code
    */
    class HttpAsyncTask extends AsyncTask<String,String,String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            Log.d("[Log]", "preExecute");
            progressDialog = new ProgressDialog(FindIdActivity.this);
            progressDialog.setTitle("아이디 찾기");
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
                AlertDialog.Builder dailog = new AlertDialog.Builder(FindIdActivity.this);
                dailog.setTitle("아이디 찾기");
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
                AlertDialog.Builder dailog = new AlertDialog.Builder(FindIdActivity.this);
                dailog.setTitle("ERROR");
                dailog.setMessage("조회에 실패하였습니다. 다시 시도해 주십시오.");
                dailog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                dailog.show();
            } else {
                // 아이디 정보를 가지고 액티비티 전환
                Intent intent = new Intent(getApplicationContext(), ResultIdActivity.class);
                intent.putExtra("userid", result);
                startActivity(intent);
            }
        }
    }
    /*
    End HTTP 통신 Code
     */
}