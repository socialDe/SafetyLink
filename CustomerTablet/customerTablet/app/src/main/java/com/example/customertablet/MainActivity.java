package com.example.customertablet;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.iid.FirebaseInstanceId;

public class MainActivity extends AppCompatActivity {
    HttpAsyncTask httpAsyncTask;
    ActionBar actionBar;
    EditText editText_carNum, editText_carYear, editText_carModel, editText_carType, editText_oilType;
    Button button_carRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText_carNum = findViewById(R.id.editText_carNum);
        editText_carYear = findViewById(R.id.editText_carYear);
        editText_carModel = findViewById(R.id.editText_carModel);
        editText_carType = findViewById(R.id.editText_carType);
        editText_oilType = findViewById(R.id.editText_oilType);
        button_carRegister = findViewById(R.id.button_carRegister);
    }

    public void clickbt(View v){
        String num = editText_carNum.getText().toString();
        String year = editText_carYear.getText().toString();
        String model = editText_carModel.getText().toString();
        String carType = editText_carType.getText().toString();
        String oilType = editText_oilType.getText().toString();
        String token = FirebaseInstanceId.getInstance().getId();

        String url = "http://192.168.25.35/webServer/carregisterimpl.mc";
        url += "?num=" + num + "&year=" + year + "&model=" + model + "&cartype=" + carType + "&oilType=" + oilType + "&token=" + token;
        httpAsyncTask = new HttpAsyncTask();
        httpAsyncTask.execute(url);
    }

    /*
    HTTP 통신 Code
    */
    class HttpAsyncTask extends AsyncTask<String,String,String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("Register ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0].toString();
            String result = com.example.customertablet.network.HttpConnect.getString(url);
            return result;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            //String result = s.trim();
            if(s.equals("fail")){
                // 차량 등록 실패
                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("ERROR");
                builder.setMessage("차량 등록에 실패하였습니다. 다시 시도해주십시오.");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();

            }else if(s.equals("success")){
                // 차량 등록 성공
                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("차량 등록");
                builder.setMessage("차량이 등록되었습니다.");

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