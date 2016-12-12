package com.example.administrator.mylifelogger;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DayStat extends FragmentActivity implements View.OnClickListener, OnMapReadyCallback {
    SQLiteDatabase db;
    private GoogleMap mMap;
    private Spinner serMonth,serDay;
    private Button searchBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_stat);
        db = SQLiteDatabase.openDatabase( "/data/data/com.example.administrator.mylifelogger/myDB",null, SQLiteDatabase.CREATE_IF_NECESSARY);


        serMonth = (Spinner)findViewById(R.id.spinner4);
        serDay = (Spinner)findViewById(R.id.spinner5);
        searchBtn = (Button)findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onClick(View view) {


        Cursor cursor = db.rawQuery("select * from life", null);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Cursor cursor = db.rawQuery("select * from life", null);
        while (cursor.moveToNext()) {

                LatLng location = new LatLng(cursor.getDouble(1), cursor.getDouble(2));
                mMap.addMarker(new MarkerOptions().position(location).title(cursor.getInt(0) + ". " + cursor.getString(6)));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(location));

        }
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        mMap.animateCamera(zoom);
    }

}
