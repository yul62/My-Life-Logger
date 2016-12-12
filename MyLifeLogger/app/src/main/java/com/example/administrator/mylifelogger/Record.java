package com.example.administrator.mylifelogger;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.plus.model.people.Person;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Record extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener {
    private GoogleMap mMap;
    private TextView name;

    private Button Gps;
    private Double mLat, mLng;
    private GPSListener gpsListener = new GPSListener();
    private LocationManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_record);


        name = (TextView) findViewById(R.id.Name);
        Gps = (Button) findViewById(R.id.GPS_btn);
        Gps.setOnClickListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        getMapAddress(37.6108,126.9956,1);
    }

    @Override
    public void onClick(View view) {

        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);       //gps 확인
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            showSettingsAlert();    //설정창
        }
        startLocationService();  //gps
    }



    //gps 설정되어있지 않을 때 설정창 이동
    public void showSettingsAlert() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder
                .setTitle("GPS 사용유무셋팅")
                .setMessage("GPS 셋팅이 되지 않았습니다. \n 설정창으로 가시겠습니까?")
                .setPositiveButton("Setting".toString(),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(intent);
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
        AlertDialog dialog = alertDialogBuilder.create();
        dialog.show();
    }


    //gps 정보요청
    private void startLocationService() {

        long minTime = 1000;
        float minDistance = 0;

        try {
            // GPS를 이용한 위치 요청
            manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    minTime,
                    minDistance,
                    gpsListener);
            // 네트워크를 이용한 위치 요청
            manager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    minTime,
                    minDistance,
                    gpsListener);

            // 위치 확인이 안되는 경우에도 최근에 확인된 위치 정보 먼저 확인
            Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {
                Double latitude = lastLocation.getLatitude();
                Double longitude = lastLocation.getLongitude();

                //   Toast.makeText(getApplicationContext(), "Last Known Location : " + "Latitude : " + latitude + "\nLongitude:" + longitude, Toast.LENGTH_LONG).show();
            }
        } catch(SecurityException ex) {
            ex.printStackTrace();
        }

    }


    private class GPSListener implements LocationListener {

        //위치 정보가 업데이트 될 때
        public void onLocationChanged(Location location) {
            double latitude = location.getLatitude();
            double longitude  = location.getLongitude();

            //String msg = "Latitude : "+ myLat+ "\nLongitude:"+ myLng;
            //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

            try {
                manager.removeUpdates(gpsListener);         //gps 리스너 업데이트 종료
            } catch(SecurityException ex) {
                ex.printStackTrace();
            }
            getMapAddress(latitude,longitude,0);

        }
        public void onProviderDisabled(String provider) {
        }


        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }
    void getMapAddress(double lat, double lng,int first){
        LatLng loaction = new LatLng(lat, lng);

        String locaStr=null;
        if(first==1)
            locaStr = "국민대학교";
        else
            locaStr = "현재 위치";
        mMap.addMarker(new MarkerOptions().position(loaction).title(locaStr));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loaction));

        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        mMap.animateCamera(zoom);


        Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());

        String str = null;
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(loaction.latitude, loaction.longitude, 1);
            str = addresses.get(0).getAddressLine(0).toString();
            System.out.println(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        name.setText(str.substring(5));
    }

}