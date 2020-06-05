package com.example.Client;

import Common.GameState;
import Common.GameStateType;

// not used
public class GameStateMain implements GameState {
    private GameStateContext _parent;
    private boolean _goodToGo;

    GameStateMain(GameStateContext parent){
        _parent = parent;
    }

    @Override
    public void update(long ms) {
    }
}