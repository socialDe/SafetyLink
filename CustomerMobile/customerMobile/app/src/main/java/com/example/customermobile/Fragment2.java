package com.example.customermobile;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.customermobile.activity.CarActivity;
import com.example.customermobile.activity.NonTruckFuncSetActivity;
import com.example.customermobile.activity.TruckFuncSetActivity;
import com.example.customermobile.vo.CarVO;
import com.example.customermobile.vo.UsersVO;


public class Fragment2 extends Fragment {
    TextView textView_myInfo, textView_carRegister, textView_safetyFuncSet;
    CarVO nowCar;
    int nowCarId;
    String userId;
    UsersVO user;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) getLayoutInflater().inflate(R.layout.fragment2, container, false);

        textView_myInfo = rootView.findViewById(R.id.textView_myInfo);
        textView_carRegister = rootView.findViewById(R.id.textView_carRegister);
        textView_safetyFuncSet = rootView.findViewById(R.id.textView_safetyFuncSet);
        nowCarId = ((CarActivity)getActivity()).getNowCarId();
        nowCar = ((CarActivity)getActivity()).getNowCar();
        user = ((CarActivity)getActivity()).getNowUser();
        userId = user.getUserid();

        textView_safetyFuncSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 현재 선택된 차량이 세단 or 승합차인 경우
                if(nowCar.getCartype().equals("p") || nowCar.getCartype().equals("v")){
                    Intent intent = new Intent(getActivity(), NonTruckFuncSetActivity.class);
                    intent.putExtra("userId",userId);
                    intent.putExtra("nowCarId", nowCarId);
                    intent.putExtra("nowCar", nowCar);
                    intent.putExtra("user", user);
                    System.out.println("Intent putExtra: "+nowCarId);
                    startActivity(intent);

                }else if(nowCar.getCartype().equals("t")){
                    // 현재 선택된 차량이 트럭인 경우
                    Intent intent = new Intent(getActivity(), TruckFuncSetActivity.class);
                    intent.putExtra("userId",userId);
                    intent.putExtra("nowCarId", nowCarId);
                    System.out.println("Intent putExtra: "+nowCarId);
                    startActivity(intent);

                }


            }
        });



        return rootView;
    }
}