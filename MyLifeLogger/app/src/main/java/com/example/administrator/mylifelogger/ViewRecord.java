package com.example.administrator.mylifelogger;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ViewRecord extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    SQLiteDatabase db;
    private TextView title, content, category,location;
    private double mLat, mLng;
    private ImageView iv ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_record);

        db = SQLiteDatabase.openDatabase("/data/data/com.example.administrator.mylifelogger/myDB", null, SQLiteDatabase.CREATE_IF_NECESSARY);
        title = (TextView)findViewById(R.id.textView11);
        content = (TextView)findViewById(R.id.textView12);
        category = (TextView)findViewById(R.id.textView13);
        location = (TextView)findViewById(R.id.textView14);

        iv = (ImageView)findViewById(R.id.imageView2);


        Intent intent = getIntent();

        Cursor cursor = db.rawQuery("select * from life",null);
        while(cursor.moveToNext()){
            if(cursor.getInt(0) == intent.getExtras().getInt("id")){
                //title.setText(cursor.getString(3)+" "+cursor.getString(4)+" "+cursor.getString(6)+" "+cursor.getInt(9)+"시간"+cursor.getInt(10));
                title.setText(cursor.getString(6));
                content.setText(cursor.getString(7));
                category.setText(cursor.getString(4)+" " +
                        "" +
                        "" +
                        ""+cursor.getString(5)+" "+cursor.getInt(9)+"시간 "+cursor.getInt(10)+"분");
                location.setText(cursor.getString(3));
                mLat = cursor.getDouble(1);
                mLng = cursor.getDouble(2);
                iv.setImageURI(Uri.parse(cursor.getString(8)));
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map3);
                mapFragment.getMapAsync(this);
                break;
            }
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng location = new LatLng(mLat, mLng);
        mMap.addMarker(new MarkerOptions().position(location).title("jj"));


        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(14);
        mMap.animateCamera(zoom);
    }
}