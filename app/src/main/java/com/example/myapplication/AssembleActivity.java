package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AssembleActivity extends AppCompatActivity implements GameState {
    @Override
    public void update(int ms) {
        // TODO: DEBUG EDIT
        Log.d("Stub", "MatchStateAssemble: Showing assemble screen");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assemble);

        Button btn_pointer = (Button) findViewById(R.id.btn_pointer);
        btn_pointer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selection_intent = new Intent(AssembleActivity.this, SelectCharacterActivity.class);
                startActivity(selection_intent);
                finish();
            }
        });
    }
}