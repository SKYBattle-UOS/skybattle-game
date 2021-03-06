package com.example.Client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import Common.CharacterFactory;

public class SelectCharacterFragment extends Fragment {
    private ArrayList<Button> mButtons = new ArrayList<>();
    private CharacterFactory mFactory;
    private MatchStateSelectCharacter mMatchState;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_select_character, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMatchState = (MatchStateSelectCharacter) ((GameStateMatch) Core.get().getState()).getState();
        mFactory = Core.get().getMatch().getCharacterFactory();

        for (int i : mFactory.getAvailableCharacterIndices()){
            Button button = (Button) LayoutInflater
                    .from(getContext()).inflate(R.layout.button_simple, (ViewGroup) view, false);
            setUpSimpleButton(button, i);

            // 버튼 왼쪽에 캐릭터 이미지 추가하는 코드; 캐릭터 해당 이미지 설정하면 됨
            String character = mFactory.getCharacterName(i).toString();
            switch (character) {
                case "좀비 워너비":
                    button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.character_zombie,
                            0, 0, 0);
                    break;
                case "벌쳐":
                    button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.character_bird,
                            0, 0, 0);
                    break;
                case "회복맨":
                    button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.character_nurse,
                            0, 0, 0);
                    break;

                default:
                    button.setCompoundDrawablesWithIntrinsicBounds(R.drawable.character_knight,
                            0, 0, 0);
            }


            ((LinearLayout) view).addView(button);
        }
    }

    private void setUpSimpleButton(Button button, int index){
        mButtons.add(button);
        button.setText(mFactory.getCharacterName(index));
        button.setOnClickListener(v -> {
            mMatchState.selectCharacter(index);
            for (Button b : mButtons)
                b.setEnabled(false);
            Core.get().getUIManager().setTopText(mFactory.getCharacterName(index) + "을(를) 선택했습니다");
        });
    }
}