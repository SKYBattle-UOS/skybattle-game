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
        if (_ioManager.didHostPressStart()) {
            // assemble
            Log.i("Stub", "GameStateRoom: Start Button Pressed by Host");
            _parent.switchState(GameStateType.MATCH);
            Core.getInstance().getUIManager().switchScreen(ScreenType.ASSEMBLE);
        }
    }
}
