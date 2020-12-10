package com.example.customermobile;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFService extends FirebaseMessagingService {
    public MyFService() {
    }

//    생성
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        String title = remoteMessage.getNotification().getTitle();
        // title은 고정 아래는 추가가능
        String carid = remoteMessage.getData().get("carid");
        String contents = remoteMessage.getData().get("contents");

        Log.d("[FCM]","[FCM]"+title+" "+carid+" "+contents);

        Intent intent = new Intent("notification");
        intent.putExtra("carid",carid);
        intent.putExtra("contents",contents);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

}