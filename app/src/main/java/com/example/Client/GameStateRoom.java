package com.example.Client;

import android.util.Log;

import Common.GameState;
import Common.GameStateType;
import Common.InputBitStream;

public class GameStateRoom implements GameState {
    private GameStateContext _parent;
    private IOManager _ioManager;

    GameStateRoom(GameStateContext stateContext){
        _parent = stateContext;
        _ioManager = Core.getInstance().getIOManager();
    }

    @Override
    public void update(long ms) {
        // 방 정보 갱신
        if (_ioManager.roomTitleChanged()) {
            Log.i("Stub", "Room Title Changed");
        }

        if (_ioManager.roomModeChanged()) {
            Log.i("Stub", "Room Mode Changed");
        }

        if (_ioManager.roomPlayersInfoChanged()) {
            Log.i("Stub", "Rooom Players Info Changed");
        }

        // 게임 시작
        if (_ioManager.didHostPressStart()) {
            if (_ioManager.roomIsAbleToStart()) {
                Log.i("Stub", "GameStateRoom: Start Button Pressed by Host");
                _parent.switchState(GameStateType.MATCH);
                Core.getInstance().getUIManager().switchScreen(ScreenType.ASSEMBLE);
            } else {
                Log.i("Stub", "GameStateRoom: Game Start Failed");
            }
        }
    }
}
