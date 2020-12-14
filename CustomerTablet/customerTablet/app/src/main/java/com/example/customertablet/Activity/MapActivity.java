package com.example.customertablet.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.customertablet.HomeActivity;
import com.example.customertablet.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity {
    MapView mapView;
    GoogleMap gMap;
    LocationManager locationManager;

    double mLatitude;  //위도
    double mLongitude; //경도

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // Permission Check
        String[] permission = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };
        ActivityCompat.requestPermissions(this, permission, 101);

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        locationManager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        MapsInitializer.initialize(getApplication().getApplicationContext());
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                gMap = googleMap;
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 10, locationListener);

                if (mLatitude == 0 && mLongitude == 0){
                    mLatitude = 37;
                    mLongitude = 127;
                }
                LatLng latlng = new LatLng(mLatitude, mLongitude);
                Toast.makeText(getApplicationContext(), "latlng: " + mLatitude + ", "+ mLongitude, Toast.LENGTH_SHORT).show();
                gMap.setMyLocationEnabled(true);
                gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,10));
            }
        });
    }

    //위치정보 구하기 리스너
    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            mLatitude = location.getLatitude();   //위도
            mLongitude = location.getLongitude(); //경도
            LatLng latlng = new LatLng(mLatitude, mLongitude);
            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng,10));
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) { Log.d("[tag]", "onStatusChanged"); }

        @Override
        public void onProviderEnabled(String provider) { }

        @Override
        public void onProviderDisabled(String provider) { }
    };

    @SuppressLint("MissingPermission")
    @Override
    protected void onResume() {
        super.onResume();
        if(gMap != null){
            gMap.setMyLocationEnabled(true);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    protected void onPause() {
        super.onPause();
        if(gMap != null){
            gMap.setMyLocationEnabled(false);
        }
    }

    public void clickbt(View v){
        if(v.getId() == R.id.imageButton_control){
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }else if (v.getId() == R.id.imageButton_map){

        }else  if (v.getId() == R.id.imageButton_setting){

        }
    }
}