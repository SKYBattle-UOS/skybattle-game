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

        Core.createInstance(getApplicationContext());

        Button btn_entrance = findViewById(R.id.btn_entrance);

        btn_entrance.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("IP주소 입력");
                alert.setMessage("IP주소를 입력하시오.");

                final EditText ip_addr = new EditText(MainActivity.this);
                alert.setView(ip_addr);

                alert.setPositiveButton("입력", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        /*"입력" 버튼 클릭 시 실행하는 메소드*/
                        Core.getInstance().getUIManager().invoke(AndroidUIManager.ENTER_ROOM_PORT);
                    }
                });
                alert.show();
            }
        });

        Button btn_makeroom = findViewById(R.id.btn_makeroom);
        btn_makeroom.setOnClickListener(v -> Core.getInstance().getUIManager().invoke(AndroidUIManager.ENTER_ROOM_PORT));
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


}