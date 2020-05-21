package com.example.Client;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import Common.Skill;
import Host.SkillTarget;

public class InGameFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ingame, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button[] buttons = new Button[4];
        buttons[0] = view.findViewById(R.id.btn_q);
        buttons[1] = view.findViewById(R.id.btn_w);
        buttons[2] = view.findViewById(R.id.btn_e);
        buttons[3] = view.findViewById(R.id.btn_r);

        Skill[] skills = Core.getInstance().getInputManager().getThisPlayer().getSkills();
        for (int i = 0 ; i < 4; i++){
            buttons[i].setText(skills[i].getName());
            switch (skills[i].getSkillTargetType()){
                case PLAYER:
                    setPlayerBtnListener(buttons[i], i);
                    break;
                case COORDINATE:
                    setCoordBtnListener(buttons[i], i);
                    break;
                case INSTANT:
                    setInsantBtnListener(buttons[i], i);
                    break;
            }

        }

        Button btn_map = view.findViewById(R.id.btn_map);
        btn_map.setOnClickListener(v -> ((MatchActivity) getActivity()).showDebugMap());
    }

    private void setCoordBtnListener(Button btn, int i){
        btn.setOnClickListener(v -> {
            ((MatchActivity) getActivity()).showClickMap(
                    (lat, lon) -> {
                        Core.getInstance().getInputManager().qwer(new SkillTarget(i, lat, lon));
                        ((MatchActivity) getActivity()).setTopText("게임 진행 중");
                    }
            );
            ((MatchActivity) getActivity()).setTopText("시전 위치를 선택하세요");
        });
    }

    private void setInsantBtnListener(Button btn, int i){
        btn.setOnClickListener(v ->
                Core.getInstance().getInputManager().qwer(new SkillTarget(i))
        );
    }

    private void setPlayerBtnListener(Button btn, int i){

    }
}