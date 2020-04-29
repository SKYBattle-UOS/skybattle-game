package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class SelectCharacterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_character);
        Button btn_character1 = (Button) findViewById(R.id.btn_character1);
        btn_character1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ready_intent = new Intent(SelectCharacterActivity.this, GetReadyActivity.class);
                startActivity(ready_intent);
                finish();
            }
        });

        Button btn_character2 = (Button) findViewById(R.id.btn_character2);
        btn_character2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ready_intent = new Intent(SelectCharacterActivity.this, GetReadyActivity.class);
                startActivity(ready_intent);
                finish();
            }
        });

        Button btn_character3 = (Button) findViewById(R.id.btn_character3);
        btn_character3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ready_intent = new Intent(SelectCharacterActivity.this, GetReadyActivity.class);
                startActivity(ready_intent);
                finish();
            }
        });

        Button btn_character4 = (Button) findViewById(R.id.btn_character4);
        btn_character4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ready_intent = new Intent(SelectCharacterActivity.this, GetReadyActivity.class);
                startActivity(ready_intent);
                finish();
            }
        });

        Button btn_character5 = (Button) findViewById(R.id.btn_character5);
        btn_character5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ready_intent = new Intent(SelectCharacterActivity.this, GetReadyActivity.class);
                startActivity(ready_intent);
                finish();
            }
        });
    }
}