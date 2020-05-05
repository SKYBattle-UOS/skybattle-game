package com.example.Client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class SelectCharacterActivity extends AppCompatActivity implements Screen {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_character);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Core.getInstance().getUIManager().setCurrentScreen(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Core.getInstance().getUIManager().setCurrentScreen(null);
    }

    @Override
    public void switchTo(ScreenType type) {
        if (type == ScreenType.GETREADY){
            Intent ready_intent = new Intent(SelectCharacterActivity.this, GetReadyActivity.class);
            startActivity(ready_intent);
            finish();
        }
    }
}