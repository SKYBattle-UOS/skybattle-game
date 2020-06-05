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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_select_character, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MatchStateSelectCharacter matchState =
                (MatchStateSelectCharacter) ((GameStateMatch) Core.get().getState()).getState();

        CharacterFactory factory = matchState.getCharacterFactory();
        for (int i = 0; i < factory.size(); i++){
            Button button = (Button) LayoutInflater
                    .from(getContext()).inflate(R.layout.button_simple, (ViewGroup) view, false);
            button.setText(factory.getCharacterName(i));
            int finalI = i;
            button.setOnClickListener(v -> matchState.selectCharacter(finalI));
            ((LinearLayout) view).addView(button);
        }
    }
}