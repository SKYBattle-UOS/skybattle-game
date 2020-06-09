package com.example.Client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import Common.GameOverState;

public class GameOverFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gameover, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView summary = view.findViewById(R.id.summary_text);
        ((AndroidUIManager) Core.get().getUIManager()).getGameOverState().observe(
                this, state -> summary
                        .setText(String.format("%s 승리", state == GameOverState.HUMANSWIN ? "인간" : "좀비"))
        );
    }
}
