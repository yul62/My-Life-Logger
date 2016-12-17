package com.example.administrator.mylifelogger;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.plus.model.people.Person;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Record extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMarkerDragListener {
    private GoogleMap mMap;
    private TextView name;
    private EditText title, context;
    private Spinner spinner,spinner2,spinner3;
    private String dateStr,ImgStr=null;
    SQLiteDatabase db;
    ImageView iv = null;

    private Button Gps, picBtn, saveBtn,delBtn;
    private Double mLat, mLng;
    private GPSListener gpsListener = new GPSListener();
    private LocationManager manager;
    private LatLng latlngCameraCenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        try {
            db = SQLiteDatabase.openDatabase( "/data/data/com.example.administrator.mylifelogger/myDB",
                    null, SQLiteDatabase.CREATE_IF_NECESSARY);
            db.execSQL("create table life ( recID integer PRIMARY KEY autoincrement,  lat  REAL,  lng REAL, location STRING, mdate STRING, category STRING ,title STRING ,content STRING,image STRING, hour INTEGER, minutes INTEGER);  "    );
            //   db.close();
        }catch (SQLException e) {
        }
        name = (TextView) findViewById(R.id.Name);
        title = (EditText)findViewById(R.id.editText);
        context = (EditText)findViewById(R.id.editText2);
        spinner = (Spinner)findViewById(R.id.spinner);
        spinner2 = (Spinner)findViewById(R.id.spinner2);
        spinner3 = (Spinner)findViewById(R.id.spinner3);

        Gps = (Button) findViewById(R.id.GPS_btn);
        Gps.setOnClickListener(this);

        picBtn = (Button) findViewById(R.id.picBtn);
        picBtn.setOnClickListener(this);

        saveBtn = (Button)findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(this);


        iv = (ImageView)findViewById(R.id.iv);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //초기 지도는 국민대
        getMapAddress(37.6108, 126.9956, 1);
    }

    @Override //사진 찍은 이미지 넘겨주는것
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
        iv.setImageURI(data.getData());
        ImgStr  = String.valueOf(data.getData());
        Log.v("vv", String.valueOf(data.getData()));}
        catch (Exception e){

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.GPS_btn:
                manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);       //gps 확인
                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    showSettingsAlert();    //설정창
                }
                startLocationService();  //gps
                break;

            case R.id.picBtn:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,1);
                break;

            case R.id.saveBtn:
                Log.v("vv",name.getText()+dateStr);
                db.execSQL("insert into life (lat,  lng, location , mdate , category  ,title  ,content , image, hour, minutes) values('" + mLat + "', '" + mLng + "','" + name.getText() + "','" + dateStr +  "','" + spinner.getSelectedItem().toString() + "','" +
                        title.getText().toString() + "','" + context.getText().toString() + "','"+ImgStr+"','"+spinner2.getSelectedItem()+"','"+spinner3.getSelectedItem()+"' );");
                Toast.makeText(getBaseContext(),"저장이 완료되었습니다.",Toast.LENGTH_SHORT).show();
                title.setText("");
                context.setText("");
                iv.clearColorFilter();
                break;


        }
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
        } catch (SecurityException ex) {
            ex.printStackTrace();
        }

    }


    private class GPSListener implements LocationListener {

        //위치 정보가 업데이트 될 때
        public void onLocationChanged(Location location) {
            mLat = location.getLatitude();
            mLng = location.getLongitude();

            try {
                manager.removeUpdates(gpsListener);         //gps 리스너 업데이트 종료
            } catch (SecurityException ex) {
                ex.printStackTrace();
            }
            getMapAddress(mLat, mLng, 0);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    //현재 시간가져오기,
    void getTime(){
        long now = System.currentTimeMillis();
        // 현재 시간을 저장 한다.
        Date date = new Date(now);
        // 시간 포맷으로 만든다.
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        dateStr = sdfNow.format(date);
    }
    // 위도경도로 지도 띄우기
    void getMapAddress(double lat, double lng, int first) {
        mMap.clear();
        getTime();

        LatLng location = new LatLng(lat, lng);
        mLat=lat; mLng=lng;
        String locaStr = null;
        if (first == 1)
            locaStr = "국민대학교";
        else
            locaStr = "현재 위치";
        mMap.addMarker(new MarkerOptions().position(location).title(locaStr).draggable(true));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        mMap.setOnMarkerDragListener(this);

        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        mMap.animateCamera(zoom);


        //한글로 제대로 된 주소 가져오기 ex) 서울특별시 XX구 XX동
        getLocalName();
    }

    //한글로 제대로 된 주소 가져오기 ex) 서울특별시 XX구 XX동
    void getLocalName(){
        Geocoder geocoder = new Geocoder(getBaseContext(), Locale.getDefault());

        String str = null;
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(mLat, mLng, 1);
            str = addresses.get(0).getAddressLine(0).toString();
            System.out.println(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        name.setText(str.substring(5));
    }
    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override // 마커 이동 끝났을 때
    public void onMarkerDragEnd(Marker marker) {

        latlngCameraCenter= marker.getPosition();

       // Toast.makeText(getApplicationContext(), "마커이동", Toast.LENGTH_LONG).show();
        getTime();
        mLat=latlngCameraCenter.latitude; mLng = latlngCameraCenter.longitude;
        getLocalName();
    }
}