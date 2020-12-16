package com.example.customermobile.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.customermobile.R;
import com.example.customermobile.network.HttpConnect;
import com.example.customermobile.vo.CarVO;
import com.example.customermobile.vo.UsersVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import static com.example.customermobile.activity.LoginActivity.ip;

public class MyPageActivity extends AppCompatActivity {

    TextView mypage_tx_registerId, mypage_tx_registerPwdCheck;
    EditText mypage_etx_registerPwd, mypage_etx_registerPwdCon, mypage_etx_registerName,
            mypage_etx_registerPhone1, mypage_etx_registerPhone2, mypage_etx_registerPhone3, mypage_etx_registerBirth;
    RadioGroup mypage_rg_registerSex;
    RadioButton mypage_rb_registerMan, mypage_rb_registerWoman;
    ImageButton imageButton_back3;

    UsersVO user;

    ModifyAsync modifyAsync;
    UserAsync userAsync;
    DeleteAsync deleteAsync;

    Button mypage_bt_modifyx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);
//        int id = mypage_rg_registerSex.getCheckedRadioButtonId();
//        RadioButton rb = (RadioButton) findViewById(id);

        mypage_tx_registerId = findViewById(R.id.mypage_tx_registerId);
        mypage_tx_registerPwdCheck = findViewById(R.id.mypage_tx_registerPwdCheck);

        mypage_etx_registerPwd = findViewById(R.id.mypage_etx_registerPwd);
        mypage_etx_registerPwdCon = findViewById(R.id.mypage_etx_registerPwdCon);
        mypage_etx_registerPwdCon.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String pwd = mypage_etx_registerPwd.getText().toString();
                String pwdcon = mypage_etx_registerPwdCon.getText().toString();

                if(pwd.equals(pwdcon)){
                    mypage_tx_registerPwdCheck.setText("");
                }else {
                    mypage_tx_registerPwdCheck.setText("비밀번호가 일치하지 않습니다");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mypage_etx_registerName = findViewById(R.id.mypage_etx_registerName);
        mypage_etx_registerPhone1 = findViewById(R.id.mypage_etx_registerPhone1);
        mypage_etx_registerPhone2 = findViewById(R.id.mypage_etx_registerPhone2);
        mypage_etx_registerPhone3 = findViewById(R.id.mypage_etx_registerPhone3);
        mypage_etx_registerBirth = findViewById(R.id.mypage_etx_registerBirth);

        mypage_rg_registerSex = findViewById(R.id.mypage_rg_registerSex);

        mypage_rb_registerMan = findViewById(R.id.mypage_rb_registerMan);
        mypage_rb_registerWoman = findViewById(R.id.mypage_rb_registerWoman);

        mypage_bt_modifyx = findViewById(R.id.mypage_bt_modifyx);
        mypage_bt_modifyx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent getintent = getIntent();
        user = (UsersVO) getintent.getSerializableExtra("user");
        getUserData();
        Log.d("[Server]",user.toString());

        mypage_tx_registerId.setText(user.getUserid()); // 수정 불가능하도록 변경(UI 문제로 일단 보류)
        mypage_etx_registerPwd.setText(user.getUserpwd()); // 나중에는 비워놓고 맞게 적어야 수정 가능하게 바꾸기
        mypage_etx_registerPwdCon.setText(user.getUserpwd()); // 나중에는 비워놓고 맞게 적어야 수정 가능하게 바꾸기
        mypage_etx_registerName.setText(user.getUsername());
        mypage_etx_registerPhone1.setText(user.getUserphone().substring(0,3));
        mypage_etx_registerPhone2.setText(user.getUserphone().substring(3,7));
        mypage_etx_registerPhone3.setText(user.getUserphone().substring(7,11));

        mypage_rg_registerSex.clearCheck();
        if(user.getUsersex().equals("m")){
            mypage_rb_registerMan.setChecked(true);
        } else if(user.getUsersex().equals("f")){
            mypage_rb_registerWoman.setChecked(true);
        }

        imageButton_back3 = findViewById(R.id.imageButton_back3);
        imageButton_back3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // 날짜 형식에 맞게 변환
        SimpleDateFormat sdf = new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        try {
            Date userbirth = sdf.parse(user.getUserbirth().toString());
            SimpleDateFormat newsdf = new SimpleDateFormat("yyyy/MM/dd");
            String newbirth = newsdf.format(userbirth);
            Log.d("[Server]", newbirth);
            mypage_etx_registerBirth.setText(newbirth);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        modifyAsync = new ModifyAsync();
        userAsync = new UserAsync();
        deleteAsync = new DeleteAsync();
        getUserData();
    } // onCreate

    public void dateBt(View v){
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
                        mypage_etx_registerBirth.setText(String.format("%d/%d/%d", year, monthOfYear + 1, dayOfMonth));
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
    }

    public void modifyBt(View v) {
        int id = mypage_rg_registerSex.getCheckedRadioButtonId();
        RadioButton rb = (RadioButton) findViewById(id);

        String uid = mypage_tx_registerId.getText().toString();
        String pwd = mypage_etx_registerPwd.getText().toString();
        String pwdcon = mypage_etx_registerPwdCon.getText().toString();
        String name = mypage_etx_registerName.getText().toString();
        String phone = mypage_etx_registerPhone1.getText().toString() + mypage_etx_registerPhone2.getText().toString() + mypage_etx_registerPhone3.getText().toString();
        String birth = mypage_etx_registerBirth.getText().toString();


        // 성별데이터 DB 규약에 맞게 변환
        String sex = "";
        if (rb.getText().toString().equals("여")) {
            sex = "f";
        } else if (rb.getText().toString().equals("남")) {
            sex = "m";
        }

        if (pwd.equals(pwdcon)) {
            String url = "http://" + ip + "/webServer/usermodifyimpl.mc";
            url += "?id=" + uid + "&pwd=" + pwd + "&name=" + name + "&sex=" + sex + "&phone=" + phone + "&birth=" + birth;
            modifyAsync = new ModifyAsync();
            Log.d("[Server]","url:"+url);
            modifyAsync.execute(url);
        }
    }

        class ModifyAsync extends AsyncTask<String, Void, Void> {

            public Void result;

            @Override
            protected Void doInBackground(String... strings) {
                String url = strings[0];
                HttpConnect.getString(url); //result는 JSON
                return result;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
//                MyPageActivity myPageActivity = MyPageActivity.this;
//                myPageActivity.finish();

                Intent intent = new Intent(getApplicationContext(), MyPageActivity.class);
                getUserData();
                intent.putExtra("user",user);
                startActivity(intent);
                finish();
            }
        }

    public void getUserData() {
        // URL 설정.
        String url = "http://" + ip + "/webServer/userdata.mc?id=" + user.getUserid();

        // AsyncTask를 통해 HttpURLConnection 수행.
        userAsync = new UserAsync();
        userAsync.execute(url);
    }

    class UserAsync extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0];
            String result = HttpConnect.getString(url); //result는 JSON
            Log.d("[TAG]", "result = "+result);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {

            Log.d("[TEST]", "s:" + s);

            // userid가 car를 갖고있지 않으면 차량을 등록하는 화면으로 넘긴다
            // 차별 차정보를 저장한다
            JSONArray ja = null;
            try {
                ja = new JSONArray(s);
                for(int i = 0; i < ja.length(); i++){
                JSONObject jo = ja.getJSONObject(i);

                String pwd = jo.getString("pwd");
                user.setUserpwd(pwd);
                mypage_etx_registerPwd.setText(pwd);
                String name = jo.getString("name");
                user.setUsername(name);
                mypage_etx_registerName.setText(name);
                String sex = jo.getString("sex");
                user.setUsersex(sex);
                mypage_rg_registerSex.clearCheck();
                if(user.getUsersex().equals("m")){
                    mypage_rb_registerMan.setChecked(true);
                } else if(user.getUsersex().equals("f")){
                    mypage_rb_registerWoman.setChecked(true);
                }
                String phone = jo.getString("phone");
                user.setUserphone(phone);
                mypage_etx_registerPhone1.setText(phone.substring(0,3));
                mypage_etx_registerPhone2.setText(phone.substring(3,7));
                mypage_etx_registerPhone3.setText(phone.substring(7,11));
                String birth = jo.getString("birth");
        // 날짜 형식에 맞게 변환
                SimpleDateFormat sdf = new SimpleDateFormat("yy/mm/dd", Locale.ENGLISH);
                try {
                    mypage_etx_registerBirth.setText(birth);
                    Date userbirth = sdf.parse(birth);
                    user.setUserbirth(userbirth);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.d("[Server]", "get 안 : "+user);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void deleteBt(View v){

    String url = "http://" + ip + "/webServer/userdeleteimpl.mc?id="+user.getUserid();
    deleteAsync = new DeleteAsync();
    deleteAsync.execute(url);
}


class DeleteAsync extends AsyncTask<String, Void, Void> {

    public Void result;

    @Override
    protected Void doInBackground(String... strings) {
        String url = strings[0];
        HttpConnect.getString(url); //result는 JSON
        return result;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
}