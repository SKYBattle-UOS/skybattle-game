package com.example.Client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import android.widget.TextView;

import java.util.ArrayList;

import Common.GameState;
import Common.RoomUserInfo;
import Host.CoreHost;

public class RoomActivity extends AppCompatActivity implements Screen {
    private TextView _roomTitle;
    private ListView _listView;
    private CustomAdapter _listViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        _roomTitle = findViewById(R.id.roomTitle);

        AndroidUIManager uiManager = (AndroidUIManager) Core.get().getUIManager();
        GameStateRoom roomState = (GameStateRoom) Core.get().getState();

        uiManager.getTitleText().observe(this, text -> _roomTitle.setText(text));
        uiManager.getRoomUserInfos().observe(this, infos -> {
            _listViewAdapter.clear();
            _listViewAdapter.addAll(infos);
            _listViewAdapter.notifyDataSetChanged();
        });

        EditText editTitle = findViewById((R.id.editTitle));
        findViewById(R.id.editTitleButton).setOnClickListener(
                v -> {
                    if (editTitle.getText().toString().replace(" ", "").equals("")) { }
                    else{
                        roomState.changeRoomTitle(editTitle.getText().toString());
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(editTitle.getWindowToken(), 0);
                        editTitle.setText(null);
                    }
                });

        if (!Core.get().isHost()){
            LinearLayout roomLinLayout = findViewById(R.id.roomLinLayout);
            roomLinLayout.removeView(editTitle);
            roomLinLayout.removeView(findViewById(R.id.editTitleButton));
        }

        _listView = findViewById(R.id.playerlist);
        _listViewAdapter = new CustomAdapter(this, 0, new ArrayList<>());
        _listView.setAdapter(_listViewAdapter);

        Button userNameButton = findViewById(R.id.btn_nickname);
        userNameButton.setOnClickListener(v -> {
            EditText userName = findViewById(R.id.edit_nickname);
            if (userName.getText().toString().replace(" ", "").equals("")) { }

            else {
                //등록 후, 키보드가 숨겨지도록 함
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(userName.getWindowToken(), 0);

                ((GameStateRoom) Core.get().getState()).setUserName(userName.getText().toString());
                userName.setText(null);
            }
        });

        //게임 시작 버튼
        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(v -> {
            roomState.startGame();
            startButton.setEnabled(false);
        });

        if (!Core.get().isHost()) startButton.setEnabled(false);

        // 나가기 버튼
        Button exitButton = findViewById(R.id.exitButton);
        exitButton.setOnClickListener(v -> {
            exitButton.setEnabled(false);
            if (Core.get().isHost())
                CoreHost.destroyInstance();
            Core.get().close();
        });

        //Button teamA = findViewById(R.id.teamAButton);
        //Button teamB = findViewById(R.id.teamBButton);
        //teamA.setOnClickListener(v -> roomState.setTeam(0));
        //teamB.setOnClickListener(v -> roomState.setTeam(1));
}

  /*한 화면에 리스트뷰, 텍스트뷰, 버튼을 표현하기 위해 커스텀 어댑터 사용*/
    private class CustomAdapter extends ArrayAdapter<RoomUserInfo> {
        public CustomAdapter(Context context, int textViewResourcedId, ArrayList<RoomUserInfo> objects) {
            super(context, textViewResourcedId, objects);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater v1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = v1.inflate(R.layout.listview_player, null);
            }

            final TextView userName = v.findViewById(R.id.userName);
            userName.setText(getItem(position).name);

            //final TextView roleView = v.findViewById(R.id.rolelist);
            //roleView.setText(getItem(position).team == 0 ? "팀A" : "팀B");

            return v;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Core.get().getUIManager().setCurrentScreen(this, ScreenType.ROOM);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Core.get().getUIManager().setCurrentScreen(null, ScreenType.ROOM);
    }

    @Override
    public void switchTo(ScreenType type) {
        if (type == ScreenType.MAP) {
            Intent room_intent = new Intent(RoomActivity.this, MatchActivity.class);
            startActivity(room_intent);
            finish();
        }
        else if (type == ScreenType.MAIN){
            Intent room_intent = new Intent(RoomActivity.this, MainActivity.class);
            startActivity(room_intent);
            finish();
        }
    }
}