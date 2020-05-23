package com.example.Client;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

/**
 * Activity 초기화면
 */
public class SplashActivity  extends AppCompatActivity {

    public static final int PERMISSIONS_REQUEST_CODE = 100;    //앱 위치 사용 requestCode
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceStare) {
        super.onCreate(savedInstanceStare);
        setContentView(R.layout.activity_splash);

        if (!checkLocationServicesStatus()) {   //gps 기능이 켜져있는지 확인
            showDialogForLocationServiceSetting();
        }
        checkRunTimePermission();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 500);
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

    //런타임 퍼미션 처리 0
    void checkRunTimePermission(){
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.

        ArrayList<String> targetList = new ArrayList<String>();

        for (int i=0; i< REQUIRED_PERMISSIONS.length; i++ ){
            String currentPermission = REQUIRED_PERMISSIONS[i];
            int permissionCheck = ContextCompat.checkSelfPermission(this, currentPermission);

            if(permissionCheck != PackageManager.PERMISSION_GRANTED){
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[i])) {
                    // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있다.
                    Toast.makeText(this, currentPermission + " 어플 사용을 위하여 위치권한이 필요합니다.",Toast.LENGTH_LONG).show();
                }else
                    targetList.add(currentPermission);
            }
        }

        String[] targets = new String[targetList.size()];
        targetList.toArray(targets);

        if(targets.length > 0)
            ActivityCompat.requestPermissions(this, targets, PERMISSIONS_REQUEST_CODE);
    }
}