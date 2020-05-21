package com.example.Client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import Host.SkillTarget;

public class InGameFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ingame, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btn_q = view.findViewById(R.id.btn_q);
        btn_q.setOnClickListener(v -> Core.getInstance().getInputManager().qwer(new SkillTarget(0)));

        Button btn_w = view.findViewById(R.id.btn_w);
        btn_w.setOnClickListener(v -> Core.getInstance().getInputManager().qwer(new SkillTarget(1)));

        Button btn_e = view.findViewById(R.id.btn_e);
        btn_e.setOnClickListener(v -> Core.getInstance().getInputManager().qwer(new SkillTarget(2)));

        Button btn_r = view.findViewById(R.id.btn_r);
        btn_r.setOnClickListener(v -> Core.getInstance().getInputManager().qwer(new SkillTarget(3)));

        Button btn_map = view.findViewById(R.id.btn_map);
        btn_map.setOnClickListener(v -> ((MatchActivity) getActivity()).showDebugMap());
    }
}