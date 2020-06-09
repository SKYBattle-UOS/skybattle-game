package com.example.Client;

import Common.GameState;

public class MatchStateGameOver implements GameState {
    private GameStateMatch _match;

    public MatchStateGameOver(GameStateMatch match){
        _match = match;
    }

    @Override
    public void start() {
        Core.get().getUIManager().setDefaultTopText("게임 종료");
    }
}
