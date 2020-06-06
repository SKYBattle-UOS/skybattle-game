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

import Common.CharacterFactory;

public class SelectCharacterFragment extends Fragment {
    private Button[] mButtons;
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
        mFactory = mMatchState.getCharacterFactory();
        mButtons = new Button[mFactory.size()];

        for (int i = 0; i < mFactory.size(); i++){
            Button button = (Button) LayoutInflater
                    .from(getContext()).inflate(R.layout.button_simple, (ViewGroup) view, false);
            setUpSimpleButton(button, i);
            ((LinearLayout) view).addView(button);
        }
    }

    private void setUpSimpleButton(Button button, int index){
        mButtons[index] = button;
        button.setText(mFactory.getCharacterName(index));
        button.setOnClickListener(v -> {
            mMatchState.selectCharacter(index);
            for (Button b : mButtons)
                b.setEnabled(false);
            Core.get().getUIManager().setTopText(mFactory.getCharacterName(index) + "을(를) 선택했습니다");
        });
    }
}