package com.example.Client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DebugMapFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_debug_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btn_up = view.findViewById(R.id.btn_debugUp);
        btn_up.setOnClickListener(v -> Core.get().getInputManager().debugMove(0));

        Button btn_down = view.findViewById(R.id.btn_debugDown);
        btn_down.setOnClickListener(v -> Core.get().getInputManager().debugMove(1));

        Button btn_right = view.findViewById(R.id.btn_debugRight);
        btn_right.setOnClickListener(v -> Core.get().getInputManager().debugMove(2));

        Button btn_left = view.findViewById(R.id.btn_debugLeft);
        btn_left.setOnClickListener(v -> Core.get().getInputManager().debugMove(3));
    }
}
