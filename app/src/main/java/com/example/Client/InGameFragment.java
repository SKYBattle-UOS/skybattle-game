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
import java.util.List;

import Common.CoordinateSkill;
import Common.PlayerTargetSkill;
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

        _buttons = new Button[4];
        _buttons[0] = view.findViewById(R.id.btn_q);
        _buttons[1] = view.findViewById(R.id.btn_w);
        _buttons[2] = view.findViewById(R.id.btn_e);
        _buttons[3] = view.findViewById(R.id.btn_r);

        AndroidUIManager uiManager = (AndroidUIManager) Core.get().getUIManager();
        for (int i = 0; i < 4; i++){
            int finalI = i;
            uiManager.getButtonString(i).observe(this, text -> _buttons[finalI].setText(text));
            uiManager.getButtonEnabled(i).observe(this, bool -> _buttons[finalI].setEnabled(bool));
        }

        List<Skill> skills = Core.get().getMatch().getThisPlayer().getSkills();
        for (int i = 0 ; i < 4; i++){
            setButtonListener(skills.get(i), _buttons[i], i + 1);
        }

        Button btn_map = view.findViewById(R.id.btn_map);
        btn_map.setOnClickListener(v -> ((MatchActivity) getActivity()).showDebugMap());

        TextView health = view.findViewById(R.id.health_text);
        uiManager.getHealth().observe(this, i -> health.setText("체력 : " + (i / 1000)));
    }

    public void addItemButton(OnButtonCreatedListener callback){
        LinearLayout layout = getActivity().findViewById(R.id.ingame_fragment);
        Button btn = new Button(getActivity());
        layout.addView(btn);
        _items.add(btn);
        callback.onButtonCreated(btn);
    }

    public void clearItemButtons(){
        LinearLayout layout = getActivity().findViewById(R.id.ingame_fragment);
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
                    (lat, lon) -> Core.get().getInputManager().qwer(new SkillTarget(i, lat, lon)),
                    () -> uiManager.setTopText(uiManager.getDefaultTopText())
            );
            uiManager.setTopText("시전 위치를 선택하세요");
        });
    }

    private void setInsantBtnListener(Button btn, int i){
        btn.setOnClickListener(v ->
                Core.get().getInputManager().qwer(new SkillTarget(i))
        );
    }

    private void setPlayerBtnListener(Button btn, int i){
        AndroidUIManager uiManager = (AndroidUIManager) Core.get().getUIManager();
        btn.setOnClickListener(v -> {
            MatchActivity ma = ((MatchActivity) getActivity());
            uiManager.setTopText("시전 대상을 선택하세요");
            ma.showTargetPlayers(
                networkId -> Core.get().getInputManager().qwer(new SkillTarget(i, networkId)),
                () -> uiManager.setTopText(uiManager.getDefaultTopText())
            );
        });
    }
}