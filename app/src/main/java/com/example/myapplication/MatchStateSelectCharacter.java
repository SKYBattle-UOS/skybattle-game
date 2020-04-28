package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * 매치의 각 화면에 대한 상태패턴의 상태 객체 중 캐릭터 선택 화면.
 *
 * @author Korimart
 * @version 0.0
 * @since 2020-04-21
 */
public class MatchStateSelectCharacter extends AppCompatActivity implements GameState {
    @Override
    public void update(int ms) {
        // TODO: DEBUG EDIT
        Log.i("Stub", "MatchStateSelectCharacter: Showing Character Selection Screen");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_state_select_character);
        Button btn_character1 = (Button) findViewById(R.id.btn_character1);
        btn_character1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ready_intent = new Intent(MatchStateSelectCharacter.this, MatchStateGetReady.class);
                startActivity(ready_intent);
                finish();
            }
        });

        Button btn_character2 = (Button) findViewById(R.id.btn_character2);
        btn_character2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ready_intent = new Intent(MatchStateSelectCharacter.this, MatchStateGetReady.class);
                startActivity(ready_intent);
                finish();
            }
        });

        Button btn_character3 = (Button) findViewById(R.id.btn_character3);
        btn_character3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ready_intent = new Intent(MatchStateSelectCharacter.this, MatchStateGetReady.class);
                startActivity(ready_intent);
                finish();
            }
        });

        Button btn_character4 = (Button) findViewById(R.id.btn_character4);
        btn_character4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ready_intent = new Intent(MatchStateSelectCharacter.this, MatchStateGetReady.class);
                startActivity(ready_intent);
                finish();
            }
        });

        Button btn_character5 = (Button) findViewById(R.id.btn_character5);
        btn_character5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ready_intent = new Intent(MatchStateSelectCharacter.this, MatchStateGetReady.class);
                startActivity(ready_intent);
                finish();
            }
        });
    }
}
