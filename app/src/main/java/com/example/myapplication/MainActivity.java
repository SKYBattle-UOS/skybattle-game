package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            Core.createInstance(getApplicationContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Core.getInstance().run();

        Button btn_entrance = (Button) findViewById(R.id.btn_entrance);
        btn_entrance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "방에 입장합니다", Toast.LENGTH_LONG).show();
                Intent entrance_intent = new Intent(MainActivity.this, RoomActivity.class);
                startActivity(entrance_intent);
                finish();
            }
        });
    }
}