package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 매치의 각 화면에 대한 상태패턴의 상태 객체 중 집합화면.
 *
 * @author Korimart
 * @version 0.0
 * @since 2020-04-21
 */
public class MatchStateAssemble extends AppCompatActivity implements GameState {
    @Override
    public void update(int ms) {
        // TODO: DEBUG EDIT
        Log.d("Stub", "MatchStateAssemble: Showing assemble screen");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_state_assemble);

        Button btn_pointer = (Button) findViewById(R.id.btn_pointer);
        btn_pointer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selection_intent = new Intent(MatchStateAssemble.this, MatchStateSelectCharacter.class);
                startActivity(selection_intent);
                finish();
            }
        });
    }
}
