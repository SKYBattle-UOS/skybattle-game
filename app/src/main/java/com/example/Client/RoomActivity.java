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


public class RoomActivity extends AppCompatActivity implements Screen {
    private TextView _roomTitle;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        _roomTitle = findViewById(R.id.roomTitle);

        Core.getInstance().getUIManager().getTitleText().observe(this, text -> _roomTitle.setText(text));

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
        
        //시작 버튼
        Button btn_start = findViewById(R.id.startButton);
        btn_start.setOnClickListener(v -> Core.getInstance().getUIManager().invoke(UIManager.ROOM_START_PORT));

        //나가기 버튼: 클릭 시, 이전 화면으로 돌아감. 닉네임 리스트에서 나가기 버튼을 클릭한 사용자가 사라지도록 해야함.(추가예정)
        Button btn_exit= findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(v -> Core.getInstance().getUIManager().invoke(UIManager.EXIT_ROOM_PORT));
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
        else if (type == ScreenType.MAIN){
            Intent room_intent = new Intent(RoomActivity.this, MainActivity.class);
            startActivity(room_intent);
            finish();
        }
    }
}
