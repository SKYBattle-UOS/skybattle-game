package com.example.Client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleObserver;

import java.util.ArrayList;

import Common.CoordinateSkill;
import Common.PlayerTargetSkill;
import Common.ReadOnlyList;
import Common.Skill;
import Host.SkillTarget;

public class InGameFragment extends Fragment {
    private Button[] _buttons;
    private ArrayList<Button> _items = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getLifecycle().addObserver((LifecycleObserver) Core.get().getUIManager());
        return inflater.inflate(R.layout.fragment_ingame, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addSkillButtons();

        Button btn_map = view.findViewById(R.id.btn_map);
        btn_map.setOnClickListener(v -> ((MatchActivity) getActivity()).showDebugMap());

        TextView health = view.findViewById(R.id.health_text);
        AndroidUIManager uiManager = (AndroidUIManager) Core.get().getUIManager();
        uiManager.getHealth().observe(this, i -> health.setText(String.format("체력 : %.1f", (float)(i / 1000))));

        TextView time = view.findViewById(R.id.time_text);
        uiManager.getRemainingTime().observe(this, t -> time.setText(String.format("남은 시간 : %d초", t)));
    }

    public void addSkillButtons(){
        ReadOnlyList<Skill> skills = Core.get().getMatch().getThisPlayer().getProperty().getSkills();
        AndroidUIManager uiManager = (AndroidUIManager) Core.get().getUIManager();
        LinearLayout linLayout = getActivity().findViewById(R.id.ingame_linlayout);

        _buttons = new Button[skills.size()];
        for (int i = 0; i < _buttons.length; i++){
            _buttons[i] = (Button) LayoutInflater
                    .from(getContext()).inflate(R.layout.button_simple, linLayout, false);
            linLayout.addView(_buttons[i], i);

            int finalI = i;
            uiManager.getButtonString(i).observe(this, text -> _buttons[finalI].setText(text));
            uiManager.getButtonEnabled(i).observe(this, bool -> _buttons[finalI].setEnabled(bool));

            setButtonListener(skills.get(i), _buttons[i], i);
        }
    }

    public void clearSkillButtons(){
        LinearLayout linLayout = getActivity().findViewById(R.id.ingame_linlayout);
        for (Button btn : _buttons){
            linLayout.removeView(btn);
        }
    }

    public void addItemButton(OnButtonCreatedListener callback){
        LinearLayout layout = getActivity().findViewById(R.id.ingame_linlayout);
        Button btn = (Button) LayoutInflater
                .from(getContext()).inflate(R.layout.button_simple, layout, false);
        layout.addView(btn);
        _items.add(btn);
        callback.onButtonCreated(btn);
    }

    public void clearItemButtons(){
        LinearLayout layout = getActivity().findViewById(R.id.ingame_linlayout);
        for (Button btn : _items){
            layout.removeView(btn);
        }
        _items.clear();
    }

    public void setButtonListener(Skill skill, Button button, int qwer){
        if (skill instanceof PlayerTargetSkill)
            setPlayerBtnListener(button, qwer);
        else if (skill instanceof CoordinateSkill)
            setCoordBtnListener(button, qwer);
        else
            setInsantBtnListener(button, qwer);
    }

    private void setCoordBtnListener(Button btn, int i){
        AndroidUIManager uiManager = (AndroidUIManager) Core.get().getUIManager();
        btn.setOnClickListener(v -> {
            MatchActivity ma = ((MatchActivity) getActivity());
            ma.showClickMap(
                    (lat, lon) -> Core.get().getInputManager().castSkill(i, new SkillTarget(lat, lon)),
                    () -> uiManager.setTopText(uiManager.getDefaultTopText())
            );
            uiManager.setTopText("시전 위치를 선택하세요");
        });
    }

    private void setInsantBtnListener(Button btn, int i){
        btn.setOnClickListener(v -> {
                Core.get().getInputManager().castSkill(i, new SkillTarget());
                Core.get().getUIManager().setButtonActive(i, false);
            }
        );
    }

    private void setPlayerBtnListener(Button btn, int i){
        AndroidUIManager uiManager = (AndroidUIManager) Core.get().getUIManager();
        btn.setOnClickListener(v -> {
            MatchActivity ma = ((MatchActivity) getActivity());
            uiManager.setTopText("시전 대상을 선택하세요");
            ma.showTargetPlayers(
                networkId -> Core.get().getInputManager().castSkill(i, new SkillTarget(networkId)),
                () -> uiManager.setTopText(uiManager.getDefaultTopText())
            );
        });
    }
}