package com.example.administrator.mylifelogger;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ViewRecord extends FragmentActivity {

    SQLiteDatabase db;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_record);

        db = SQLiteDatabase.openDatabase("/data/data/com.example.administrator.mylifelogger/myDB", null, SQLiteDatabase.CREATE_IF_NECESSARY);
        textView = (TextView)findViewById(R.id.textView11);

        Intent intent = getIntent();

        Cursor cursor = db.rawQuery("select * from life",null);
        while(cursor.moveToNext()){
            if(cursor.getInt(0) == intent.getExtras().getInt("id")){
                textView.setText(cursor.getString(3)+" "+cursor.getString(4)+" "+cursor.getString(6)+" "+cursor.getInt(9)+"시간"+cursor.getInt(10));
                break;
            }
        }
    }


}
