package com.example.Client;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

public class MainActivity extends AppCompatActivity implements Screen, AutoPermissionsListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!checkLocationServicesStatus()) {   //gps 기능이 켜져있는지 확인
            showDialogForLocationServiceSetting();
        }

        Core.createInstance(getApplicationContext());

        Button btn_entrance = findViewById(R.id.btn_entrance);
        btn_entrance.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("IP주소 입력");
                alert.setMessage("IP주소를 입력하시오.");

                final EditText ipaddr=new EditText(MainActivity.this);
                alert.setView(ipaddr);

                alert.setPositiveButton("입력", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /*"입력" 버튼 클릭 시 실행하는 메소드*/
                        Core.getInstance().getUIManager().invoke(UIManager.ENTER_ROOM_PORT);
                    }
                });
                alert.show();
            }
        });

        Button btn_makeroom = findViewById(R.id.btn_makeroom);
        btn_makeroom.setOnClickListener(v -> Core.getInstance().getUIManager().invoke(UIManager.ENTER_ROOM_PORT));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Core.getInstance().getUIManager().setCurrentScreen(this, ScreenType.MAIN);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Core.getInstance().getUIManager().setCurrentScreen(null, ScreenType.MAIN);
    }

    @Override
    public void switchTo(ScreenType type) {
        if (type == ScreenType.ROOM){
            Toast.makeText(getApplicationContext(), "방에 입장합니다", Toast.LENGTH_LONG).show();
            Intent entrance_intent = new Intent(MainActivity.this, RoomActivity.class);
            startActivity(entrance_intent);
            finish();
        }
    }

    @Override
    public void onDenied(int i, String[] strings) {
    }

    @Override
    public void onGranted(int i, String[] strings) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AutoPermissions.Companion.parsePermissions(this, requestCode, permissions, this);
        Toast.makeText(this, "requestCode : "+requestCode+"  permissions : "+permissions+"  grantResults :"+grantResults, Toast.LENGTH_SHORT).show();
    }


    //위치 서비스 사용이 가능한지 불가능한지 0
    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정부탁드립니다.");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, Location.GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }
}