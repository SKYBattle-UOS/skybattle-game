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

import java.util.function.Consumer;

import Common.GameObject;
import Common.Player;
import Common.PlayerProperty;

public class TargetPlayersFragment extends Fragment {
    private Consumer<Integer> _onButtonClick;

    public TargetPlayersFragment(Consumer<Integer> onButtonClick){
        _onButtonClick = onButtonClick;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.fragment_target_players, container, false);

        for (Player player : Core.get().getMatch().getPlayers()){
            Button btn = new Button(view.getContext());
            btn.setTextSize(20);
            btn.setHeight(200);
            btn.setOnClickListener(v -> _onButtonClick.accept(player.getGameObject().getNetworkId()));

            if (player == Core.get().getMatch().getThisPlayer())
                btn.setText(player.getGameObject().getName() + " (자기 자신)");
            else
                btn.setText(player.getGameObject().getName());

            view.addView(btn);
        }

        return view;
    }
}
