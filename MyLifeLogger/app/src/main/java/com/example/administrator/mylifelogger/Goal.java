package com.example.administrator.mylifelogger;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
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
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Goal extends FragmentActivity implements View.OnClickListener {

    private Button addBtn, refreshBtn;
    ListView listview;
    ListViewAdapter adapter;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            db = SQLiteDatabase.openDatabase("/data/data/com.example.administrator.mylifelogger/myDB",
                    null, SQLiteDatabase.CREATE_IF_NECESSARY);
            db.execSQL("create table goal ( recID integer PRIMARY KEY autoincrement, category STRING, hour INTEGER, minutes INTEGER, mdate STRING, title STRING, content STRING );  ");
            Toast.makeText(getBaseContext(), "하하", Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
        }
        setContentView(R.layout.activity_goal);

        addBtn = (Button) findViewById(R.id.button);
        addBtn.setOnClickListener(this);

        refreshBtn = (Button) findViewById(R.id.refreshBtn);
        refreshBtn.setOnClickListener(this);

        adapter = new ListViewAdapter();

        // 리스트뷰 참조 및 Adapter달기
        listview = (ListView) findViewById(R.id.listView2);
        listview.setAdapter(adapter);

        getList();

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //   Toast.makeText(getApplication(),adapter.getdbId(position),Toast.LENGTH_SHORT).show();
                Log.v("dd", String.valueOf(adapter.getdbId(position)));
                int n =  adapter.getdbId(position);
                Intent intent = new Intent(getApplicationContext(),ViewGoal.class);
                intent.putExtra("id",n);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                Intent intent = new Intent(getApplicationContext(), GoalAdd.class);
                startActivity(intent);
                getList();
                break;
            case R.id.refreshBtn:
                getList();
                break;
        }
    }

    public void removeList() {
        for (int i = adapter.getCount(); i > 0; i--) {
            adapter.removeItem(i - 1);
        }
        adapter.notifyDataSetChanged();
    }
    public void getList(){

        Cursor cursor = db.rawQuery("select * from goal", null);
        //category STRING, hour INTEGER, minutes INTEGER, mdate STRING, title STRING, content STRING

        removeList();
        while (cursor.moveToNext()) {

            switch (cursor.getString(1)) {
                case "공부":
                    adapter.addItem(ContextCompat.getDrawable(this, R.drawable.book), cursor.getString(5), cursor.getString(2) + "시간 " + cursor.getString(3) + "분", cursor.getInt(0));
                    break;
                case "운동":
                    adapter.addItem(ContextCompat.getDrawable(this, R.drawable.exercise), cursor.getString(5), cursor.getString(2) + "시간 " + cursor.getString(3) + "분", cursor.getInt(0));
                    break;
                case "카페":
                    adapter.addItem(ContextCompat.getDrawable(this, R.drawable.coffee), cursor.getString(5), cursor.getString(2) + "시간 " + cursor.getString(3) + "분", cursor.getInt(0));
                    break;
                case "식사":
                    adapter.addItem(ContextCompat.getDrawable(this, R.drawable.food), cursor.getString(5), cursor.getString(2) + "시간 " + cursor.getString(3) + "분", cursor.getInt(0));
                    break;
                case "여행":
                    adapter.addItem(ContextCompat.getDrawable(this, R.drawable.travel), cursor.getString(5), cursor.getString(2) + "시간 " + cursor.getString(3) + "분", cursor.getInt(0));
                    break;
                case "게임":
                    adapter.addItem(ContextCompat.getDrawable(this, R.drawable.game), cursor.getString(5), cursor.getString(2) + "시간 " + cursor.getString(3) + "분", cursor.getInt(0));
                    break;
                case "영화":
                    adapter.addItem(ContextCompat.getDrawable(this, R.drawable.camera), cursor.getString(5), cursor.getString(2) + "시간 " + cursor.getString(3) + "분", cursor.getInt(0));
                    break;
            }
            adapter.notifyDataSetChanged();
        }
    }
}
