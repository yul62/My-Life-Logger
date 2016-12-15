package com.example.administrator.mylifelogger;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

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
    private Spinner serMonth, serDay;
    private Button searchBtn;


    ListView listview;
    ListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_stat);
        db = SQLiteDatabase.openDatabase("/data/data/com.example.administrator.mylifelogger/myDB", null, SQLiteDatabase.CREATE_IF_NECESSARY);

        serMonth = (Spinner) findViewById(R.id.spinner4);
        serDay = (Spinner) findViewById(R.id.spinner5);
        searchBtn = (Button) findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Adapter 생성
        adapter = new ListViewAdapter();

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.listview1);
        listview.setAdapter(adapter);

    }

    @Override
    public void onClick(View view) {
        mMap.clear();
        serMonth.getSelectedItem();
        Cursor cursor = db.rawQuery("select * from life", null);
        String str = null;
        str = serMonth.getSelectedItem().toString() + "/" + serDay.getSelectedItem().toString();
        int result = 0;
        removeList();
        while (cursor.moveToNext()) {
            if (str.equals(cursor.getString(4).substring(5, 10))) {
                switch (cursor.getString(5)) {
                    case "공부":
                        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.book), cursor.getString(6), cursor.getString(9) + "시간 " + cursor.getString(10) + "분",cursor.getInt(0));
                        break;
                    case "운동":
                        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.exercise), cursor.getString(6), cursor.getString(9) + "시간 " + cursor.getString(10) + "분",cursor.getInt(0));
                        break;
                    case "카페":
                        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.coffee), cursor.getString(6), cursor.getString(9) + "시간 " + cursor.getString(10) + "분",cursor.getInt(0));
                        break;
                    case "식사":
                        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.food), cursor.getString(6), cursor.getString(9) + "시간 " + cursor.getString(10) + "분",cursor.getInt(0));
                        break;
                    case "여행":
                        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.travel), cursor.getString(6), cursor.getString(9) + "시간 " + cursor.getString(10) + "분",cursor.getInt(0));
                        break;
                    case "게임":
                        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.game), cursor.getString(6), cursor.getString(9) + "시간 " + cursor.getString(10) + "분",cursor.getInt(0));
                        break;
                    case "영화":
                        adapter.addItem(ContextCompat.getDrawable(this, R.drawable.camera), cursor.getString(6), cursor.getString(9) + "시간 " + cursor.getString(10) + "분",cursor.getInt(0));
                        break;
                }
                result = 1;
                Toast.makeText(getBaseContext(), cursor.getString(4).substring(5, 10), Toast.LENGTH_SHORT).show();
                LatLng location = new LatLng(cursor.getDouble(1), cursor.getDouble(2));
                mMap.addMarker(new MarkerOptions().position(location).title(cursor.getInt(0) + ". " + cursor.getString(6)));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
            }
            adapter.notifyDataSetChanged();
        }
        if (result == 0) {
            Toast.makeText(getBaseContext(), "결과가 없습니다.", Toast.LENGTH_SHORT).show();
            removeList();
        }
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);
        mMap.animateCamera(zoom);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        String str = null;
        str = serMonth.getSelectedItem().toString() + "/" + serDay.getSelectedItem().toString();
        Cursor cursor = db.rawQuery("select * from life", null);

        while (cursor.moveToNext()) {
            switch (cursor.getString(5)) {
                case "공부":
                    adapter.addItem(ContextCompat.getDrawable(this, R.drawable.book), cursor.getString(6), cursor.getString(9) + "시간 " + cursor.getString(10) + "분",cursor.getInt(0));
                    break;
                case "운동":
                    adapter.addItem(ContextCompat.getDrawable(this, R.drawable.exercise), cursor.getString(6), cursor.getString(9) + "시간 " + cursor.getString(10) + "분",cursor.getInt(0));
                    break;
                case "카페":
                    adapter.addItem(ContextCompat.getDrawable(this, R.drawable.coffee), cursor.getString(6), cursor.getString(9) + "시간 " + cursor.getString(10) + "분",cursor.getInt(0));
                    break;
                case "식사":
                    adapter.addItem(ContextCompat.getDrawable(this, R.drawable.food), cursor.getString(6), cursor.getString(9) + "시간 " + cursor.getString(10) + "분",cursor.getInt(0));
                    break;
                case "여행":
                    adapter.addItem(ContextCompat.getDrawable(this, R.drawable.travel), cursor.getString(6), cursor.getString(9) + "시간 " + cursor.getString(10) + "분",cursor.getInt(0));
                    break;
                case "게임":
                    adapter.addItem(ContextCompat.getDrawable(this, R.drawable.game), cursor.getString(6), cursor.getString(9) + "시간 " + cursor.getString(10) + "분",cursor.getInt(0));
                    break;
                case "영화":
                    adapter.addItem(ContextCompat.getDrawable(this, R.drawable.camera), cursor.getString(6), cursor.getString(9) + "시간 " + cursor.getString(10) + "분",cursor.getInt(0));
                    break;
            }
//            Toast.makeText(getBaseContext(), cursor.getString(4).substring(5, 10), Toast.LENGTH_SHORT).show();
            LatLng location = new LatLng(cursor.getDouble(1), cursor.getDouble(2));
            mMap.addMarker(new MarkerOptions().position(location).title(cursor.getInt(0) + ". " + cursor.getString(6)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(location));

        }
        adapter.notifyDataSetChanged();

        CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);
        mMap.animateCamera(zoom);
    }

    public void removeList() {
        for (int i = adapter.getCount(); i > 0; i--) {
            adapter.removeItem(i - 1);
        }
        adapter.notifyDataSetChanged();
    }


}
