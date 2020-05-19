package com.example.Client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class RoomActivity extends AppCompatActivity implements RoomScreen {
    private TextView _roomTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        _roomTitle = findViewById(R.id.roomTitle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Core.getInstance().getUIManager().setCurrentScreen(this, ScreenType.ROOM);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Core.getInstance().getUIManager().setCurrentScreen(null, ScreenType.ROOM);
    }

    @Override
    public void switchTo(ScreenType type) {
        if (type == ScreenType.ASSEMBLE){
            Intent room_intent = new Intent(RoomActivity.this, MatchActivity.class);
            startActivity(room_intent);
            finish();
        }
    }

    @Override
    public void setTitle(String title) {
        _roomTitle.setText(title);
    }
}