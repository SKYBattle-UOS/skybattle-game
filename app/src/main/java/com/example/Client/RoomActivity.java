package com.example.Client;

import androidx.appcompat.app.AlertDialog;
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
import android.widget.ListView;

import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import Host.CoreHost;


public class RoomActivity extends AppCompatActivity implements Screen {
    private TextView _roomTitle;
    private ListView _listView;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        _roomTitle = findViewById(R.id.roomTitle);

        ((AndroidUIManager) Core.get().getUIManager())
                .getTitleText().observe(this, text -> _roomTitle.setText(text));

        EditText editTitle = findViewById((R.id.editTitle));
        findViewById(R.id.editTitleButton).setOnClickListener(v -> {
            ((GameStateRoom) Core.get().getState()).changeRoomTitle(
                    editTitle.getText().toString()
            );
        });

        ArrayList<String> items = new ArrayList<>(); //닉네임리스트를 저장할 배열

        listView = this.findViewById(R.id.playerlist);

        //닉네임 길게 누르면 삭제됨
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            CustomAdapter adapter = new CustomAdapter(RoomActivity.this, 0, items);
            listView.setAdapter((adapter));
            items.remove(position);
            Toast.makeText(getApplicationContext(), "닉네임이 삭제되었습니다.", Toast.LENGTH_LONG).show();
            adapter.notifyDataSetChanged();
            return true;
        });

        //닉네임 등록 버튼
        Button btn_nickname = findViewById(R.id.btn_nickname);

        //입력란에 닉네임 입력 후, '등록'버튼을 누르면 실행하는 메소드
        btn_nickname.setOnClickListener(v -> {
            CustomAdapter adapter = new CustomAdapter(RoomActivity.this, 0, items);
            listView.setAdapter((adapter));
            EditText edit_nickname = findViewById(R.id.edit_nickname);
            if (edit_nickname.getText().toString().replace(" ", "").equals("")) { }
            else {
                items.add(edit_nickname.getText().toString()); //배열에 입력란에서 받은 닉네임 등록
                adapter.notifyDataSetChanged();

                //등록 후, 키보드가 숨겨지도록 함
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(edit_nickname.getWindowToken(), 0);

                edit_nickname.setText(null); //닉네임 입력 후, 다음 입력 시에 리셋

            }
        });

        //게임 시작 버튼
        Button btn_start = findViewById(R.id.startButton);
        btn_start.setOnClickListener(
                v -> ((GameStateRoom) Core.get().getState()).startGame());

        if (!Core.get().isHost()) btn_start.setEnabled(false);

        //방 나가기 버튼: 클릭 시, 이전 화면으로 돌아감. 돌아가기 전에 닉네임 삭제여부를 묻는다.
        Button btn_exit = findViewById(R.id.exitButton);
        btn_exit.setOnClickListener(v -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(RoomActivity.this);
            alert.setTitle("잠깐!");
            alert.setMessage("닉네임을 삭제하셨습니까?           (닉네임을 길게 누르면 삭제됩니다.)");

            alert.setPositiveButton("예",
                    (dialog, which) -> {
                        if (Core.get().isHost())
                            CoreHost.destroyInstance();
                        Core.get().close();
                    });

            alert.setNegativeButton("아니오", (dialog, id) -> dialog.cancel());
            alert.show();
        });
}

  /*한 화면에 리스트뷰, 텍스트뷰, 버튼을 표현하기 위해 커스텀 어댑터 사용*/
    private class CustomAdapter extends ArrayAdapter<String> {
        private ArrayList<String> items;

        public CustomAdapter(Context context, int textViewResourcedId, ArrayList<String> objects) {
            super(context, textViewResourcedId, objects);
            this.items = objects;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater v1 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = v1.inflate(R.layout.listview_player, null);
            }

            final TextView nicknamelist = (TextView) v.findViewById(R.id.nicknamelist);
            nicknamelist.setText(items.get(position)); //닉네임 리스트를 리스트뷰에 보여줌

            final TextView roleView = (TextView) v.findViewById(R.id.rolelist);

            final String text = items.get(position);
            //팀A 또는 B 버튼 클릭 시, 닉네임 리스트 옆에 팀 표시됨
            Button btn_teamA = (Button) v.findViewById(R.id.btn_teamA);
            btn_teamA.setOnClickListener(new View.OnClickListener() { 
                @Override
                public void onClick(View v) {
                    roleView.setText("팀A");
                }
            });

            Button btn_teamB = (Button) v.findViewById(R.id.btn_teamB);
            btn_teamB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    roleView.setText("팀B");
                }
            });

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
