package com.example.administrator.mylifelogger;



import android.*;
import android.Manifest;
import android.app.TabActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TabHost;
import android.widget.Toast;

public class MainActivity extends  TabActivity   {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkDangerousPermissions();

        // 이 탭액티비티에서 사용할 수 있는 TabHost 객체를 얻는다.
        TabHost tab_host = getTabHost();
        // 각 탭에 사용할 TabSpec 객체.
        // 탭호스트에 TabWidget 과 FrameLayout 이 사용할 정보를 넘겨주는 역할을 한다.
        TabHost.TabSpec spec;
        // 각 탭의 FrameLayout 이 사용하는 액티비티를 구성하는 객체
        Intent intent;

        // 탭에서 액티비티를 사용할 수 있도록 인텐트를 생성한다.
        intent = new Intent().setClass(this, Record.class);
        // "일지 기록" 이라는 태그 값을 가진 TabSpec 객체를 생성한다.
        spec = tab_host.newTabSpec("일지 기록");
        // TabSpec 객체에 TabWidget 객체가 출력할 탭의 이름을 설정한다.
        spec.setIndicator("일지 기록");
        // TabSpec 객체에 FrameLayout 이 출력할 페이지를 설정한다.
        spec.setContent(intent);
        // 탭호스트에 해당 정보를 가진 탭을 추가한다.
        tab_host.addTab(spec);

        // 탭에서 액티비티를 사용할 수 있도록 인텐트를 생성한다.
        intent = new Intent().setClass(this, DayStat.class);
        // "통계" 이라는 태그 값을 가진 TabSpec 객체를 생성한다.
        spec = tab_host.newTabSpec("하루 통계");
        // TabSpec 객체에 TabWidget 객체가 출력할 탭의 이름을 설정한다.
        spec.setIndicator("하루 통계");
        // TabSpec 객체에 FrameLayout 이 출력할 페이지를 설정한다.
        spec.setContent(intent);
        // 탭호스트에 해당 정보를 가진 탭을 추가한다.
        tab_host.addTab(spec);

        // 탭에서 액티비티를 사용할 수 있도록 인텐트를 생성한다.
        intent = new Intent().setClass(this, MapsActivity.class);
        // "통계" 이라는 태그 값을 가진 TabSpec 객체를 생성한다.
        spec = tab_host.newTabSpec("일주일 통계");
        // TabSpec 객체에 TabWidget 객체가 출력할 탭의 이름을 설정한다.
        spec.setIndicator("일주일 통계");
        // TabSpec 객체에 FrameLayout 이 출력할 페이지를 설정한다.
        spec.setContent(intent);
        // 탭호스트에 해당 정보를 가진 탭을 추가한다.
        tab_host.addTab(spec);

        // 탭에서 액티비티를 사용할 수 있도록 인텐트를 생성한다.
        intent = new Intent().setClass(this, Goal.class);
        // "통계" 이라는 태그 값을 가진 TabSpec 객체를 생성한다.
        spec = tab_host.newTabSpec("목표");
        // TabSpec 객체에 TabWidget 객체가 출력할 탭의 이름을 설정한다.
        spec.setIndicator("목표");
        // TabSpec 객체에 FrameLayout 이 출력할 페이지를 설정한다.
        spec.setContent(intent);
        // 탭호스트에 해당 정보를 가진 탭을 추가한다.
        tab_host.addTab(spec);

        // 첫번째 탭을 선택한 상태로 지정한다.
        tab_host.setCurrentTab(0);
    }
    //gps 허가
    private void checkDangerousPermissions() {
        String[] permissions = {
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
        };

        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int i = 0; i < permissions.length; i++) {
            permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                break;
            }
        }

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            //    Toast.makeText(this, "권한 있음", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "권한 없음", Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                Toast.makeText(this, "권한 설명 필요함.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this, permissions, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(this, permissions[i] + " 권한이 승인됨.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, permissions[i] + " 권한이 승인되지 않음.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}