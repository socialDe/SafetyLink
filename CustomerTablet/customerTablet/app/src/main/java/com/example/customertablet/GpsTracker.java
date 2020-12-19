package com.example.customertablet;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.widget.Toast;

import com.example.customertablet.Activity.MapActivity;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;

import java.util.function.Consumer;


public class GpsTracker extends Service implements LocationListener {

    private final Context mContext;
    Location location;
    double latitude;
    double longitude;
    GoogleApiClient mGoogleApiClient = null;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    protected LocationManager locationManager;


    public GpsTracker(Context context) {

        this.mContext = context;


        getLocation();
        Log.d("[Weather]", "GPSTracker Class 생성");
        Log.d("[Weather]", "This Device SDK_INT:" + Build.VERSION.SDK_INT);
    }


    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


            if (!isGPSEnabled && !isNetworkEnabled) {
                Log.d("[Weather]", "GPS && Network Not Enabled");

            } else {

                int hasFineLocationPermission = ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.ACCESS_FINE_LOCATION);
                int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION);


                if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                        hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

                    ;
                } else
                    return null;


                if (isNetworkEnabled) {

                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("[Weather]", "Network Enabled");
                    if (locationManager != null)
                    {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null)
                        {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }


                if (isGPSEnabled)
                { Log.d("[Weather]", "GPS Enabled");
                    if (location == null)
                    {
                        Log.d("[Weather]", "GPS location Not Null");
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null)
                        {
                            Log.d("[Weather]", "Location Manager Not Null");
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            Log.d("[Weather]", "Location Provider:" +LocationManager.GPS_PROVIDER);

                            if(location == null){
                                Log.d("[Weather]", "Location Null");
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                                    Consumer<Location> consumer = new Consumer<Location>() {
                                        @Override
                                        public void accept(Location location) {
                                            Log.d("[Weather]", "Consumer accept Executed");
                                        }
                                    };
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                        locationManager.getCurrentLocation(LocationManager.GPS_PROVIDER,null,mContext.getMainExecutor(),consumer);
                                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                        if(location == null){
                                            Log.d("[Weather]", "Location getCurrentLocation Failed. It still location is null");
                                        }
                                        Log.d("[Weather]", "Current Location "+location.getLatitude()+":"+location.getLongitude());
                                    }else{
                                        Log.d("[Weather]", "SDK_INT <= R");
                                        Log.d("[Weather]", "SDK_INT:" +Build.VERSION.SDK_INT);
                                    }
                                }else{
                                    Log.d("[Weather]", "SDK_INT <= P");
                                }
                            }else {
                                Log.d("[Weather]", "Location Not Null");

                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }
        }

        catch (Exception e)
        {
            Log.d("[Weather]", ""+e.toString());
        }

        Log.d("[Weather]", "Location 생성자 location 반환");
        return location;
    }

    public double getLatitude()
    {
        Log.d("[Weather]", "getLatitude 호출");
        if(location != null)
        {
            Log.d("[Weather]", "getLatitude 입력");
            latitude = location.getLatitude();
        }

        return latitude;
    }

    public double getLongitude()
    {
        Log.d("[Weather]", "GetLongitude 호출");
        if(location != null)
        {
            Log.d("[Weather]", "GetLongitude 입력");
            longitude = location.getLongitude();
        }

        return longitude;
    }

    @Override
    public void onLocationChanged(Location location)
    {
        Log.d("[Weather]", "Location Changed");
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        Log.d("[Weather]", "Changed Location is lat:"+lat +" lng: "+lng);
    }

    @Override
    public void onProviderDisabled(String provider)
    {
    }

    @Override
    public void onProviderEnabled(String provider)
    {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras)
    {
    }

    @Override
    public IBinder onBind(Intent arg0)
    {
        return null;
    }


    public void stopUsingGPS()
    {
        if(locationManager != null)
        {
            locationManager.removeUpdates(GpsTracker.this);
        }
    }


}