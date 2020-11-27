package com.example.customermobile;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.owl93.dpb.CircularProgressView;
import com.skydoves.progressview.OnProgressChangeListener;
import com.skydoves.progressview.ProgressView;

import java.util.Random;

import www.sanju.motiontoast.MotionToast;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView toolbar_title;
    Fragment fragment1, fragment2, fragment3;
    //  네이게이션 드로우어어
    private DrawerLayout mDrawerLayout;
    private Context context = this;

    // circularProgress구현부분
//    CircularProgressView circularProgressView;
//    ProgressView progressView;
    // circularProgress구현부분 종료


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 상단 바 설정
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar_title = findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.mipmap.menuicon); //뒤로가기 버튼 이미지 지정

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        // 네비게이션 화면 설정
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                int id = menuItem.getItemId();
                String title = menuItem.getTitle().toString();

                if (id == R.id.home) {
                    onChangedFragment(1, null);
                } else if (id == R.id.mypage) {
                    onChangedFragment(2, null);
                } else if (id == R.id.map) {
                    onChangedFragment(3, null);
                } else if (id == R.id.logout) {

                }

                return true;
            }
        });

        // 프래그먼트 화면 설정
        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        fragment3 = new Fragment3();
        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment1).commit();

    }


    public void onChangedFragment(int position, Bundle bundle) {
        Fragment fragment = null;

        switch (position){
            case 1:
                fragment = fragment1;
                toolbar_title.setText("Home");
                break;
            case 2:
                fragment = fragment2;
                toolbar_title.setText("My Page");
                break;
            case 3:
                fragment = fragment3;
                toolbar_title.setText("Map");
                break;
            default:
                break;
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: { // 왼쪽 상단 버튼 눌렀을 때
                mDrawerLayout.openDrawer(GravityCompat.END);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    //circularProgress구현부분
//        circularProgressView = findViewById(R.id.circularProgressView);
//        circularProgressView.setProgress(0);
//        circularProgressView.setMaxValue(100);
//
//        progressView = findViewById(R.id.progressView);
//        progressView.setOnProgressChangeListener(new OnProgressChangeListener() {
//            @Override
//            public void onChange(float v) {
//                progressView.setLabelText("Progress: " + v + "%");
//            }
//        });
    // circularProgress구현부분 종료



    public void clickbt(View v) {

        //circularProgress구현부분
//        if(v.getId() == R.id.button1){
//            circularProgressView.setProgress(circularProgressView.getProgress() + 5);
//        }else if(v.getId() == R.id.button2){
//            MotionToast.Companion.createToast(this,
//                    "Hurray success 😍",
//                    "Upload Completed successfully!",
//                    MotionToast.TOAST_SUCCESS,
//                    MotionToast.GRAVITY_BOTTOM,
//                    MotionToast.LONG_DURATION,
//                    ResourcesCompat.getFont(this,R.font.helvetica_regular));
//        }else if(v.getId() == R.id.button3){
//            Random r = new Random();
//            progressView.setProgress(r.nextInt(100));
//        }
        // circularProgress구현부분 종료

    }
}