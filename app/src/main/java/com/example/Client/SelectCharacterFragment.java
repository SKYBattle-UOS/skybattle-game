package com.example.Client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

        Button button = view.findViewById(R.id.characterButton0);
        button.setText("멋쟁이");
        button.setOnClickListener(v -> matchState.selectCharacter(0));
    }
}