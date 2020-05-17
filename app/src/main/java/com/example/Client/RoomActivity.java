package com.example.Client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RoomActivity extends AppCompatActivity implements Screen {
    private TextView _roomTitle;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        // _roomTitle = findViewById(R.id.roomTitle);

        listView = (ListView)this.findViewById(R.id.playerlist);

        ArrayList<String> items = new ArrayList<>(); //플레이어 닉네임으로 변경할 것
        items.add("닉1");
        items.add("닉2");
        items.add("닉3");
        items.add("닉4");

        CustomAdapter adapter = new CustomAdapter(this,0,items);
        listView.setAdapter((adapter));
    }

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
            TextView textView = (TextView) v.findViewById(R.id.textView2);
            textView.setText(items.get(position));

            final String text = items.get(position);
            Button button1 = (Button) v.findViewById(R.id.button1);
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(RoomActivity.this, text, Toast.LENGTH_SHORT).show();
                }
            });

            Button button2 = (Button) v.findViewById(R.id.button2);
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(RoomActivity.this, text, Toast.LENGTH_SHORT).show();
                }
            });

            return v;
        }
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
        if (type == ScreenType.ASSEMBLE) {
            Intent room_intent = new Intent(RoomActivity.this, MatchActivity.class);
            startActivity(room_intent);
            finish();
        }
    }

    @Override
    public void setText(String text) {
        _roomTitle.setText(text);
    }

}