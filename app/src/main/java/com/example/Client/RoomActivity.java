package com.example.Client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import android.widget.Button;

import android.widget.TextView;
import org.w3c.dom.Text;
import java.util.ArrayList;


public class RoomActivity extends AppCompatActivity implements RoomScreen {
    private TextView _roomTitle;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        _roomTitle = findViewById(R.id.roomTitle);

        listView = (ListView) this.findViewById(R.id.playerlist);

        ArrayList<String> items = new ArrayList<>(); //닉네임리스트를 저장할 배열

        Button btn_nickname = (Button)findViewById(R.id.btn_nickname);
        btn_nickname.setOnClickListener(new View.OnClickListener() { //입력란에 닉네임 입력 후, '등록'버튼을 누르면 실행하는 메소드
            @Override
            public void onClick(View v) {
                CustomAdapter adapter = new CustomAdapter(RoomActivity.this, 0, items);
                listView.setAdapter((adapter));
                EditText edit_nickname = findViewById(R.id.edit_nickname);
                items.add(edit_nickname.getText().toString()); //배열에 입력란에서 받은 닉네임을 추가
                edit_nickname.setText(null); //닉네임 입력 후, 다음 입력 시에 리셋
            }
        });


        Button btn_exit= (Button)findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent room_intent = new Intent(RoomActivity.this, MainActivity.class);
                startActivity(room_intent);
                finish();
        }
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
            Button btn_teamA = (Button) v.findViewById(R.id.btn_teamA);
            btn_teamA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    roleView.setText("팀A");
                }
            });

            Button btn_btn_teamB = (Button) v.findViewById(R.id.btn_teamB);
            btn_teamB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    roleView.setText("팀B");
                }
            });

            return v;
        }

        Button btn_start = findViewById(R.id.startButton);
        btn_start.setOnClickListener(v -> Core.getInstance().getUIManager().invoke(UIManager.ROOM_START_PORT));
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
        if (type == ScreenType.ASSEMBLE) {
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