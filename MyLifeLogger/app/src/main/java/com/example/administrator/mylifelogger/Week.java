package com.example.administrator.mylifelogger;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Week extends FragmentActivity  implements View.OnClickListener{
    Spinner selMonth, selDay;
    SQLiteDatabase db;
    Button serBtn;
    TextView text,lastday;
    ListView listview;
    ListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);

        db = SQLiteDatabase.openDatabase("/data/data/com.example.administrator.mylifelogger/myDB", null, SQLiteDatabase.CREATE_IF_NECESSARY);

        selMonth = (Spinner)findViewById(R.id.selMonth);
        selDay = (Spinner)findViewById(R.id.selDay);

        serBtn = (Button)findViewById(R.id.searchBtn2);
        serBtn.setOnClickListener(this);

        text = (TextView)findViewById(R.id.text);
        lastday = (TextView)findViewById(R.id.lastday);

        adapter = new ListViewAdapter();
        listview = (ListView)findViewById(R.id.listview3);

        listview.setAdapter(adapter);
        init();

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //   Toast.makeText(getApplication(),adapter.getdbId(position),Toast.LENGTH_SHORT).show();
                Log.v("dd", String.valueOf(adapter.getdbId(position)));
                int n =  adapter.getdbId(position);
                Intent intent = new Intent(getApplicationContext(),ViewRecord.class);
                intent.putExtra("id",n);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onClick(View view) {
        Cursor cursor = db.rawQuery("select * from life", null);
        String str = "";
        str = selMonth.getSelectedItem().toString() + "/" + selDay.getSelectedItem().toString();
        int result = 0;
        removeList();
        Calendar cal = Calendar
                .getInstance();
        int studymin=0,exercisemin=0,cafemin=0,foodmin=0,travelmin=0,gamemin=0,moviemin=0;
       // Log.v("dddd",get)
        cal.set(2016, Integer.parseInt(str.substring(0,2)) - 1, Integer.parseInt(str.substring(3,5)) - 1);
        String[] date = new String[7];
        for (int i = 0; i < 7; i++) {
            date[i] = nextday(cal, 1);
        }
        lastday.setText("   ~"+date[6].substring(5,7)+"월 "+date[6].substring(8,10)+"까지");
        while (cursor.moveToNext()) {
            for (int i = 0; i < 7; i++) {
                if (date[i].equals(cursor.getString(4).substring(0, 10))) {
                    switch (cursor.getString(5)) {
                        case "공부":
                            adapter.addItem(ContextCompat.getDrawable(this, R.drawable.book), cursor.getString(6), cursor.getString(9) + "시간 " + cursor.getString(10) + "분", cursor.getInt(0));
                            studymin+=Integer.parseInt(cursor.getString(9))*60+Integer.parseInt(cursor.getString(10));
                            break;
                        case "운동":
                            adapter.addItem(ContextCompat.getDrawable(this, R.drawable.exercise), cursor.getString(6), cursor.getString(9) + "시간 " + cursor.getString(10) + "분", cursor.getInt(0));
                            exercisemin+=Integer.parseInt(cursor.getString(9))*60+Integer.parseInt(cursor.getString(10));
                            break;
                        case "카페":
                            adapter.addItem(ContextCompat.getDrawable(this, R.drawable.coffee), cursor.getString(6), cursor.getString(9) + "시간 " + cursor.getString(10) + "분", cursor.getInt(0));
                            cafemin+=Integer.parseInt(cursor.getString(9))*60+Integer.parseInt(cursor.getString(10));
                            break;
                        case "식사":
                            adapter.addItem(ContextCompat.getDrawable(this, R.drawable.food), cursor.getString(6), cursor.getString(9) + "시간 " + cursor.getString(10) + "분", cursor.getInt(0));
                            foodmin+=Integer.parseInt(cursor.getString(9))*60+Integer.parseInt(cursor.getString(10));
                            break;
                        case "여행":
                            adapter.addItem(ContextCompat.getDrawable(this, R.drawable.travel), cursor.getString(6), cursor.getString(9) + "시간 " + cursor.getString(10) + "분", cursor.getInt(0));
                            travelmin+=Integer.parseInt(cursor.getString(9))*60+Integer.parseInt(cursor.getString(10));
                            break;
                        case "게임":
                            adapter.addItem(ContextCompat.getDrawable(this, R.drawable.game), cursor.getString(6), cursor.getString(9) + "시간 " + cursor.getString(10) + "분", cursor.getInt(0));
                            gamemin+=Integer.parseInt(cursor.getString(9))*60+Integer.parseInt(cursor.getString(10));
                            break;
                        case "영화":
                            adapter.addItem(ContextCompat.getDrawable(this, R.drawable.camera), cursor.getString(6), cursor.getString(9) + "시간 " + cursor.getString(10) + "분", cursor.getInt(0));
                            moviemin+=Integer.parseInt(cursor.getString(9))*60+Integer.parseInt(cursor.getString(10));
                            break;
                    }
                    result = 1;

                }
                adapter.notifyDataSetChanged();
            }
        }
        if (result == 0) {
            Toast.makeText(getBaseContext(), "결과가 없습니다.", Toast.LENGTH_SHORT).show();
            text.setText("");
            removeList();
        }
        else{
            String concat="";
            if(studymin!=0)
                concat+= "공부 "+studymin/60+ "시간 "+studymin%60+"분\n";
            if(exercisemin!=0)
                concat += "운동 "+ exercisemin/60+ "시간 "+exercisemin%60+"분\n";
            if (cafemin!=0)
                concat += "카페 "+ cafemin/60+ "시간 "+cafemin%60+"분\n";
            if(foodmin!=0)
                concat+="식사 "+foodmin/60 +"시간 "+foodmin%60+"분\n";
            if(travelmin!=0)
                concat += "여행 "+travelmin/60+"시간 "+travelmin%60+"분\n";
            if(gamemin!=0)
                concat += "게임 "+gamemin/60+"시간 "+gamemin%60+"분\n";
            if(moviemin!=0)
                concat +="영화 "+moviemin/60+"시간"+moviemin%60+"분\n";

            text.setText(concat);

        }
    }
    public void init(){
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
        }
        adapter.notifyDataSetChanged();
    }
    public void removeList() {
        for (int i = adapter.getCount(); i > 0; i--) {
            adapter.removeItem(i - 1);
        }
        adapter.notifyDataSetChanged();
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
