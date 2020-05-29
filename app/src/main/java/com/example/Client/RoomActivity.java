package com.example.Client;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class RoomActivity extends AppCompatActivity implements Screen {
    private TextView _roomTitle;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        _roomTitle = findViewById(R.id.roomTitle);

        ((AndroidUIManager) Core.get().getUIManager())
                .getTitleText().observe(this, text -> _roomTitle.setText(text));

        ArrayList<String> items = new ArrayList<>(); //닉네임리스트를 저장할 배열

        listView = (ListView) this.findViewById(R.id.playerlist);

        //닉네임 길게 누르면 삭제됨
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                CustomAdapter adapter = new CustomAdapter(RoomActivity.this, 0, items);
                listView.setAdapter((adapter));
                items.remove(position);
                Toast.makeText(getApplicationContext(), "닉네임이 삭제되었습니다.", Toast.LENGTH_LONG).show();
                adapter.notifyDataSetChanged();
                return true;
            }
        });

        //닉네임 등록 버튼
        Button btn_nickname = (Button)findViewById(R.id.btn_nickname);

        btn_nickname.setOnClickListener(new View.OnClickListener() { //입력란에 닉네임 입력 후, '등록'버튼을 누르면 실행하는 메소드
            @Override
            public void onClick(View v) {
                CustomAdapter adapter = new CustomAdapter(RoomActivity.this, 0, items);
                listView.setAdapter((adapter));
                EditText edit_nickname = findViewById(R.id.edit_nickname);
                if (edit_nickname.getText().toString().replace(" ", "").equals("")) { }
                else {
                    items.add(edit_nickname.getText().toString()); //배열에 입력란에서 받은 닉네임 등록
                    adapter.notifyDataSetChanged();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); //등록 후, 키보드가 숨겨지도록 함
                    imm.hideSoftInputFromWindow(edit_nickname.getWindowToken(), 0);

                    edit_nickname.setText(null); //닉네임 입력 후, 다음 입력 시에 리셋

                }
            }
        });

        //게임 시작 버튼
        Button btn_start = findViewById(R.id.startButton);
        btn_start.setOnClickListener(v -> Core.get().getUIManager().invoke(AndroidUIManager.ROOM_START_PORT));

        //방 나가기 버튼: 클릭 시, 이전 화면으로 돌아감. 돌아가기 전에 닉네임 삭제여부를 묻는다.
        Button btn_exit= findViewById(R.id.btn_exit);
        btn_exit.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick (View v){
            AlertDialog.Builder alert = new AlertDialog.Builder(RoomActivity.this);
            alert.setTitle("잠깐!");
            alert.setMessage("닉네임을 삭제하셨습니까?           (닉네임을 길게 누르면 삭제됩니다.)");

            alert.setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Core.get().getUIManager().invoke(AndroidUIManager.EXIT_ROOM_PORT);
                }
            });

            alert.setNegativeButton("아니오", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface dialog, int id)
                {
                    dialog.cancel();
                }
            });
            alert.show();
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
