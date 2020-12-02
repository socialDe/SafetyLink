package com.example.customermobile.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.customermobile.R;
import com.example.customermobile.network.HttpConnect;
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
import com.vo.UsersVO;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LoginActivity extends AppCompatActivity {
    HttpAsyncTask httpAsyncTask; // HTTP 전송 데이터
    SharedPreferences sp; // 자동 로그인
    UsersVO user; // user 객체

    ActionBar actionBar;
    EditText editText_loginid, editText_loginpwd;
    CheckBox checkBox_loginauto;

    // 소셜로그인을 위한 변수
    private FirebaseAuth mAuth = null;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;
    private SignInButton signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // ActionBar Setting
        actionBar = getSupportActionBar();
        actionBar.hide();

        editText_loginid = findViewById(R.id.editText_loginId);
        editText_loginpwd = findViewById(R.id.editText_loginPwd);
        checkBox_loginauto = findViewById(R.id.checkBox_loginAuto);

        sp = getSharedPreferences("autoLogin", MODE_PRIVATE);

        String userid = sp.getString("userid", "");
        String userpwd = sp.getString("userpwd", "");

        if(userid != null && userid != "" && userpwd != null && userpwd != ""){
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

        if (mAuth.getCurrentUser() != null) {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
            finish();
        }
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
                        } else {
                            // If sign in fails, display a message to the user.
                            updateUI(null);
                        }
                    }
                });
    }
    private void updateUI(FirebaseUser user) { //update ui code here
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();

            Log.d("[TEST]",name+" "+email+" "+photoUrl+" "+uid);
            }


            Intent intent = new Intent(this, RegisterActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

    }



    public void clickbt(View v){
        if(v.getId() == R.id.button_login) {
            UsersVO user = new UsersVO(editText_loginid.getText().toString(), editText_loginpwd.getText().toString());

            String id = editText_loginid.getText().toString();
            String pwd = editText_loginpwd.getText().toString();
            login(id, pwd);
        }else if(v.getId() == R.id.button_register){
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
        }else if(v.getId() == R.id.textView_findId){
            Intent intent = new Intent(getApplicationContext(), FindIdActivity.class);
            startActivity(intent);
        }else if(v.getId() == R.id.textView_findPwd){
            Intent intent = new Intent(getApplicationContext(), FindPwdActivity.class);
            startActivity(intent);
        }
     }

    public void login(String id, String pwd){
        String url = "http://192.168.0.103/webServer/userloginimpl.mc";
        url += "?id="+id+"&pwd="+pwd;
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
            Log.d("[Log]", "preExecute");
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setTitle("Login");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
//            userData = new UsersVO();
//            userData.setUserid(urls[1]);
//            userData.setUserpwd(urls[2]);
//            Log.d("[Server]", "[AsyncTask Background]" + urls[0] + urls[1] + urls[2] + urls);
//            return POST(urls[0], userData);

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
                // LOGIN FAIL
                androidx.appcompat.app.AlertDialog.Builder dailog = new AlertDialog.Builder(LoginActivity.this);
                dailog.setTitle("LOGIN FAIL");
                dailog.setMessage("Try Again...");
                dailog.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                dailog.show();
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
                    String mobiletoken = jo.getString("mobiletoken");

                    // String 변수를 Date로 변환
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date userbirth = sdf.parse(strbirth);
                    Date userregdate = sdf.parse(strregdate);

                    // user 객체 생성
                    user = new UsersVO(userid, userpwd, username, userphone, userbirth, usersex, userregdate, userstate, usersubject, babypushcheck, accpushcheck, mobiletoken);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // 자동 로그인
                if(checkBox_loginauto.isChecked()) {
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
                    edit.putString("mobiletoken", user.getMobiletoken());
                    edit.commit();
                }

                // LOGIN COMPLETE
                // 액티비티 기록 없이 메인 화면으로 전환
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("user", user);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                Toast.makeText(LoginActivity.this, user.getUsername() + "님 환영합니다", Toast.LENGTH_SHORT).show();
            }
        }
    }
    /*
    End HTTP 통신 Code
     */
}