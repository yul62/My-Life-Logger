package com.example.administrator.mylifelogger;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ViewGoal extends FragmentActivity {

    SQLiteDatabase db;
    private TextView title, time, content, result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_goal);

        title = (TextView) findViewById(R.id.title1);
        time = (TextView) findViewById(R.id.day);
        content = (TextView) findViewById(R.id.content1);
        result = (TextView) findViewById(R.id.result);

        try {
            db = SQLiteDatabase.openDatabase("/data/data/com.example.administrator.mylifelogger/myDB",
                    null, SQLiteDatabase.CREATE_IF_NECESSARY);
            db.execSQL("create table goal ( recID integer PRIMARY KEY autoincrement, category STRING, hour INTEGER, minutes INTEGER, mdate STRING, title STRING, content STRING );  ");
            Toast.makeText(getBaseContext(), "하하", Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
        }

        Intent intent = getIntent();

        Cursor cursor = db.rawQuery("select * from goal", null);
        Cursor cursor1 = db.rawQuery("select *from life", null);
        while (cursor.moveToNext()) {
            int goalTime =0;

            if (cursor.getInt(0) == intent.getExtras().getInt("id")) {
                //category , hour , minutes , mdate , title , content
                title.setText( cursor.getString(5));
                Toast.makeText(getBaseContext(), cursor.getString(4), Toast.LENGTH_SHORT).show();
                time.setText("목표 : "+cursor.getString(4).substring(0, 2) + "월 " + cursor.getString(4).substring(3, 5) + "일 부터 일주일 동안 "
                        + cursor.getString(1) +" "+ cursor.getInt(2) + "시간 " + cursor.getInt(3) + "분");
                content.setText(cursor.getString(6));
                Calendar cal = Calendar
                        .getInstance();
                goalTime = cursor.getInt(2)*60+cursor.getInt(3);
                int hour = 0, minute = 0;
                String cate =cursor.getString(1);
                cal.set(2016, Integer.parseInt(cursor.getString(4).substring(0, 2)) - 1, Integer.parseInt(cursor.getString(4).substring(3, 5)) - 1);
                String[] str = new String[7];
                for (int i = 0; i < 7; i++) {
                    str[i] = nextday(cal, 1);
                }

                while (cursor1.moveToNext()) {
                    for (int i = 0; i < 7; i++) {
                        if (cursor1.getString(4).substring(0, 10).equals(str[i])&&cate.equals(cursor1.getString(5))) {
                            minute += cursor1.getInt(9) * 60 + cursor1.getInt(10);
                        }
                    }
                }

                if(goalTime < minute)
                    result.setText(minute/60+"시간 "+minute%60+"분으로 목표 완료함");
                else{
                    int year,year1, month,month1 ,day, day1;

                    long now = System.currentTimeMillis();
                    // 현재 시간을 저장 한다.
                    Date date = new Date(now);
                    // 시간 포맷으로 만든다.
                    SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd");
                    String strNow = sdfNow.format(date);

                    year=Integer.parseInt(str[6].substring(0,4));
                    month = Integer.parseInt(str[6].substring(5,7));
                    day = Integer.parseInt(str[6].substring(8,10));
                    year1=Integer.parseInt(strNow.substring(0,4));
                    month1 = Integer.parseInt(strNow.substring(5,7));
                    day1= Integer.parseInt(strNow.substring(8,10));

                    if(year1>year )
                        result.setText(minute/60+"시간 "+minute%60+"분으로 목표를 완료하지 못함");
                    else if(month1>month)
                        result.setText(minute/60+"시간 "+minute%60+"분으로 목표를 완료하지 못함");
                    else if(day1>day)
                        result.setText(minute/60+"시간 "+minute%60+"분으로 목표를 완료하지 못함");
                    else
                        result.setText(minute/60+"시간 "+minute%60+"분으로 진행중");
                }
                break;
            }

        }


    }

    public String nextday(Calendar cal, int i) {
        cal.add(Calendar.DATE, +i);
        java.util.Date weekago = cal.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd",
                Locale.getDefault());
        String str = formatter.format(weekago);
        Log.v("dd", str);
        return str;

    }
}
