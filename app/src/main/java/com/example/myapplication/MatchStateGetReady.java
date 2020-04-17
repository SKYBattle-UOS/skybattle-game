package com.example.myapplication;

import android.util.Log;

public class MatchStateGetReady implements GameState {
    private int _countDown;
    private GameStateMatch _match;

    MatchStateGetReady(GameStateMatch parent, int countInMS){
        _match = parent;
        _countDown = countInMS;
    }

    @Override
    public void run(int ms) {
        _countDown -= ms;
        if (_countDown < 0){
            _match.switchState(MatchStateType.INGAME);
            return;
        }

        Log.i("Stub", String.format("MatchStateGetReady: Showing Get Ready Screen; %d seconds left", _countDown / 1000));
    }
}
