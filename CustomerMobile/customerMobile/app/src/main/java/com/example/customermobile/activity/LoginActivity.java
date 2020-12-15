package com.example.customermobile.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.customermobile.R;
import com.example.customermobile.network.HttpConnect;

import com.example.customermobile.vo.CarVO;
import com.example.customermobile.vo.UsersVO;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LoginActivity extends AppCompatActivity {

    public static String ip = "192.168.0.103";

    HttpAsyncTask httpAsyncTask; // HTTP 전송 데이터
    SharedPreferences sp; // 자동 로그인
    UsersVO user; // user 객체
    SharedPreferences sptoken; // token
    String token; // token 값 저장

    ActionBar actionBar;
    EditText editText_loginid, editText_loginpwd;
    CheckBox checkBox_loginauto;

    String email = "";

    int checkcar = 0;

    // 소셜로그인을 위한 변수
    private FirebaseAuth mAuth = null;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private SignInButton signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 앱 실행시 디바이스 토큰 불러오기
        getToken();

        sp = getSharedPreferences("user", MODE_PRIVATE);
        sptoken = getSharedPreferences("applicaton", MODE_PRIVATE);
        token = sptoken.getString("token", "");


        // 인텐트로 string을 받아 toast를 띄워주는 부분
        Intent intent = getIntent();
        String getintent = intent.getStringExtra("toast");
        if (getintent != null) {
            if (getintent.equals("loginok")) {
                Toast.makeText(LoginActivity.this, "회원가입되었습니다. 해당 아이디로 로그인해주세요.", Toast.LENGTH_SHORT).show();
            } else if (getintent.equals("loginfail")) {
                Toast.makeText(LoginActivity.this, "회원가입에 실패하였습니다. 다시 시도해주십시오.", Toast.LENGTH_LONG).show();
            }
        }


        editText_loginid = findViewById(R.id.editText_loginId);
        editText_loginpwd = findViewById(R.id.editText_loginPwd);
        checkBox_loginauto = findViewById(R.id.checkBox_loginAuto);


        String userid = sp.getString("userid", "");
        String userpwd = sp.getString("userpwd", "");

        if (userid != null && userid != "" && userpwd != null && userpwd != "") {
            login(userid, userpwd);
        }

        // 소셜로그인
        signInButton = findViewById(R.id.signInButton);
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

//        if (mAuth.getCurrentUser() != null) {
//            login(email,"google");
//        }
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

    }

    // 소셜로그인
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            Log.d("[TEST]", "google sign in success");
                        } else {
                            // If sign in fails, display a message to the user.
                            updateUI(null);
                            Log.d("[TEST]", "google sign in fail");
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser fuser) { //update ui code here

        if (fuser != null) {
            // Name, email address, and profile photo Url
            String name = fuser.getDisplayName();
            email = fuser.getEmail();
            Uri photoUrl = fuser.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = fuser.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = fuser.getUid();

            Log.d("[TEST]", "GetGoogleInfo: " + name + " " + email + " " + photoUrl + " " + uid + " " + emailVerified);

            // 구글이메일이 DB에 유저아이디 정보로 있는지 확인한다
            String emailcheck = email;

            String url = "http://" + ip + "/webServer/useridcheckimpl.mc";
            url += "?id=" + emailcheck;
            httpAsyncTask = new LoginActivity.HttpAsyncTask();
            httpAsyncTask.execute(url);

            Log.d("[TEST]", "Here:" + user);

            //login(email,"google");

            finish();
        }

    }


    public void getToken() {
        //토큰값을 받아옵니다.
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        // 토큰이 계속 초기화가 되기때문에 sharedPreferences로 저장하여 초기화 방지
                        token = task.getResult().getToken();
                        sptoken = getSharedPreferences("applicaton", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sptoken.edit();
                        editor.putString("token", token); // key, value를 이용하여 저장하는 형태
                        editor.commit();
                        Log.d("[Log]", token);
                    }
                });

    }


    public void clickbt(View v) {
        if (v.getId() == R.id.button_login) {
            String id = editText_loginid.getText().toString();
            String pwd = editText_loginpwd.getText().toString();
            login(id, pwd);
        } else if (v.getId() == R.id.button_register) {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.textView_findId) {
            Intent intent = new Intent(getApplicationContext(), FindIdActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.textView_findPwd) {
            Intent intent = new Intent(getApplicationContext(), FindPwdActivity.class);
            startActivity(intent);
        }
    }

    public void login(String id, String pwd) {
        String url = "http://" + ip + "/webServer/userloginimpl.mc";
        url += "?id=" + id + "&pwd=" + pwd + "&token=" + token;
        httpAsyncTask = new HttpAsyncTask();
        httpAsyncTask.execute(url);
    }


    //로그인 한 유저가 차량이 있으면 CarActivity로 없으면 CarRegister로
    public void checkCarData() {
        // URL 설정.
        String carUrl = "http://" + ip + "/webServer/cardata.mc?userid=" + user.getUserid();

        // AsyncTask를 통해 HttpURLConnection 수행.
        CarAsync carAsync = new CarAsync();
        carAsync.execute(carUrl);
    }


    /*
    HTTP 통신 Code
    */
    class HttpAsyncTask extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setTitle("Login");
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

            if (s == null || s.equals("")) {
                Toast.makeText(LoginActivity.this, "로그인 오류가 발생했습니다. 관리자에게 문의하세요", Toast.LENGTH_SHORT).show();
                return;
            }

            final String result = s.trim();

            Log.d("[TEST]", "[TEST]:" + result);

            if (result.equals("fail")) {
                // LOGIN FAIL
                AlertDialog.Builder dailog = new AlertDialog.Builder(LoginActivity.this);
                dailog.setTitle("로그인에 실패하였습니다.");
                dailog.setMessage("다시 시도해 주십시오.");
                dailog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                dailog.show();
            } else if (result.equals("login")) {
                // 다른 디바이스에서 로그인한 계정
                AlertDialog.Builder dailog = new AlertDialog.Builder(LoginActivity.this);
                dailog.setTitle("다른 기기에서 로그인 중입니다.");
                dailog.setMessage("로그아웃 후 사용해 주세요.");
                dailog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                dailog.show();
            } else if (result.equals("cannot")) {
                // 비밀번호가 다름
                AlertDialog.Builder dailog = new AlertDialog.Builder(LoginActivity.this);
                dailog.setTitle("아이디 또는 비밀번호를 다시 확인해 주세요.");
                dailog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                dailog.show();
            }// useridcheckimpl에서 available이 오면 db에 구글email로 아이디가 없다고 판단하고 구글정보로 회원가입을 진행시킨다.
            else if (result.equals("available")) {
                Intent intent = new Intent(getApplicationContext(), SocialRegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("email", email);
                startActivity(intent);
            }// useridcheckimpl에서 cannot이 오면 db에 구글email로 아이디가 있다고 판단하고 email로 로그인을 진행시킨다.
            else if (result.equals("cannot id")) {
                login(email, "google");
            }// useridcheckimpl에서 checkfail 오면 아이디체크 오류가 발생하여 토스트로 알려준다.
            else if (result.equals("checkfail")) {
                Toast.makeText(LoginActivity.this, user.getUsername() + "구글 로그인 오류가 발생했습니다!", Toast.LENGTH_SHORT).show();
            } else {
                // LOGIN SUCCESS
                JSONObject jo = null;
                // Date형 변수 문자열 선언
                String strbirth = "";
                String strregdate = "";
                try {
                    // JSONObject 값 가져오기
                    jo = new JSONObject(s);
                    String userid = jo.getString("userid");
                    String userpwd = jo.getString("userpwd");
                    String username = jo.getString("username");
                    String userphone = jo.getString("userphone");
                    strbirth = jo.getString("userbirth");
                    String usersex = jo.getString("usersex");
                    strregdate = jo.getString("userregdate");
                    String userstate = jo.getString("userstate");
                    String usersubject = jo.getString("usersubject");
                    String babypushcheck = jo.getString("babypushcheck");
                    String accpushcheck = jo.getString("accpushcheck");
                    String sleeppushcheck = jo.getString("sleeppushcheck");
                    String droppushcheck = jo.getString("droppushcheck");
                    String mobiletoken = jo.getString("mobiletoken");

                    // String 변수를 Date로 변환
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date userbirth = sdf.parse(strbirth);
                    Date userregdate = sdf.parse(strregdate);

                    // user 객체 생성
                    user = new UsersVO(userid, userpwd, username, userphone, userbirth, usersex, userregdate, userstate, usersubject, babypushcheck, accpushcheck, sleeppushcheck, droppushcheck, mobiletoken);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 자동 로그인
                if (checkBox_loginauto.isChecked()) {
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString("userid", user.getUserid());
                    edit.putString("userpwd", user.getUserpwd());
                    edit.putString("username", user.getUsername());
                    edit.putString("userphone", user.getUserphone());
                    edit.putString("userbirth", strbirth);
                    edit.putString("usersex", user.getUsersex());
                    edit.putString("userregdate", strregdate);
                    edit.putString("userstate", user.getUserstate());
                    edit.putString("usersubject", user.getUsersubject());
                    edit.putString("babypushcheck", user.getBabypushcheck());
                    edit.putString("accpushcheck", user.getAccpushcheck());
                    edit.putString("sleeppushcheck", user.getAccpushcheck());
                    edit.putString("droppushcheck", user.getAccpushcheck());
                    edit.putString("mobiletoken", user.getMobiletoken());
                    edit.commit();
                }

                //로그인 한 유저가 차량이 있으면 CarActivity로 없으면 CarRegister로
                checkCarData();

            }
        }
    }
    /*
    End HTTP 통신 Code
     */

    // carcheck를 위한 Async
    class CarAsync extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... strings) {
            String url = strings[0];
            String result = HttpConnect.getString(url); //result는 JSON
            Log.d("[TAG]", result);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {

            Log.d("[TEST]", "s:" + s);

            // userid가 car를 갖고있지 않으면 차량을 등록하는 화면으로 넘긴다
            if (s == null || s.equals("[]")) {
                Intent intent = new Intent(getApplicationContext(), CarRegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.putExtra("user", user);
                startActivity(intent);
            } else {
                Intent intent = new Intent(getApplicationContext(), CarActivity.class);
                intent.putExtra("user", user);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                Toast t = Toast.makeText(LoginActivity.this, user.getUsername() + "님 환영합니다", Toast.LENGTH_SHORT);
                t.setGravity(Gravity.BOTTOM, 0, 150);
                t.show();
            }
        }


    }

}