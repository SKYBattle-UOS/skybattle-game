package com.example.Client;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import Host.CoreHost;

public class MainActivity extends AppCompatActivity implements MainScreen, AutoPermissionsListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn_entrance = findViewById(R.id.btn_entrance);

        btn_entrance.setOnClickListener(v -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            alert.setTitle("IP주소 입력");
            alert.setMessage("IP주소를 입력하시오.");

            final EditText ip_addr = new EditText(MainActivity.this);
            ip_addr.setId(R.id.IPAddrDialog);
            alert.setView(ip_addr);

            alert.setPositiveButton("입력", (dialog, which) -> {
                String host = ((EditText) ((AlertDialog) dialog).findViewById(R.id.IPAddrDialog))
                        .getText().toString();
                Core.get().open(host);
            });
            alert.show();
        });

        Button btn_makeroom = findViewById(R.id.btn_makeroom);
        btn_makeroom.setOnClickListener(v -> {
            CoreHost.createInstance();
            Core.get().open("localhost");
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Core.get().getUIManager().setCurrentScreen(this, ScreenType.MAIN);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Core.get().getUIManager().setCurrentScreen(null, ScreenType.MAIN);
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
    public void onHostNotFound(){
        Log.i("hehe", "not connected");
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
}