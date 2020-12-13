package com.example.customermobile.network;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpConnect {
    public static String getString(String urlstr){
        Log.d("[Log]", "HttpConnect.getString: " + urlstr);
        String result = null;
        URL url = null;
        HttpURLConnection hcon = null;
        InputStream is = null;
        try{
            url = new URL(urlstr);
            hcon = (HttpURLConnection)url.openConnection();
            hcon.setConnectTimeout(3000);
            hcon.setRequestMethod("GET");
            is = new BufferedInputStream(hcon.getInputStream());
            Log.d("[Log]", "is: ");
            result = convertStr(is);
            Log.d("[Log]", "result: " + result);
        }catch(Exception e){
            Log.d("[Log]", "Exception!!");
            e.printStackTrace();
        }

        return result;
    }

    public static String convertStr(InputStream is){
        String result = null;
        BufferedReader bi = null;
        StringBuilder sb = new StringBuilder();
        try{
            bi = new BufferedReader(
                    new InputStreamReader(is)
            );
            String temp = "";
            while((temp =bi.readLine()) != null){
                sb.append(temp);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return sb.toString();
    }
}