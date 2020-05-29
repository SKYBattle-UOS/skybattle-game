package com.example.Client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import Common.CoordinateSkill;
import Common.PlayerTargetSkill;
import Common.Skill;
import Host.SkillTarget;

public class InGameFragment extends Fragment {
    private Button[] buttons;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ingame, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        buttons = new Button[4];
        buttons[0] = view.findViewById(R.id.btn_q);
        buttons[1] = view.findViewById(R.id.btn_w);
        buttons[2] = view.findViewById(R.id.btn_e);
        buttons[3] = view.findViewById(R.id.btn_r);

        AndroidUIManager uiManager = (AndroidUIManager) Core.getInstance().getUIManager();
        for (int i = 0; i < 4; i++){
            int finalI = i;
            uiManager.getButtonString(i).observe(this, text -> buttons[finalI].setText(text));
            uiManager.getButtonEnabled(i).observe(this, bool -> buttons[finalI].setEnabled(bool));
        }

        Skill[] skills = Core.getInstance().getMatch().getThisPlayer().getSkills();
        for (int i = 0 ; i < 4; i++){
            if (skills[i] instanceof PlayerTargetSkill)
                setPlayerBtnListener(buttons[i], i);
            else if (skills[i] instanceof CoordinateSkill)
                setCoordBtnListener(buttons[i], i);
            else
                setInsantBtnListener(buttons[i], i);
        }

        Button btn_map = view.findViewById(R.id.btn_map);
        btn_map.setOnClickListener(v -> ((MatchActivity) getActivity()).showDebugMap());

        TextView health = view.findViewById(R.id.health_text);
        uiManager.getHealth().observe(this, i -> health.setText("체력 : " + (i / 1000)));
    }

    private void setCoordBtnListener(Button btn, int i){
        AndroidUIManager uiManager = (AndroidUIManager) Core.getInstance().getUIManager();
        btn.setOnClickListener(v -> {
            MatchActivity ma = ((MatchActivity) getActivity());
            ma.showClickMap(
                    (lat, lon) -> Core.getInstance().getInputManager().qwer(new SkillTarget(i, lat, lon)),
                    () -> uiManager.setTopText(uiManager.getDefaultTopText())
            );
            uiManager.setTopText("시전 위치를 선택하세요");
        });
    }

    private void setInsantBtnListener(Button btn, int i){
        btn.setOnClickListener(v ->
                Core.getInstance().getInputManager().qwer(new SkillTarget(i))
        );
    }

    private void setPlayerBtnListener(Button btn, int i){
        AndroidUIManager uiManager = (AndroidUIManager) Core.getInstance().getUIManager();
        btn.setOnClickListener(v -> {
            MatchActivity ma = ((MatchActivity) getActivity());
            uiManager.setTopText("시전 대상을 선택하세요");
            ma.showTargetPlayers(
                networkId -> Core.getInstance().getInputManager().qwer(new SkillTarget(i, networkId)),
                () -> uiManager.setTopText(uiManager.getDefaultTopText())
            );
        });
    }
}