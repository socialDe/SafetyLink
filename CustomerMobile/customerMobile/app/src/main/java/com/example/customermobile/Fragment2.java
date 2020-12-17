package com.example.customermobile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.customermobile.activity.CarActivity;
import com.example.customermobile.activity.CarManagementActivity;
import com.example.customermobile.activity.MyPageActivity;
import com.example.customermobile.activity.NonTruckFuncSetActivity;
import com.example.customermobile.activity.TruckFuncSetActivity;
import com.example.customermobile.vo.CarVO;
import com.example.customermobile.vo.UsersVO;


public class Fragment2 extends Fragment implements CarActivity.OnBackPressedListener {
    TextView textView_myInfo, textView_carRegister, textView_safetyFuncSet;
    CarVO nowCar;
    int nowCarId;
    String userId;
    UsersVO user;
    CarActivity carActivity;
    Fragment1 fragment1;
    MenuItem menuItem;
    TextView toolbar_title;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) getLayoutInflater().inflate(R.layout.fragment2, container, false);

        textView_myInfo = rootView.findViewById(R.id.textView_myInfo);
        textView_carRegister = rootView.findViewById(R.id.textView_carRegister);
        textView_carRegister.setOnClickListener(new View.OnClickListener() { // 차량 관리 페이지
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CarManagementActivity.class);
                intent.putExtra("user",((CarActivity)getActivity()).getNowUser());
                startActivity(intent);
            }
        });
        textView_safetyFuncSet = rootView.findViewById(R.id.textView_safetyFuncSet);
        nowCarId = ((CarActivity)getActivity()).getNowCarId();
        nowCar = ((CarActivity)getActivity()).getNowCar();
        user = ((CarActivity)getActivity()).getNowUser();
        userId = user.getUserid();
        toolbar_title = rootView.findViewById(R.id.toolbar_title);

        Log.d("[now]","1:"+nowCar+" 2:"+nowCarId+" 3:"+user);

        textView_safetyFuncSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 현재 선택된 차량이 세단 or 승합차인 경우
                if(nowCar.getCartype().equals("p") || nowCar.getCartype().equals("v")){
                    Intent intent = new Intent(getActivity(), NonTruckFuncSetActivity.class);
                    intent.putExtra("userId",userId);
                    intent.putExtra("nowCarId", nowCarId);
                    intent.putExtra("nowCar", nowCar);
                    System.out.println("Intent putExtra: "+nowCarId);
                    startActivity(intent);

                }else if(nowCar.getCartype().equals("t")){
                    // 현재 선택된 차량이 트럭인 경우
                    Intent intent = new Intent(getActivity(), TruckFuncSetActivity.class);
                    intent.putExtra("userId",userId);
                    intent.putExtra("nowCar", nowCar);
                    intent.putExtra("nowCarId", nowCarId);
                    System.out.println("Intent putExtra: "+nowCarId);
                    startActivity(intent);
                }
            }
        });

        textView_myInfo.setOnClickListener(new View.OnClickListener() { // 내 정보
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyPageActivity.class);
                intent.putExtra("user",user);
                startActivity(intent);
            }
        });
        fragment1 = new Fragment1();
        return rootView;
    }

    @Override
    public void onBack() {
        Log.e("Other", "onBack()");
        // 리스너를 설정하기 위해 Activity 를 받아옵니다.
        CarActivity activity = (CarActivity)getActivity();
        // 한번 뒤로가기 버튼을 눌렀다면 Listener 를 null 로 해제해줍니다.
        activity.setOnBackPressedListener(null);
        // MainFragment 로 교체
        activity.getCarData();
//        activity.replaceFragment(Fragment1.newInstance()); // 이건 다른 방법

//        toolbar_title.setText("Home");
//        getActivity().getSupportFragmentManager().beginTransaction()
//                .replace(R.id.container, fragment1).commit();
        // Activity 에서도 뭔가 처리하고 싶은 내용이 있다면 하단 문장처럼 호출해주면 됩니다.
//         activity.onBackPressed();
    }
    @Override
    // 혹시 Context 로 안되시는분은 Activity 로 바꿔보시기 바랍니다.
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.e("Other", "onAttach()");
        ((CarActivity)context).setOnBackPressedListener(this);
    }

}
