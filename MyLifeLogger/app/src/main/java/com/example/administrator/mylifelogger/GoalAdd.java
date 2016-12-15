package com.example.administrator.mylifelogger;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class GoalAdd extends FragmentActivity implements View.OnClickListener {
    private Button btn,delBtn;
    private Spinner caSpin, hourSpin, minSpin, monthSpin, daySpin;
    private EditText title, content;
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_add);

        btn = (Button) findViewById(R.id.button2);
        btn.setOnClickListener(this);

        delBtn = (Button)findViewById(R.id.button3);
        delBtn.setOnClickListener(this);

        caSpin = (Spinner) findViewById(R.id.spinner6);
        hourSpin = (Spinner) findViewById(R.id.spinner7);
        minSpin = (Spinner) findViewById(R.id.spinner8);
        monthSpin = (Spinner) findViewById(R.id.spinner9);
        daySpin = (Spinner) findViewById(R.id.spinner10);

        title = (EditText) findViewById(R.id.editText3);
        content = (EditText) findViewById(R.id.editText4);

        try {
            db = SQLiteDatabase.openDatabase("/data/data/com.example.administrator.mylifelogger/myDB",
                    null, SQLiteDatabase.CREATE_IF_NECESSARY);
            db.execSQL("create table goal ( recID integer PRIMARY KEY autoincrement, category STRING, hour INTEGER, minutes INTEGER, mdate STRING, title STRING, content STRING );  ");
            Toast.makeText(getBaseContext(), "하하", Toast.LENGTH_SHORT).show();
        } catch (SQLException e) {
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button2:
                String str = monthSpin.getSelectedItem() + "/" + daySpin.getSelectedItem();
                db.execSQL("insert into goal (category , hour , minutes , mdate , title , content ) values('" + caSpin.getSelectedItem() + "','" + hourSpin.getSelectedItem() + "','" + minSpin.getSelectedItem() + "','" + str + "','" + title.getText().toString() + "','" + content.getText().toString() + "' );");
                title.setText("");
                content.setText("");
                Toast.makeText(getBaseContext(), "저장이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button3:
                db.execSQL("drop table goal");
                Toast.makeText(getBaseContext(),"삭제하였습니다.",Toast.LENGTH_SHORT).show();
                break;
        }

    }
}
