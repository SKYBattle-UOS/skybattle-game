package com.example.myapplication;

import android.util.Log;

/**
 * 매치의 각 화면에 대한 상태패턴의 상태 객체 중 준비화면.
 * 게임시작 전 러너들이 도망가는 화면.
 *
 * @author Korimart
 * @version 0.0
 * @since 2020-04-21
 */
public class MatchStateGetReady implements GameState {
    private int _countDown;
    private GameStateMatch _match;

    MatchStateGetReady(GameStateMatch parent, int countInMS){
        _match = parent;
        _countDown = countInMS;
    }

    @Override
    public void update(int ms) {
        _countDown -= ms;
        if (_countDown < 0){
            _match.switchState(MatchStateType.INGAME);
            return;
        }

        // TODO: DEBUG EDIT
        Log.i("Stub", String.format("MatchStateGetReady: Showing Get Ready Screen; %d seconds left", _countDown / 1000));
    }
}
